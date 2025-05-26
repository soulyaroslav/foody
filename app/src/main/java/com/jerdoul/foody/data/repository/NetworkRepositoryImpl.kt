package com.jerdoul.foody.data.repository

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.NetworkErrorHandler
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.error.type.NetworkError
import com.jerdoul.foody.domain.repository.NetworkRepository
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(private val networkErrorHandler: NetworkErrorHandler) : NetworkRepository {
    override suspend fun login(email: String, password: String): Result<String, NetworkError> =
        networkErrorHandler.runWithErrorHandler {
            "Success"
        }
}