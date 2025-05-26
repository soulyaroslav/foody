package com.jerdoul.foody.domain.usecase

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.NetworkErrorHandler
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.repository.NetworkRepository
import com.jerdoul.foody.domain.validation.ValidationProcessor
import com.jerdoul.foody.domain.validation.ValidationType
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val validationProcessor: ValidationProcessor
) {
    class Params private constructor(val email: String, val password: String) {
        companion object {
            fun toParams(email: String, password: String) = Params(email, password)
        }
    }

    suspend operator fun invoke(params: Params): Result<String, Error> = with(params) {
        val emailType = ValidationType.Email(email)
        when (val result = validationProcessor.validate(emailType)) {
            is Result.Error -> {
                Result.Error(result.error)
            }

            is Result.Success -> {
                networkRepository.login(email, password)
            }
        }
    }
}