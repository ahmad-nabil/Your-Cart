package com.ejabi.domain.repository

import com.ejabi.domain.model.AuthSession

interface AuthRepository  {
    suspend fun login(username: String, password: String): AuthSession
    suspend fun getSavedSession(): AuthSession?
    suspend fun logout()
}