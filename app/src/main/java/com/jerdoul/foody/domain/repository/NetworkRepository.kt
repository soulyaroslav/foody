package com.jerdoul.foody.domain.repository

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.NetworkError

interface NetworkRepository {
    suspend fun login(email: String, password: String): Result<Boolean, NetworkError>
    suspend fun register(
        name: String,
        email: String,
        password: String,
        configPassword: String
    ): Result<Boolean, NetworkError>
}