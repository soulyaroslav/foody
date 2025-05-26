package com.jerdoul.foody.domain.error.type

sealed interface NetworkError: Error {
    sealed interface Http: NetworkError {
        data object NoNetworkConnection : Http
        data object Unauthorized : Http
        data object RequestTimeout : Http
        data object TooManyRequests : Http
        data object InternalServerError : Http
        data object BadGateway : Http
        data object ServiceUnavailable : Http
    }
    data class Custom(val message: String): NetworkError
    data object Unknown: NetworkError
    data object JsonSerialization: NetworkError
}