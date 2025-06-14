package com.elvinliang.aviation.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.koin.dsl.module

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun auth(): FirebaseAuth = Firebase.auth
}

val firebaseModule = module {
    single<FirebaseAuth> {
        Firebase.auth
    }
}
