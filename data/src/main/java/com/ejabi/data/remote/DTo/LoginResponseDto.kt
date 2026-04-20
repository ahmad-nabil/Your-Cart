package com.ejabi.data.remote.DTo

data class LoginResponseDto(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val image: String?,
    val accessToken: String,
    val refreshToken: String?
)