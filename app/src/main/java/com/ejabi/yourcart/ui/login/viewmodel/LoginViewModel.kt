package com.ejabi.yourcart.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejabi.domain.model.AuthSession
import com.ejabi.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _login = MutableLiveData<AuthSession?>()
    val login: LiveData<AuthSession?> = _login

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val authSession = loginUseCase(username, password)
                _login.value = authSession
            } catch (e: Exception) {
                _error.value = e.message ?: "فشل تسجيل الدخول"
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetError() {
        _error.value = null
    }
}