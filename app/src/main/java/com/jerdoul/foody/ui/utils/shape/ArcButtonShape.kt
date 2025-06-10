package com.jerdoul.foody.ui.utils.shape

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class ArcButtonShape(
    private val cornerRadius: Dp,
    private val archHeight: () -> Dp = { 0.dp },
    private val horizontalPadding: () -> Dp = { 0.dp },
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = with(density) {
        val radiusPx = cornerRadius.toPx()
        val archPx = archHeight().toPx()
        val left = horizontalPadding().toPx()

        val width = size.width
        val height = size.height

        val right = width - left

        val path = Path().apply {
            // Start after top-left corner
            moveTo(left + radiusPx, 0f)

            // Top-left rounded corner
            arcTo(
                rect = Rect(left, 0f, left + radiusPx * 2, radiusPx * 2),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Top arch (upward)
            cubicTo(
                left + (right - left) * 0.25f, -archPx,
                left + (right - left) * 0.75f, -archPx,
                right - radiusPx, 0f
            )

            // Top-right rounded corner
            arcTo(
                rect = Rect(right - radiusPx * 2, 0f, right, radiusPx * 2),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Right side
            lineTo(right, height - radiusPx)

            // Bottom-right rounded corner
            arcTo(
                rect = Rect(right - radiusPx * 2, height - radiusPx * 2, right, height),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Bottom arch (downward)
            cubicTo(
                left + (right - left) * 0.75f, height + archPx,
                left + (right - left) * 0.25f, height + archPx,
                left + radiusPx, height
            )

            // Bottom-left rounded corner
            arcTo(
                rect = Rect(left, height - radiusPx * 2, left + radiusPx * 2, height),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Left side
            lineTo(left, radiusPx)
            close()
        }

        Outline.Generic(path)
    }
}