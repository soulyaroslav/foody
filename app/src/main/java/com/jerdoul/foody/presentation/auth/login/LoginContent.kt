package com.jerdoul.foody.presentation.auth.login

import androidx.compose.animation.core.Spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.presentation.auth.AuthorizationAction
import com.jerdoul.foody.ui.composable.BaseFilledButton
import com.jerdoul.foody.ui.composable.CustomPasswordUnderlinedTextField
import com.jerdoul.foody.ui.composable.CustomUnderlinedTextField
import com.jerdoul.foody.ui.utils.UiText
import com.jerdoul.foody.utils.extensions.fadeInAnimation
import com.jerdoul.foody.utils.extensions.verticalSlideInAnimation

data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val emailValidationError: UiText? = null,
    val password: String = "",
    val passwordValidationError: UiText? = null,
)

@Composable
fun LoginContent(
    state: LoginState,
    onAction: (AuthorizationAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .verticalSlideInAnimation(
                    initialOffsetY = -150f,
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = 100f,
                    delay = 400
                )
                .fadeInAnimation(delay = 400),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CustomUnderlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                text = state.email,
                error = state.emailValidationError,
                hint = "example@example.com",
                label = "Email Address",
                onTextChanged = { email ->
                    onAction(AuthorizationAction.ValidateEmail(email))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
            )
            CustomUnderlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                text = state.password,
                error = state.passwordValidationError,
                hint = "1234",
                label = "Password",
                onTextChanged = { password ->
                    onAction(AuthorizationAction.ValidatePassword(password))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        BaseFilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .verticalSlideInAnimation(
                    initialOffsetY = 150f,
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = 100f,
                    delay = 900
                )
                .fadeInAnimation(delay = 900)
                .navigationBarsPadding(),
            onClick = {
                keyboardController?.hide()
                onAction(AuthorizationAction.Login)
            },
            isLoading = state.isLoading,
            loadingContent = {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            content = {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}