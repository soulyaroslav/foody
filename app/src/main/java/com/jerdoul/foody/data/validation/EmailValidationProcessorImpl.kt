package com.jerdoul.foody.data.validation

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.error.type.ValidationError
import com.jerdoul.foody.domain.validation.ValidationPatternProvider
import com.jerdoul.foody.domain.validation.ValidationProcessor
import com.jerdoul.foody.domain.validation.ValidationType
import java.util.regex.Pattern
import javax.inject.Inject

class EmailValidationProcessorImpl @Inject constructor(
    private val patternProvider: ValidationPatternProvider
) : ValidationProcessor() {

    override fun validate(type: ValidationType): Result<Unit, ValidationError> {
        return if (type is ValidationType.Email) {
            if (type.email.isEmpty()) {
                Result.Error(ValidationError.Email.EMPTY_EMAIL)
            } else {
                val pattern = patternProvider.providePattern(type)
                if (Pattern.matches(pattern, type.email)) {
                    Result.Success(Unit)
                } else {
                    Result.Error(ValidationError.Email.INVALID_EMAIL)
                }
            }
        } else {
            validateNext(type)
        }
    }
}