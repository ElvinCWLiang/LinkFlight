package com.elvinliang.aviation.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.elvinliang.aviation.data.ConfigRepository
import com.elvinliang.aviation.data.ConfigRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.koin.dsl.module
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideConfigRepository(
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

val repositoryModule = module {

}
