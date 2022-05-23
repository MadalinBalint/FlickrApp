package com.mendelin.flickerapp.base

import android.app.Application
import com.mendelin.flickerapp.domain.logging.TimberPlant
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FlickrApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        TimberPlant.plantTimberDebugLogger()
    }
}