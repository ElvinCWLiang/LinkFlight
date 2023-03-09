package com.elvinliang.aviation.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.elvinliang.aviation.data.ConfigRepository
import com.elvinliang.aviation.data.ConfigRepositoryImpl
import com.elvinliang.aviation.remote.OpenSkyNetworkService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun configRepository(
        sharedPreferences: SharedPreferences
    ): ConfigRepository {
        return ConfigRepositoryImpl(sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(
        application: Application
    ): SharedPreferences {
        return application.getSharedPreferences("config", Context.MODE_PRIVATE)
    }
}