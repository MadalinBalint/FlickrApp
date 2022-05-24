package com.mendelin.flickerapp.di

import com.mendelin.flickerapp.data.remote.FlickrApi
import com.mendelin.flickerapp.data.repository.FlickrRepositoryImpl
import com.mendelin.flickerapp.domain.remote.RetrofitServiceProvider
import com.mendelin.flickerapp.domain.repository.FlickrRepository
import com.mendelin.flickerapp.domain.use_case.PhotoPagingDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    @Named("FlickrApi")
    fun provideFlickrApi(): FlickrApi =
        RetrofitServiceProvider.getService()

    @Provides
    @Singleton
    @Named("FlickrRepository")
    fun provideFlickrRepositoryImpl(@Named("FlickrApi") service: FlickrApi): FlickrRepository =
        FlickrRepositoryImpl(service)

    @Provides
    @Singleton
    @Named("PhotoPagingDataUseCase")
    fun providePhotoPagingDataUseCase(@Named("FlickrRepository") repository: FlickrRepository): PhotoPagingDataUseCase =
        PhotoPagingDataUseCase(repository)
}