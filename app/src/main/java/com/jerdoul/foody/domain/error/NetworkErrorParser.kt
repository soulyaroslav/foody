package com.jerdoul.foody.domain.error

import com.jerdoul.foody.domain.error.type.NetworkError
import retrofit2.HttpException

interface NetworkErrorParser {
    fun parse(exception: HttpException): NetworkError
}