package com.jerdoul.foody.data.validation

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.error.type.ValidationError
import com.jerdoul.foody.domain.validation.ValidationPatternProvider
import com.jerdoul.foody.domain.validation.ValidationProcessor
import com.jerdoul.foody.domain.validation.ValidationType
import java.util.regex.Pattern
import javax.inject.Inject

class ConfirmPasswordValidationProcessorImpl @Inject constructor() : ValidationProcessor() {

    override fun validate(type: ValidationType): Result<Unit, ValidationError> {
        return if (type is ValidationType.ConfirmPassword) {
            if (type.confirmPassword.isEmpty()) {
                Result.Error(ValidationError.Password.EMPTY_PASSWORD)
            } else if (!type.confirmPassword.any { it.isUpperCase() }) {
                Result.Error(ValidationError.Password.NO_UPPER_CASE)
            } else if (!type.confirmPassword.any { it.isLowerCase() }) {
                Result.Error(ValidationError.Password.NO_LOWER_CASE)
            } else if (!type.confirmPassword.any { it.isDigit() }) {
                Result.Error(ValidationError.Password.NO_DIGITS)
            } else if (!type.confirmPassword.any { !it.isLetterOrDigit() }) {
                Result.Error(ValidationError.Password.NO_SPECIAL_CHARACTERS)
            } else if (type.password != type.confirmPassword) {
                Result.Error(ValidationError.Password.PASSWORDS_DO_NOT_MATCH)
            } else {
                Result.Success(Unit)
            }
        } else {
            validateNext(type)
        }
    }
}