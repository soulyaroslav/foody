package com.jerdoul.foody.presentation.auth.login

import androidx.compose.animation.core.Spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.ui.composable.BaseFilledButton
import com.jerdoul.foody.ui.composable.CustomUnderlinedTextField
import com.jerdoul.foody.ui.utils.UiText
import com.jerdoul.foody.utils.extensions.fadeInAnimation
import com.jerdoul.foody.utils.extensions.verticalSlideInAnimation

data class RegistrationState(
    val name: String = "",
    val nameValidationError: UiText? = null,
    val email: String = "",
    val emailValidationError: UiText? = null,
    val password: String = "",
    val passwordValidationError: UiText? = null,
    val confirmPassword: String = "",
    val confirmPasswordValidationError: UiText? = null
)

@Composable
fun RegistrationContent(
    state: RegistrationState,
    onEmailAddressChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.weight(.2f))
        Column(
            modifier = Modifier
                .verticalSlideInAnimation(
                    initialOffsetY = 150f,
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = 100f,
                    delay = 400
                )
                .fadeInAnimation(delay = 400),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CustomUnderlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                error = state.emailValidationError,
                hint = "example@example.com",
                label = "Email Address",
                onTextChanged = {
                }
            )
            CustomUnderlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                error = state.emailValidationError,
                hint = "example@example.com",
                label = "Email Address",
                onTextChanged = { email ->
                    onEmailAddressChanged(email)
                }
            )
            CustomUnderlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                error = state.emailValidationError,
                hint = "example@example.com",
                label = "Email Address",
                onTextChanged = { password ->
                    onPasswordChanged(password)
                }
            )
            CustomUnderlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                error = state.emailValidationError,
                hint = "example@example.com",
                label = "Email Address",
                onTextChanged = {
                }
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
            onClick = {},
            content = {
                Text(
                    text = "Register",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}