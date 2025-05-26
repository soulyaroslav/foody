package com.jerdoul.foody.data.validation

import com.jerdoul.foody.domain.validation.ValidationPatternProvider
import com.jerdoul.foody.domain.validation.ValidationType
import javax.inject.Inject

class ValidationPatternProviderImpl @Inject constructor() : ValidationPatternProvider {
    override fun providePattern(type: ValidationType): String {
        return when (type) {
            is ValidationType.Email -> "[a-zA-Z0-9._%+-]{1,256}@[a-zA-Z0-9.-]{1,64}\\.[a-zA-Z]{2,}"
        }
    }
}