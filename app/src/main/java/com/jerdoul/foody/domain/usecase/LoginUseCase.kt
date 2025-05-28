package com.jerdoul.foody.domain.usecase

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.error.type.NetworkError
import com.jerdoul.foody.domain.repository.NetworkRepository
import com.jerdoul.foody.domain.validation.ValidationProcessor
import com.jerdoul.foody.domain.validation.ValidationType
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val validationProcessor: ValidationProcessor
) {
    class Params private constructor(val email: String, val password: String) {
        companion object {
            fun toParams(email: String, password: String) = Params(email, password)
        }
    }

    suspend operator fun invoke(params: Params): Result<Boolean, Error> = with(params) {
        coroutineScope {
            val emailValidationJob = async {
                val emailType = ValidationType.Email(email)
                validationProcessor.validate(emailType)
            }
            val passwordValidationJob = async {
                val passwordType = ValidationType.Password(password)
                validationProcessor.validate(passwordType)
            }
            val emailValidationResult = emailValidationJob.await()
            val passwordValidationResult = passwordValidationJob.await()

            if (emailValidationResult is Result.Success && passwordValidationResult is Result.Success) {
                networkRepository.login(email, password)
            } else {
                if (emailValidationResult is Result.Error) {
                    Result.Error(emailValidationResult.error)
                } else if (passwordValidationResult is Result.Error) {
                    Result.Error(passwordValidationResult.error)
                } else  {
                    Result.Error(NetworkError.Unknown)
                }
            }
        }
    }
}