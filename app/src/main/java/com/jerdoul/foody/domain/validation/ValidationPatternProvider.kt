package com.jerdoul.foody.domain.validation

interface ValidationPatternProvider {
    fun providePattern(type: ValidationType): String
}