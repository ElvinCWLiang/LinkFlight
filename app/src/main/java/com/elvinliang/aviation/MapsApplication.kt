package com.elvinliang.aviation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MapsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FlipperInitializer.init(this)
    }
}
