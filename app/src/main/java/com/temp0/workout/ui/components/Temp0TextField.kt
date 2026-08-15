package com.temp0.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Radius
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

enum class Temp0TextFieldVariant { UNDERLINE, GLASS_PILL, GLASS_BOX }

private fun Modifier.bottomBorder(width: androidx.compose.ui.unit.Dp, color: Color): Modifier = this.then(
    Modifier.drawBehind {
        val strokeWidth = width.toPx()
        drawLine(
            color = color,
            start = Offset(0f, size.height - strokeWidth / 2),
            end = Offset(size.width, size.height - strokeWidth / 2),
            strokeWidth = strokeWidth,
        )
    },
)

/** The three text-input looks used across the app: [Temp0TextFieldVariant.UNDERLINE] (the
 *  Builder's routine-name field — no box, just a bottom rule), [Temp0TextFieldVariant.GLASS_PILL]
 *  (the exercise search bar), and [Temp0TextFieldVariant.GLASS_BOX] (the small weight-entry
 *  fields — typed input, not +/- steppers, per the design chat). */
@Composable
fun Temp0TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    variant: Temp0TextFieldVariant = Temp0TextFieldVariant.GLASS_BOX,
    textStyle: TextStyle = Temp0Type.bodyMono,
    numeric: Boolean = false,
) {
    val colors = LocalTemp0Colors.current
    val keyboardOptions = if (numeric) KeyboardOptions(keyboardType = KeyboardType.Decimal) else KeyboardOptions.Default
    val resolvedStyle = textStyle.copy(color = colors.textPrimary)

    val containerModifier = when (variant) {
        Temp0TextFieldVariant.UNDERLINE -> Modifier
            .fillMaxWidth()
            .bottomBorder(1.dp, colors.accent.copy(alpha = 0.5f))
            .padding(vertical = Temp0Spacing.smMed, horizontal = 2.dp)
        Temp0TextFieldVariant.GLASS_PILL -> Modifier
            .fillMaxWidth()
            .background(colors.accent.copy(alpha = 0.06f), RoundedCornerShape(Temp0Radius.md))
            .border(1.dp, colors.accent.copy(alpha = 0.25f), RoundedCornerShape(Temp0Radius.md))
            .padding(horizontal = Temp0Spacing.mdAlt, vertical = Temp0Spacing.smMed + 1.dp)
        Temp0TextFieldVariant.GLASS_BOX -> Modifier
            .border(1.dp, colors.accent.copy(alpha = 0.4f), RoundedCornerShape(Temp0Radius.xs))
            .padding(horizontal = Temp0Spacing.sm, vertical = 3.dp)
    }

    Box(modifier = modifier.then(containerModifier), contentAlignment = Alignment.CenterStart) {
        if (value.isEmpty() && placeholder.isNotEmpty()) {
            Text(text = placeholder, style = resolvedStyle.copy(color = colors.textPlaceholder))
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = resolvedStyle,
            singleLine = true,
            cursorBrush = androidx.compose.ui.graphics.SolidColor(colors.accent),
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
