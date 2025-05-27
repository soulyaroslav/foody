package com.jerdoul.foody.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jerdoul.foody.ui.theme.FieldTextBottomBorderColor
import com.jerdoul.foody.ui.theme.FieldTextColor
import com.jerdoul.foody.ui.theme.FieldTextHintColor
import com.jerdoul.foody.ui.theme.FieldTextLabelColor
import com.jerdoul.foody.ui.utils.UiText

@Composable
fun CustomUnderlinedTextField(
    modifier: Modifier,
    text: String = "",
    error: UiText? = null,
    hint: String = "",
    label: String = "",
    onTextChanged: (String) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = FieldTextLabelColor
        )
        BasicTextField(
            value = text,
            onValueChange = onTextChanged,
            singleLine = true,
            textStyle = TextStyle(
                color = FieldTextColor
            ),
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