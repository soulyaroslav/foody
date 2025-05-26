package com.jerdoul.foody.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

@Composable
fun AppThemeWrapper(
    dimensions: Dimensions,
    content: @Composable () -> Unit
) {
    val dimSet = remember {
        dimensions
    }

    CompositionLocalProvider(
        LocalAppDimens provides dimSet,
        content = content
    )
}

val LocalAppDimens = compositionLocalOf { smallDimensions }