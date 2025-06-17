package com.jesushz.notemarkmilestone.auth.domain.repository

import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.EmptyDataResult

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): EmptyDataResult<DataError.Network>
}
