package com.jerdoul.foody.data.repository

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.NetworkErrorHandler
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.error.type.NetworkError
import com.jerdoul.foody.domain.repository.NetworkRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(private val networkErrorHandler: NetworkErrorHandler) : NetworkRepository {
    override suspend fun login(email: String, password: String): Result<Boolean, NetworkError> =
        networkErrorHandler.runWithErrorHandler {
            delay(300L)
            true
        }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        configPassword: String
    ): Result<Boolean, NetworkError> =
        networkErrorHandler.runWithErrorHandler {
            delay(300L)
            true
        }
}