package com.ejabi.data.Repository

import com.ejabi.data.mapper.toDomain
import com.ejabi.data.remote.DTo.LoginRequestDto
import com.ejabi.data.remote.DTo.LoginResponseDto
import com.ejabi.data.remote.api.AuthApi
import com.ejabi.domain.model.AuthSession
import com.ejabi.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val auth: AuthApi): AuthRepository {


    override suspend fun login(
        username: String,
        password: String
    ): AuthSession {
        return auth.login(LoginRequestDto(username,password)).toDomain()
    }

    override suspend fun getSavedSession(): AuthSession? {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }
}