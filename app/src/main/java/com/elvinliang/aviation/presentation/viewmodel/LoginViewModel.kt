package com.elvinliang.aviation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvinliang.aviation.model.service.AccountService
import com.elvinliang.aviation.presentation.MapsActivity.Companion.LOGIN_SCREEN
import com.elvinliang.aviation.presentation.MapsActivity.Companion.MAIN_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {
    private val _state = MutableStateFlow(LoginViewState())
    val state: StateFlow<LoginViewState>
        get() = _state.asStateFlow()

    init {
        accountService.currentUser.map {
            _state.value = _state.value.copy(
                isAnonymous = it.isAnonymous
            )
        }
    }

    fun login(email: String, password: String, openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            kotlin.runCatching {
                isLoading(true)
                accountService.authenticate(email, password)
            }.onSuccess {
                isLoading(false)
                openAndPopUp(MAIN_SCREEN, LOGIN_SCREEN)
                println("evLog_linkAccount_success")
            }.onFailure {
                isLoading(false)
                println("evLog_linkAccount_Fail ${it.message}")
            }
        }
    }

    fun register(email: String, password: String, openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch {
            kotlin.runCatching {
                isLoading(true)
                accountService.linkAccount(email, password)
            }.onSuccess {
                isLoading(false)
                openAndPopUp(MAIN_SCREEN, LOGIN_SCREEN)
                println("evLog_linkAccount_success")
            }.onFailure {
                isLoading(false)
                println("evLog_linkAccount_Fail ${it.message}")
            }
        }
    }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        if (accountService.hasUser && !accountService.isAnonymousUser) openAndPopUp(MAIN_SCREEN, LOGIN_SCREEN)
        else createAnonymousAccount()
    }

    private fun createAnonymousAccount() {
        viewModelScope.launch {
            kotlin.runCatching {
                isLoading(true)
                accountService.createAnonymousAccount()
            }.onSuccess {
                isLoading(false)
            }.onFailure {
                isLoading(false)
            }
        }
    }

    fun isLoading(show: Boolean) {
        _state.value = _state.value.copy(
            isLoading = show
        )
    }
}

data class LoginViewState(
    val isAnonymous: Boolean = true,
    val isLoading: Boolean = false
)
