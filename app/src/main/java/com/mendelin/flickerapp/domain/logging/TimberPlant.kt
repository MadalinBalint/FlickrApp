package com.mendelin.flickerapp.domain.logging

import com.mendelin.flickerapp.BuildConfig
import timber.log.Timber


object TimberPlant {
    fun plantTimberDebugLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return super.createStackElementTag(element) + ": " + element.lineNumber
                }
            })
        } else {
            Timber.plant(TimberDebugger())
        }
    }
}