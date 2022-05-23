package com.mendelin.flickerapp.di

import com.mendelin.flickerapp.data.remote.FlickrApi
import com.mendelin.flickerapp.domain.remote.RetrofitServiceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideFlickrDataSource(): FlickrApi {
        return RetrofitServiceProvider.getService()
    }
}