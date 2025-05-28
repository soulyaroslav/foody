package com.jerdoul.foody.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    data object Splash : Destination()

    @Serializable
    data object AuthorizationScreen : Destination()

    @Serializable
    data object DashboardScreen : Destination()
}