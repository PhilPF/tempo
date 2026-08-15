package com.temp0.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Radius

/**
 * The recurring "glass / blueprint" panel look used across nearly every card in the app:
 * a translucent accent-tinted fill, a hairline accent border, and — for the panels the
 * design calls "blueprint" ones (Today's Focus, Muscle Balance, the Exercise mannequin
 * card, the Builder preview) — a faint 1px grid drawn behind the content. Every
 * border/fill alpha in the source CSS is just [accent] at a different alpha, so this is
 * the one place that pattern is implemented.
 */
@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = Temp0Radius.xxxl,
    borderAlpha: Float = 0.3f,
    fillAlpha: Float = 0.05f,
    showGrid: Boolean = false,
    gridSpacing: Dp = 14.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = LocalTemp0Colors.current
    val shape = RoundedCornerShape(cornerRadius)
    val gridColor = colors.accent.copy(alpha = 0.10f)
    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.accent.copy(alpha = fillAlpha))
            .then(if (showGrid) Modifier.drawBehind { drawBlueprintGrid(gridSpacing.toPx(), gridColor) } else Modifier)
            .border(1.dp, colors.accent.copy(alpha = borderAlpha), shape),
        content = content,
    )
}

/** Two sets of evenly-spaced 1px lines — reproduces the CSS `linear-gradient` "grid paper"
 *  background used on the blueprint-style panels. */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBlueprintGrid(spacingPx: Float, lineColor: Color) {
    if (spacingPx <= 0f) return
    var x = 0f
    while (x <= size.width) {
        drawLine(lineColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
        x += spacingPx
    }
    var y = 0f
    while (y <= size.height) {
        drawLine(lineColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
        y += spacingPx
    }
}
