package com.jerdoul.foody.ui.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun BaseFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(vertical = 24.dp),
    shape: Shape = MaterialTheme.shapes.small,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    loadingContent: (@Composable (RowScope) -> Unit)? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors()
        .copy(containerColor = MaterialTheme.colorScheme.primaryContainer),
    content: @Composable (RowScope) -> Unit,
) {
    Button(
        modifier = modifier,
        contentPadding = contentPadding,
        onClick = onClick,
        shape = shape,
        colors = colors,
        enabled = enabled || !isLoading,
        content = {
            if (isLoading) {
                loadingContent?.invoke(this)
            } else {
                content(this)
            }
        }
    )
}