package com.ejabi.data.remote.DTo

data class LoginRequestDto(    val username: String,
                               val password: String,
                               val expiresInMins: Int = 60
)