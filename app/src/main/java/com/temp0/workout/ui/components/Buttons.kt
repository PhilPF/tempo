package com.temp0.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Radius
import com.temp0.workout.ui.theme.Temp0Type

/** Solid filled accent button — the app's primary action everywhere (BEGIN/CONTINUE
 *  SESSION, FINISH SET/EXERCISE/SESSION, SAVE ROUTINE). No arrows in labels, per the
 *  design chat's explicit rejection of arrow-decorated buttons. */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = LocalTemp0Colors.current
    val shape = RoundedCornerShape(Temp0Radius.lg)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.4f)
            .clip(shape)
            .background(colors.accent)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = Temp0Type.buttonLabel, color = colors.background)
    }
}

/** Outlined accent button — secondary actions that must stay visually distinct from
 *  [PrimaryButton] (most importantly SKIP REST vs. FINISH SET, a real reported bug: they
 *  must never be confusable). */
@Composable
fun OutlinedAccentButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTemp0Colors.current
    val shape = RoundedCornerShape(Temp0Radius.lg)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .border(1.dp, colors.accent.copy(alpha = 0.55f), shape)
            .clickable(onClick = onClick)
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, style = Temp0Type.buttonLabel, color = colors.accent)
    }
}

/** The one deliberate dashed-border exception in the whole app — "+ NEW ROUTINE" only. */
@Composable
fun DashedOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTemp0Colors.current
    val cornerRadius = Temp0Radius.xl
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .dashedBorder(cornerRadius, colors.accent.copy(alpha = 0.6f))
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "+ $text", style = Temp0Type.buttonLabel, color = colors.accent)
    }
}

private fun Modifier.dashedBorder(cornerRadius: Dp, color: Color): Modifier = this.then(
    Modifier.drawWithContent {
        drawContent()
        val stroke = Stroke(
            width = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f),
        )
        drawRoundRect(
            color = color,
            size = Size(size.width, size.height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius.toPx()),
            style = stroke,
        )
    },
)
