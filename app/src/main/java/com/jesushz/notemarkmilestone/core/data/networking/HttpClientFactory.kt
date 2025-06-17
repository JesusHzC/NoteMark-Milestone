package com.jesushz.notemarkmilestone.core.data.networking

import com.jesushz.notemarkmilestone.BuildConfig
import com.jesushz.notemarkmilestone.core.domain.auth.AuthInfo
import com.jesushz.notemarkmilestone.core.domain.auth.SessionStorage
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.util.Constants.ENDPOINT_REFRESH_TOKEN
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import kotlin.text.get
import kotlin.text.set

class HttpClientFactory(
    private val engine: HttpClientEngine,
    private val sessionStorage: SessionStorage
) {

    fun build(): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                level = LogLevel.ALL
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                header("X-User-Email", BuildConfig.API_KEY)
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val info = sessionStorage.get()
                        BearerTokens(
                            accessToken = info?.accessToken.orEmpty(),
                            refreshToken = info?.refreshToken.orEmpty()
                        )
                    }
                    refreshTokens {
                        val info = sessionStorage.get()
                        val response = client.post<AccessTokenRequest, AccessTokenResponse>(
                            route = ENDPOINT_REFRESH_TOKEN,
                            body = AccessTokenRequest(
                                refreshToken = info?.refreshToken.orEmpty()
                            )
                        )
                        if (response is Result.Success) {
                            val newAuthInfo = AuthInfo(
                                accessToken = response.data.accessToken,
                                refreshToken = response.data.refreshToken,
                                username = info?.username.orEmpty()
                            )
                            sessionStorage.set(newAuthInfo)
                            BearerTokens(
                                accessToken = newAuthInfo.accessToken,
                                refreshToken = newAuthInfo.refreshToken
                            )
                        } else {
                            BearerTokens(
                                accessToken = "",
                                refreshToken = ""
                            )
                        }
                    }
                }
            }
        }
    }

}