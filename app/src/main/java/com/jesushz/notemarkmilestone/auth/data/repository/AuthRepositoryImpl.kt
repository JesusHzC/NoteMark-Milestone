package com.jesushz.notemarkmilestone.auth.data.repository

import com.jesushz.notemarkmilestone.auth.data.model.LoginRequest
import com.jesushz.notemarkmilestone.auth.data.model.LoginResponse
import com.jesushz.notemarkmilestone.auth.domain.repository.AuthRepository
import com.jesushz.notemarkmilestone.core.data.networking.get
import com.jesushz.notemarkmilestone.core.data.networking.post
import com.jesushz.notemarkmilestone.core.domain.auth.SessionStorage
import com.jesushz.notemarkmilestone.core.domain.auth.AuthInfo
import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.EmptyDataResult
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.networking.asEmptyDataResult
import com.jesushz.notemarkmilestone.core.util.Constants.ENDPOINT_LOGIN
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
): AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): EmptyDataResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = ENDPOINT_LOGIN,
            body = LoginRequest(
                email = email,
                password = password
            )
        )
        if (result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken
                )
            )
        }
        return result.asEmptyDataResult()
    }

}
