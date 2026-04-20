package com.ejabi.domain.usecase.auth

import com.ejabi.domain.model.AuthSession
import com.ejabi.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): AuthSession {
        return repository.login(username, password)
    }
}