package com.jesushz.notemarkmilestone.core.domain.auth

interface SessionStorage {
    suspend fun get(): AuthInfo?
    suspend fun set(info: AuthInfo?)
}
