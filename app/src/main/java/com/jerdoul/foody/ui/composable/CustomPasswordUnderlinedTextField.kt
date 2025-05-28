package com.jerdoul.foody.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.R
import com.jerdoul.foody.ui.theme.FieldTextBottomBorderColor
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.ui.theme.FieldTextHintColor
import com.jerdoul.foody.ui.theme.FieldTextLabelColor
import com.jerdoul.foody.ui.utils.UiText

@Composable
fun CustomPasswordUnderlinedTextField(
    modifier: Modifier,
    text: String = "",
    error: UiText? = null,
    hint: String = "",
    label: String = "",
    onTextChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    enabled: Boolean = true,
//    isPasswordVisible: Boolean = false,
//    onPasswordVisibilityChanged: (Boolean) -> Unit = { },
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = FieldTextLabelColor
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                modifier = Modifier.weight(1f),
                value = text,
                onValueChange = onTextChanged,
                singleLine = true,
                textStyle = TextStyle(
                    color = FieldTextColor
                ),
                enabled = enabled,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = hint,
                                style = MaterialTheme.typography.bodySmall,
                                color = FieldTextHintColor
                            )
                        }
                        innerTextField()
                    }
                }
            )
            if (keyboardOptions.keyboardType == KeyboardType.Password) {
                IconButton(
                    onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }
                ) {
                    val resId = if (isPasswordVisible) {
                        R.drawable.ic_password_hide
                    } else {
                        R.drawable.ic_password_show
                    }
                    Icon(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(FieldTextBottomBorderColor)
        )
        AnimatedVisibility(visible = error != null) {
            Text(
                text = error?.asString() ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onError
            )
        }
    }
}