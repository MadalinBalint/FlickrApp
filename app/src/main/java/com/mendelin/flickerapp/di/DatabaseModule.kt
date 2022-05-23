package com.mendelin.flickerapp.di

import com.mendelin.flickerapp.data.FlickrDataSource
import com.mendelin.flickerapp.domain.api.RetrofitServiceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideFlickrDataSource(): FlickrDataSource {
        return RetrofitServiceProvider.getService()
    }
}