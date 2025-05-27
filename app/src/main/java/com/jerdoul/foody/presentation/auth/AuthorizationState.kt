package com.jerdoul.foody.presentation.auth

import com.jerdoul.foody.presentation.auth.login.LoginState
import com.jerdoul.foody.presentation.auth.login.RegistrationState

data class AuthorizationState(
    val currentNavItem: NavItem = NavItem.LOGIN,
    val loginState: LoginState = LoginState(),
    val registrationState: RegistrationState = RegistrationState()
)
