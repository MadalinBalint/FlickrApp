package com.mendelin.flickerapp.domain.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mendelin.flickerapp.BuildConfig
import com.mendelin.flickerapp.data.remote.FlickrApi
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object RetrofitServiceProvider {
    private const val TRY_COUNT = 3
    private const val TRY_PAUSE_BETWEEN = 1000L

    private var api: FlickrApi? = null

    private fun okHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
            addInterceptor(
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
            )
        }.build()

    private fun getGsonClient(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    fun getService(): FlickrApi {
        if (api == null) {
            api = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(getGsonClient()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient())
                .build()
                .create(FlickrApi::class.java)
        }

        return api!!
    }
}