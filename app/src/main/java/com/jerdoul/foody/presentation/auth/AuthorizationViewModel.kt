package com.jerdoul.foody.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.type.ValidationError
import com.jerdoul.foody.domain.usecase.LoginUseCase
import com.jerdoul.foody.domain.usecase.RegisterUseCase
import com.jerdoul.foody.domain.validation.ValidationProcessor
import com.jerdoul.foody.domain.validation.ValidationType
import com.jerdoul.foody.presentation.asErrorUiText
import com.jerdoul.foody.presentation.auth.login.LoginState
import com.jerdoul.foody.presentation.auth.login.RegistrationState
import com.jerdoul.foody.presentation.navigation.Destination
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.ui.utils.SnackbarController
import com.jerdoul.foody.ui.utils.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val validationProcessor: ValidationProcessor,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val navigator: Navigator
) : ViewModel() {
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
        initialValue = AuthorizationState()
    )

    fun onAction(action: AuthorizationAction) {
        when (action) {
            is AuthorizationAction.ValidateEmail -> validateEmail(action.email)
            is AuthorizationAction.ValidatePassword -> validatePassword(action.password)
            is AuthorizationAction.ChangeNavItem -> changeNavItem(action.navItem)
            AuthorizationAction.Login -> login()
            AuthorizationAction.Register -> register()
            is AuthorizationAction.ValidateConfigPassword -> validateConfirmPassword(action.password)
            is AuthorizationAction.ValidateName -> validateName(action.name)
        }
    }

    private fun validateName(name: String) {
        val type = ValidationType.Name(name)
        _registrationState.update { oldState ->
            oldState.copy(name = name)
        }
        when (val result = validationProcessor.validate(type)) {
            is Result.Error -> {
                _registrationState.update { oldState ->
                    oldState.copy(nameValidationError = result.asErrorUiText())
                }
            }

            is Result.Success -> {
                _registrationState.update { oldState ->
                    oldState.copy(nameValidationError = null)
                }
            }
        }
    }

    private fun validateEmail(email: String) {
        val type = ValidationType.Email(email)

        when (_currentNavItem.value) {
            NavItem.LOGIN -> {
                _loginState.update { oldState ->
                    oldState.copy(email = email)
                }
            }

            NavItem.SIGN_UP -> {
                _registrationState.update { oldState ->
                    oldState.copy(email = email)
                }
            }
        }
        when (val result = validationProcessor.validate(type)) {
            is Result.Error -> {
                when (_currentNavItem.value) {
                    NavItem.LOGIN -> {
                        _loginState.update { oldState ->
                            oldState.copy(emailValidationError = result.asErrorUiText())
                        }
                    }

                    NavItem.SIGN_UP -> {
                        _registrationState.update { oldState ->
                            oldState.copy(emailValidationError = result.asErrorUiText())
                        }
                    }
                }
            }

            is Result.Success -> {
                when (_currentNavItem.value) {
                    NavItem.LOGIN -> {
                        _loginState.update { oldState ->
                            oldState.copy(emailValidationError = null)
                        }
                    }

                    NavItem.SIGN_UP -> {
                        _registrationState.update { oldState ->
                            oldState.copy(emailValidationError = null)
                        }
                    }
                }
            }
        }
    }

    private fun validatePassword(password: String) {
        val type = ValidationType.Password(password)

        when (_currentNavItem.value) {
            NavItem.LOGIN -> {
                _loginState.update { oldState ->
                    oldState.copy(password = password)
                }
            }

            NavItem.SIGN_UP -> {
                _registrationState.update { oldState ->
                    oldState.copy(password = password)
                }
            }
        }
        when (val result = validationProcessor.validate(type)) {
            is Result.Error -> {
                when (_currentNavItem.value) {
                    NavItem.LOGIN -> {
                        _loginState.update { oldState ->
                            oldState.copy(passwordValidationError = result.asErrorUiText())
                        }
                    }

                    NavItem.SIGN_UP -> {
                        _registrationState.update { oldState ->
                            oldState.copy(passwordValidationError = result.asErrorUiText())
                        }
                    }
                }
            }

            is Result.Success -> {
                when (_currentNavItem.value) {
                    NavItem.LOGIN -> {
                        _loginState.update { oldState ->
                            oldState.copy(passwordValidationError = null)
                        }
                    }

                    NavItem.SIGN_UP -> {
                        _registrationState.update { oldState ->
                            oldState.copy(passwordValidationError = null)
                        }
                    }
                }
            }
        }
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        val password = _registrationState.value.password
        val type = ValidationType.ConfirmPassword(
            password = password,
            confirmPassword = confirmPassword
        )
        _registrationState.update { oldState ->
            oldState.copy(confirmPassword = confirmPassword)
        }
        when (val result = validationProcessor.validate(type)) {
            is Result.Error -> {
                _registrationState.update { oldState ->
                    oldState.copy(confirmPasswordValidationError = result.asErrorUiText())
                }
            }

            is Result.Success -> {
                _registrationState.update { oldState ->
                    oldState.copy(confirmPasswordValidationError = null)
                }
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _loginState.update { oldState ->
                oldState.copy(isLoading = true)
            }
            val params = with(_loginState.value) {
                LoginUseCase.Params.toParams(email, password)
            }
            when (val result = loginUseCase(params)) {
                is Result.Error -> {
                    when (result.error) {
                        is ValidationError.Email -> {
                            _loginState.update { oldState ->
                                oldState.copy(emailValidationError = result.asErrorUiText())
                            }
                        }

                        is ValidationError.Password -> {
                            _loginState.update { oldState ->
                                oldState.copy(passwordValidationError = result.asErrorUiText())
                            }
                        }

                        else -> {
                            SnackbarEvent.UiTextMessage(message = result.asErrorUiText())
                                .also { event ->
                                    SnackbarController.showEvent(event)
                                }
                        }
                    }
                }

                is Result.Success -> {
                    navigator.navigate(Destination.DashboardScreen) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            _loginState.update { oldState ->
                oldState.copy(isLoading = false)
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _registrationState.update { oldState ->
                oldState.copy(isLoading = true)
            }
            val params = with(_registrationState.value) {
                RegisterUseCase.Params.toParams(
                    name,
                    email,
                    password,
                    confirmPassword
                )
            }
            when (val result = registerUseCase(params)) {
                is Result.Error -> {
                    when (result.error) {
                        is ValidationError.Email -> {
                            _registrationState.update { oldState ->
                                oldState.copy(emailValidationError = result.asErrorUiText())
                            }
                        }

                        is ValidationError.Password -> {
                            _registrationState.update { oldState ->
                                oldState.copy(passwordValidationError = result.asErrorUiText())
                            }
                        }

                        is ValidationError.Name -> {
                            _registrationState.update { oldState ->
                                oldState.copy(nameValidationError = result.asErrorUiText())
                            }
                        }

                        else -> {
                            SnackbarEvent.UiTextMessage(message = result.asErrorUiText())
                                .also { event ->
                                    SnackbarController.showEvent(event)
                                }
                        }
                    }
                }

                is Result.Success -> {
                    navigator.navigate(Destination.DashboardScreen) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            _registrationState.update { oldState ->
                oldState.copy(isLoading = false)
            }
        }
    }

    private fun changeNavItem(navItem: NavItem) {
        _currentNavItem.update { navItem }
    }
}