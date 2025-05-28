package com.jerdoul.foody.data.validation

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.error.type.ValidationError
import com.jerdoul.foody.domain.validation.ValidationPatternProvider
import com.jerdoul.foody.domain.validation.ValidationProcessor
import com.jerdoul.foody.domain.validation.ValidationType
import java.util.regex.Pattern
import javax.inject.Inject

class NameValidationProcessorImpl @Inject constructor() : ValidationProcessor() {

    override fun validate(type: ValidationType): Result<Unit, ValidationError> {
        return if (type is ValidationType.Name) {
            if (type.name.isEmpty()) {
                Result.Error(ValidationError.Name.EMPTY_NAME)
            } else {
                Result.Success(Unit)
            }
        } else {
            validateNext(type)
        }
    }
}