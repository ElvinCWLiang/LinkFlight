package com.elvinliang.aviation.di

import com.elvinliang.aviation.model.service.AccountService
import com.elvinliang.aviation.model.service.impl.AccountServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.koin.dsl.module

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
}

val serviceModule = module {
    single<AccountService> {
        AccountServiceImpl(
            auth = get()
        )
    }
}