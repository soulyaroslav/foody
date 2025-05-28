package com.jerdoul.foody.domain.error.type

sealed interface ValidationError : Error {
    enum class Name : ValidationError {
        EMPTY_NAME
    }

    enum class Email : ValidationError {
        EMPTY_EMAIL,
        INVALID_EMAIL
    }

    enum class Password : ValidationError {
        EMPTY_PASSWORD,
        NO_DIGITS,
        NO_LETTERS,
        NO_UPPER_CASE,
        NO_LOWER_CASE,
        NO_SPECIAL_CHARACTERS,
        PASSWORDS_DO_NOT_MATCH
    }
}
