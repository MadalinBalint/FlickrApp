package com.mendelin.flickerapp.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mendelin.flickerapp.BuildConfig
import com.mendelin.flickerapp.data.remote.FlickrApi
import com.mendelin.flickerapp.data.repository.FlickrRepositoryImpl
import com.mendelin.flickerapp.domain.repository.FlickrRepository
import com.mendelin.flickerapp.domain.use_case.PhotoPagingDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    const val READ_TIMEOUT = 60L
    const val CONNECTION_TIMEOUT = 10L
    const val CACHE_SIZE_BYTES = 16L * 1024L * 1024L // 16 MB

    const val TRY_COUNT = 3
    const val TRY_PAUSE_BETWEEN = 1000L

    @Provides
    @Singleton
    @Named("HttpCache")
    fun provideCache(@ApplicationContext context: Context): Cache {
        val httpsCacheDir = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpsCacheDir, CACHE_SIZE_BYTES)
    }

    @Provides
    @Singleton
    @Named("GsonClient")
    fun provideGsonClient(): Gson =
        GsonBuilder().setLenient()
            .create()

    @Provides
    @Singleton
    @Named("OkHttpInterceptor")
    fun provideInterceptor(): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val url = request.url()
            val builder = request.newBuilder()

            builder.header("accept", "*/*")
            builder.header("Content-Type", "application/json")
            builder.url(url.newBuilder()
                /* API key */
                .addQueryParameter(BuildConfig.QUERY_API_KEY, BuildConfig.FLICKR_API_KEY)
                /* response size and format */
                .addQueryParameter(BuildConfig.QUERY_PER_PAGE, BuildConfig.FLICKR_PER_PAGE.toString())
                .addQueryParameter(BuildConfig.QUERY_FORMAT, "json")
                .addQueryParameter(BuildConfig.QUERY_NOJSONCALLBACK, "1")
                .build()
            )

            var response = chain.proceed(builder.build())

            /* Automatically retry the call for N times if you receive a server error (5xx) */
            var tryCount = 0
            while (response.code() in 500..599 && tryCount < TRY_COUNT) {
                try {
                    response.close()
                    Thread.sleep(TRY_PAUSE_BETWEEN)
                    response = chain.proceed(builder.build())
                    Timber.e("Request is not successful - $tryCount")
                } catch (e: Exception) {
                    Timber.e(e.localizedMessage)
                } finally {
                    tryCount++
                }
            }

            /* Intercept empty body response */
            if ((response.body()?.contentLength() ?: 0L) == 0L) {
                val contentType: MediaType? = response.body()?.contentType()
                val body: ResponseBody = ResponseBody.create(contentType, "{}")
                return@Interceptor response.newBuilder().body(body).build()
            }

            return@Interceptor response
        }

    @Provides
    @Singleton
    @Named("OkHttpClient")
    fun okHttpClient(@Named("HttpCache") cache: Cache, @Named("OkHttpInterceptor") interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder().apply {
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            cache(cache)
            addInterceptor(interceptor)
        }.build()

    @Provides
    @Singleton
    @Named("RetrofitService")
    fun provideRetrofitService(@Named("GsonClient") gson: Gson, @Named("OkHttpClient") okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    @Named("FlickrApi")
    fun provideFlickrApi(@Named("RetrofitService") service: Retrofit): FlickrApi =
        service.create(FlickrApi::class.java)

    @Provides
    @Singleton
    @Named("FlickrRepository")
    fun provideFlickrRepositoryImpl(@Named("FlickrApi") api: FlickrApi): FlickrRepository =
        FlickrRepositoryImpl(api)

    @Provides
    @Singleton
    @Named("PhotoPagingDataUseCase")
    fun providePhotoPagingDataUseCase(@Named("FlickrRepository") repository: FlickrRepository): PhotoPagingDataUseCase =
        PhotoPagingDataUseCase(repository)
}