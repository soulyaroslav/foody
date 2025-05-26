package com.jerdoul.foody.domain.error

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.NetworkError

interface NetworkErrorHandler {
    suspend fun <D> runWithErrorHandler(block: suspend () -> D): Result<D, NetworkError>
}