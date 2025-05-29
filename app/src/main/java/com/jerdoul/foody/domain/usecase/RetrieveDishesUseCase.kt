package com.jerdoul.foody.domain.usecase

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.pojo.Dish
import com.jerdoul.foody.domain.pojo.DishType
import com.jerdoul.foody.domain.repository.NetworkRepository
import javax.inject.Inject

class RetrieveDishesUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend operator fun invoke(): Result<List<Dish>, Error> {
        return networkRepository.retrieveDishes()
    }
}