package com.jerdoul.foody.presentation.auth

sealed interface AuthorizationAction {
    data class ChangeNavItem(val navItem: NavItem) : AuthorizationAction
    data class ValidateEmail(val email: String) : AuthorizationAction
    data object ValidatePassword : AuthorizationAction
}