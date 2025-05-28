package com.jerdoul.foody.domain.validation

sealed interface ValidationType {
    data class Name(val name: String) : ValidationType
    data class Email(val email: String) : ValidationType
    data class Password(val password: String) : ValidationType
    data class ConfirmPassword(val password: String, val confirmPassword: String) : ValidationType
}