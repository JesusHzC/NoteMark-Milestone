package com.jesushz.notemarkmilestone.core.domain.auth

data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val username: String
)