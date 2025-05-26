package com.jerdoul.foody.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.jerdoul.foody.ui.utils.Dimensions
import com.jerdoul.foody.ui.utils.WindowDetails
import com.jerdoul.foody.ui.utils.WindowSize
import com.jerdoul.foody.ui.utils.compactDimensions
import com.jerdoul.foody.ui.utils.largeDimensions
import com.jerdoul.foody.ui.utils.mediumDimensions
import com.jerdoul.foody.ui.utils.smallDimensions
import com.jerdoul.foody.ui.utils.AppThemeWrapper
import com.jerdoul.foody.ui.utils.LocalAppDimens

private val ColorScheme = lightColorScheme(
    primary = Primary,
    /** Example: Button text on a primary-colored button. **/
    onPrimary = OnPrimary,
    /** A variant of primary, used for surfaces like cards, filled buttons. **/
    primaryContainer = PrimaryContainer,
    /** Text/icons on primaryContainer **/
    onPrimaryContainer = OnPrimaryContainer,
    background = Background,
    /** Example: Cards, dialogs, bottom sheets **/
    surface = Surface,
    /** Color for text/icons on surface **/
    onSurface = OnSurface
)

@Composable
fun FoodyTheme(
    windowSizeDetails: WindowDetails,
    content: @Composable () -> Unit
) {
    val scaleFactor = when (windowSizeDetails.width) {
        is WindowSize.Small -> .65f
        is WindowSize.Compact -> .8f
        is WindowSize.Medium -> 1f
        is WindowSize.Large ->  1.1f
    }

    val typography = getTypographyForScaleFactor(scaleFactor)

    val dimensions = when (windowSizeDetails.width) {
        is WindowSize.Compact -> compactDimensions
        is WindowSize.Large -> largeDimensions
        is WindowSize.Medium -> mediumDimensions
        is WindowSize.Small -> smallDimensions
    }

    val shapes = when (windowSizeDetails.width) {
        is WindowSize.Compact -> compactShapes
        is WindowSize.Large -> largeShapes
        is WindowSize.Medium -> mediumShapes
        is WindowSize.Small -> smallShapes
    }

    AppThemeWrapper(dimensions = dimensions) {
        MaterialTheme(
            colorScheme = ColorScheme,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

object AppTheme {
    val dimens: Dimensions
        @Composable
        get() = LocalAppDimens.current
}