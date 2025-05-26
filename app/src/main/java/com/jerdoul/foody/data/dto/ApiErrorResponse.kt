package com.jerdoul.foody.data.dto

import com.google.gson.annotations.SerializedName
import java.lang.Exception

data class ApiErrorResponse(
    @SerializedName("message") val message: String,
    @SerializedName("code") val code: Int
)
