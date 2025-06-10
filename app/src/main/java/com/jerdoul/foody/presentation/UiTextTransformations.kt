package com.jerdoul.foody.presentation

import com.jerdoul.foody.R
import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.CheckoutError
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.error.type.NetworkError
import com.jerdoul.foody.domain.error.type.ValidationError
import com.jerdoul.foody.ui.utils.UiText

fun Result.Error<*, Error>.asErrorUiText(): UiText {
    return error.asUiText()
}

fun Error.asUiText(): UiText {
    return when (this) {
        is NetworkError -> this.asUiText()
        is ValidationError -> this.asUiText()
        is CheckoutError -> this.asUiText()
    }
}

fun CheckoutError.asUiText(): UiText {
    return when (this) {
        CheckoutError.NO_ITEMS -> UiText.StringResource(id = R.string.you_cart_is_empty)
    }
}

fun NetworkError.asUiText(): UiText {
    return when (this) {
        NetworkError.Http.NoNetworkConnection -> UiText.StringResource(id = R.string.no_network_connection)
        is NetworkError.Custom -> UiText.DynamicString(message)
        NetworkError.Http.BadGateway -> TODO()
        NetworkError.Http.InternalServerError -> TODO()
        NetworkError.Http.RequestTimeout -> TODO()
        NetworkError.Http.ServiceUnavailable -> TODO()
        NetworkError.Http.TooManyRequests -> TODO()
        NetworkError.Http.Unauthorized -> TODO()
        NetworkError.JsonSerialization -> TODO()
        NetworkError.Unknown -> TODO()
    }
}

fun ValidationError.asUiText(): UiText {
    return when (this) {
        ValidationError.Email.EMPTY_EMAIL -> UiText.DynamicString("empty email")
        ValidationError.Email.INVALID_EMAIL -> UiText.DynamicString("invalid email")
        ValidationError.Password.EMPTY_PASSWORD -> UiText.DynamicString("empty password")
        ValidationError.Password.NO_DIGITS -> UiText.DynamicString("no digits")
        ValidationError.Password.NO_LETTERS -> UiText.DynamicString("no letters")
        ValidationError.Password.NO_UPPER_CASE -> UiText.DynamicString("no uppercase")
        ValidationError.Password.NO_LOWER_CASE -> UiText.DynamicString("no lowercase")
        ValidationError.Password.NO_SPECIAL_CHARACTERS -> UiText.DynamicString("no special characters")
        ValidationError.Password.PASSWORDS_DO_NOT_MATCH -> UiText.DynamicString("passwords do not match")
        ValidationError.Name.EMPTY_NAME -> UiText.DynamicString("empty name")
    }
}