package com.jerdoul.foody.domain.error.type

sealed interface ValidationError : Error {
    enum class Email : ValidationError {
        EMPTY_EMAIL,
        INVALID_EMAIL
    }

    enum class Password : ValidationError {
        EMPTY_PASSWORD,
        INVALID_PASSWORD
    }
}
