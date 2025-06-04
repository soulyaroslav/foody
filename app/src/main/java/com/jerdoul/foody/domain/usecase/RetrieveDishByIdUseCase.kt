package com.jerdoul.foody.domain.usecase

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.repository.NetworkRepository
import javax.inject.Inject

class RetrieveDishByIdUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    class Params private constructor(val id: Int) {
        companion object {
            fun toParams(id: Int) = Params(id)
        }
    }

    suspend operator fun invoke(params: Params): Result<Dish, Error> = with(params) {
        networkRepository.retrieveDishById(id)
    }
}