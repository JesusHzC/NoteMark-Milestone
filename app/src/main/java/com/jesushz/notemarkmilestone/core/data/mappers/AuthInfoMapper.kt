package com.jesushz.notemarkmilestone.core.data.mappers

import com.jesushz.notemarkmilestone.core.data.auth.AuthInfoSerializable
import com.jesushz.notemarkmilestone.core.domain.auth.AuthInfo

fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable {
    return AuthInfoSerializable(
        accessToken = accessToken,
        refreshToken = refreshToken,
        username = username
    )
}

fun AuthInfoSerializable.toAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        username = username
    )
}