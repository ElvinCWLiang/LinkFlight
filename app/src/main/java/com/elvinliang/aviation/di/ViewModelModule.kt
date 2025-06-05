package com.elvinliang.aviation.di

import com.elvinliang.aviation.presentation.viewmodel.LoginViewModel
import com.elvinliang.aviation.presentation.viewmodel.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        LoginViewModel(
            accountService = get()
        )
    }

    viewModel {
        MainViewModel(
            openSkyNetworkApi = get(),
            configRepository = get(),
            accountService = get()
        )
    }
}