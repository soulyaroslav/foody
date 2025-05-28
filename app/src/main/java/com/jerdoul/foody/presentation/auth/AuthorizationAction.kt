package com.jerdoul.foody.presentation.auth

sealed interface AuthorizationAction {
    data class ChangeNavItem(val navItem: NavItem) : AuthorizationAction
    data class ValidateEmail(val email: String) : AuthorizationAction
    data class ValidatePassword(val password: String) : AuthorizationAction
    data class ValidateConfigPassword(val password: String) : AuthorizationAction
    data class ValidateName(val name: String) : AuthorizationAction
    data object Login : AuthorizationAction
    data object Register : AuthorizationAction
}