package com.temp0.workout.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Radius
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

/** The -/+ stepper used for Sets and Reps in the Builder's "Order & Volume" rows. */
@Composable
fun StepperControl(
    value: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTemp0Colors.current
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        StepperGlyphBox("−", onDecrement)
        Text(
            text = value.toString(),
            style = Temp0Type.bodyMonoSmall,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(22.dp)
                .padding(horizontal = Temp0Spacing.sm),
        )
        StepperGlyphBox("+", onIncrement)
    }
}

@Composable
private fun StepperGlyphBox(glyph: String, onClick: () -> Unit) {
    val colors = LocalTemp0Colors.current
    val shape = RoundedCornerShape(Temp0Radius.xs)
    Box(
        modifier = Modifier
            .size(22.dp)
            .border(1.dp, colors.accent.copy(alpha = 0.5f), shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = glyph, style = Temp0Type.bodyMonoSmall, color = colors.accent)
    }
}
