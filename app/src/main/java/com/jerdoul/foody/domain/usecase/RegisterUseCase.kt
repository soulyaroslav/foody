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

class RegisterUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val validationProcessor: ValidationProcessor
) {
    class Params private constructor(
        val name: String,
        val email: String,
        val password: String,
        val confirmPassword: String
    ) {
        companion object {
            fun toParams(
                name: String,
                email: String,
                password: String,
                confirmPassword: String
            ) = Params(
                name,
                email,
                password,
                confirmPassword,
            )
        }
    }

    suspend operator fun invoke(params: Params): Result<Boolean, Error> = with(params) {
        coroutineScope {
            val nameValidationJob = async {
                val type = ValidationType.Name(name)
                validationProcessor.validate(type)
            }
            val emailValidationJob = async {
                val type = ValidationType.Email(email)
                validationProcessor.validate(type)
            }
            val passwordValidationJob = async {
                val type = ValidationType.Password(password)
                validationProcessor.validate(type)
            }
            val confirmPasswordValidationJob = async {
                val type = ValidationType.ConfirmPassword(password, confirmPassword)
                validationProcessor.validate(type)
            }

            val nameValidationResult = nameValidationJob.await()
            val emailValidationResult = emailValidationJob.await()
            val passwordValidationResult = passwordValidationJob.await()
            val confirmPasswordValidationResult = confirmPasswordValidationJob.await()

            if (nameValidationResult is Result.Success && emailValidationResult is Result.Success &&
                passwordValidationResult is Result.Success && confirmPasswordValidationResult is Result.Success) {
                networkRepository.login(email, password)
            } else {
                if (nameValidationResult is Result.Error) {
                    Result.Error(nameValidationResult.error)
                } else if (emailValidationResult is Result.Error) {
                    Result.Error(emailValidationResult.error)
                } else if (passwordValidationResult is Result.Error) {
                    Result.Error(passwordValidationResult.error)
                } else if (confirmPasswordValidationResult is Result.Error) {
                    Result.Error(confirmPasswordValidationResult.error)
                } else {
                    Result.Error(NetworkError.Unknown)
                }
            }
        }
    }
}