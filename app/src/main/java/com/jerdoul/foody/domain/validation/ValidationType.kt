package com.jerdoul.foody.domain.validation

sealed interface ValidationType {
    data class Email(val email: String) : ValidationType
}