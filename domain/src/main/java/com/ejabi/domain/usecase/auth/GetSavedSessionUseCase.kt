package com.ejabi.domain.usecase.auth

import com.ejabi.domain.model.AuthSession
import com.ejabi.domain.repository.AuthRepository

class GetSavedSessionUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): AuthSession? {
        return repository.getSavedSession()
    }
}