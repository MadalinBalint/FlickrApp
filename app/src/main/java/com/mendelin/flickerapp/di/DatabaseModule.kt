package com.mendelin.flickerapp.di

import com.mendelin.flickerapp.data.remote.FlickrApi
import com.mendelin.flickerapp.data.repository.FlickrRepositoryImpl
import com.mendelin.flickerapp.domain.remote.RetrofitServiceProvider
import com.mendelin.flickerapp.domain.repository.FlickrRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideFlickrApi(): FlickrApi =
        RetrofitServiceProvider.getService()

    @Provides
    fun provideFlickrRepositoryImpl(service: FlickrApi): FlickrRepository =
        FlickrRepositoryImpl(service)
}