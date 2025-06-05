package com.elvinliang.aviation

import android.app.Application
import com.elvinliang.aviation.di.firebaseModule
import com.elvinliang.aviation.di.networkModule
import com.elvinliang.aviation.di.repositoryModule
import com.elvinliang.aviation.di.useCaseModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@HiltAndroidApp
class FlightyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FlipperInitializer.init(this)

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@FlightyApplication)
            // Load modules
            modules(
                networkModule,
                firebaseModule,
                repositoryModule,
                useCaseModule
            )
        }
    }
}
