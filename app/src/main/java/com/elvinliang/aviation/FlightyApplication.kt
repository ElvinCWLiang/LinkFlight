package com.elvinliang.aviation

import android.app.Application
import com.elvinliang.aviation.di.firebaseModule
import com.elvinliang.aviation.di.networkModule
import com.elvinliang.aviation.di.repositoryModule
import com.elvinliang.aviation.di.serviceModule
import com.elvinliang.aviation.di.useCaseModule
import com.elvinliang.aviation.di.viewModelModule
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class FlightyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initiateKoin()
        initiateOthers()
    }

    private fun initiateKoin() {
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
                useCaseModule,
                viewModelModule,
                serviceModule
            )
        }
    }

    private fun initiateOthers() {
        // ─── Initialize Timber for debug logging ───
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // ─── Initialize Flipper for debugging tools ───
        FlipperInitializer.init(this)
        FirebaseApp.initializeApp(this)
    }
}
