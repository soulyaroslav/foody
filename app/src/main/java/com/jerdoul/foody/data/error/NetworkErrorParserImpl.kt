package com.jerdoul.foody.data.error

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.jerdoul.foody.data.dto.ApiErrorResponse
import com.jerdoul.foody.di.GsonParserQualifier
import com.jerdoul.foody.domain.error.NetworkErrorParser
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.error.type.NetworkError
import retrofit2.HttpException
import javax.inject.Inject

class NetworkErrorParserImpl @Inject constructor(@GsonParserQualifier private val gson: Gson) : NetworkErrorParser {
    override fun parse(exception: HttpException): NetworkError = try {
        exception.response()?.errorBody()?.string()?.let { errorJson ->
            val response = gson.fromJson(errorJson, ApiErrorResponse::class.java)
            NetworkError.Custom(response.message)
        } ?: run {
            if (exception.code() == 401) {
                NetworkError.Http.Unauthorized
            } else if (exception.code() == 408) {
                NetworkError.Http.RequestTimeout
            } else if (exception.code() == 429) {
                NetworkError.Http.TooManyRequests
            } else if (exception.code() == 500) {
                NetworkError.Http.InternalServerError
            } else if (exception.code() == 502) {
                NetworkError.Http.BadGateway
            } else if (exception.code() == 503) {
                NetworkError.Http.ServiceUnavailable
            } else {
                NetworkError.Unknown
            }
        }
    } catch (e: JsonSyntaxException) {
        NetworkError.JsonSerialization
    }
}