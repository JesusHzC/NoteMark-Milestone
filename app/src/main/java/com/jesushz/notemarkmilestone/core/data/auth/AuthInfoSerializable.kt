package com.jesushz.notemarkmilestone.core.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoSerializable(
    val accessToken: String,
    val refreshToken: String
)
