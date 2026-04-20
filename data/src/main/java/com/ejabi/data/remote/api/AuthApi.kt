package com.ejabi.data.remote.api

import com.ejabi.data.remote.DTo.LoginRequestDto
import com.ejabi.data.remote.DTo.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto

}