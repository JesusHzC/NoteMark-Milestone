package com.jesushz.notemarkmilestone.core.data.auth

import com.jesushz.notemarkmilestone.core.domain.auth.AuthInfo

fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable {
    return AuthInfoSerializable(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}

fun AuthInfoSerializable.toAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}