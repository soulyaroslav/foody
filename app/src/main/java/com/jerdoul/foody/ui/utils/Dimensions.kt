package com.jerdoul.foody.ui.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val dimen24: Dp,
    val dimen16: Dp,
    val dimen8: Dp
)

val smallDimensions = Dimensions(
    dimen24 = 18.dp,
    dimen16 = 12.dp,
    dimen8 = 4.dp
)
val compactDimensions = Dimensions(
    dimen24 = 22.dp,
    dimen16 = 14.dp,
    dimen8 = 6.dp
)
val mediumDimensions = Dimensions(
    dimen24 = 24.dp,
    dimen16 = 16.dp,
    dimen8 = 8.dp
)
val largeDimensions = Dimensions(
    dimen24 = 26.dp,
    dimen16 = 18.dp,
    dimen8 = 12.dp
)
