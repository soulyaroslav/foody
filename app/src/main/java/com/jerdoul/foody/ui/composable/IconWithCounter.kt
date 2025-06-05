package com.jerdoul.foody.ui.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import com.jerdoul.foody.ui.theme.FieldTextColor

@Composable
fun IconWithCounter(
    count: Int,
    painter: VectorPainter,
    onShopCartSelected: () -> Unit
) {
    val badgeText = if (count > 99) "99+" else count.toString()
    val textMeasurer = rememberTextMeasurer()
    val badgeColor = MaterialTheme.colorScheme.primary

    IconButton(
        onClick = { onShopCartSelected() }
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .drawWithContent {
                    // Draw the icon
                    drawContent()

                    if (count > 0) {
                        // Calculate badge size based on icon size
                        val badgeRadius = size.minDimension * .3f // ~30% of smallest dimension
                        val badgeCenter = Offset(
                            x = size.width - badgeRadius * .9f, // slight shift inward
                            y = badgeRadius * .9f
                        )

                        // Draw the badge circle
                        drawCircle(
                            color = badgeColor,
                            radius = badgeRadius,
                            center = badgeCenter
                        )

                        // Measure and draw the text centered in the badge
                        val textLayoutResult = textMeasurer.measure(
                            text = AnnotatedString(badgeText),
                            style = TextStyle(
                                fontSize = badgeRadius.toSp(),
                                color = FieldTextColor,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        )

                        val textOffset = Offset(
                            x = badgeCenter.x - textLayoutResult.size.width / 2,
                            y = badgeCenter.y - textLayoutResult.size.height / 2
                        )

                        drawText(
                            textLayoutResult,
                            topLeft = textOffset
                        )
                    }
                }
        )
    }
}