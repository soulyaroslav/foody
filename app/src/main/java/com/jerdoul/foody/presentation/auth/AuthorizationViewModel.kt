package com.jerdoul.foody.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.validation.ValidationProcessor
import com.jerdoul.foody.domain.validation.ValidationType
import com.jerdoul.foody.presentation.asErrorUiText
import com.jerdoul.foody.presentation.auth.login.LoginState
import com.jerdoul.foody.presentation.auth.login.RegistrationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(private val validationProcessor: ValidationProcessor) : ViewModel() {
    private val _currentNavItem = MutableStateFlow(NavItem.LOGIN)
    private val _loginState = MutableStateFlow(LoginState())
    private val _registrationState = MutableStateFlow(RegistrationState())

    val state = combine(_loginState, _registrationState, _currentNavItem) { loginState, registrationState, currentNavItem ->
        AuthorizationState(
            loginState = loginState,
            registrationState = registrationState,
            currentNavItem = currentNavItem
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AuthorizationState())

    fun onAction(action: AuthorizationAction) {
        when (action) {
            is AuthorizationAction.ValidateEmail -> validateEmail(action.email)

            is AuthorizationAction.ValidatePassword -> {

            }

            is AuthorizationAction.ChangeNavItem -> changeNavItem(action.navItem)
        }
    }

    private fun validateEmail(email: String) {
        val type = ValidationType.Email(email)
        _loginState.update { oldState ->
            oldState.copy(email = email)
        }
        when(val result = validationProcessor.validate(type)) {
            is Result.Error -> {
                _loginState.update { oldState ->
                    oldState.copy(emailValidationError = result.asErrorUiText())
                }
            }
            is Result.Success -> {
                _loginState.update { oldState ->
                    oldState.copy(emailValidationError = null)
                }
            }
        }
    }

    private fun changeNavItem(navItem: NavItem) {
        _currentNavItem.update { navItem }
    }
}