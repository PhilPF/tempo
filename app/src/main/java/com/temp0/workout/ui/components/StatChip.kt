package com.temp0.workout.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

/** A Progress-tab stat tile: big serif numeral + small uppercase mono caption, inside a
 *  [GlassPanel]. */
@Composable
fun StatChip(value: String, label: String, modifier: Modifier = Modifier) {
    val colors = LocalTemp0Colors.current
    GlassPanel(modifier = modifier, cornerRadius = com.temp0.workout.ui.theme.Temp0Radius.xxl) {
        Column(modifier = Modifier.padding(Temp0Spacing.mdAlt)) {
            Text(text = value, style = Temp0Type.statNumeral, color = colors.accent)
            Text(
                text = label,
                style = Temp0Type.captionTiny,
                color = colors.textCaption,
                modifier = Modifier.padding(top = Temp0Spacing.xxs),
            )
        }
    }
}
