package com.ejabi.data.mapper

import com.ejabi.data.remote.DTo.LoginResponseDto
import com.ejabi.domain.model.AuthSession
import com.ejabi.domain.model.User

fun LoginResponseDto.toDomain(): AuthSession {
    return AuthSession(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = User(
            id = id,
            username = username,
            email = email,
            firstName = firstName,
            lastName = lastName,
            image = image
        )
    )
}