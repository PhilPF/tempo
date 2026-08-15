package com.temp0.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** The small filled/outline dot used for: the active-routine marker in the Routines list
 *  (filled = active, explicitly *not* a text "ACTIVE" label), the picked/unpicked marker
 *  in the Builder's exercise-library rows, and (via [com.temp0.workout.ui.state.SetDotUi])
 *  the set-progress dots on the Exercise screen. Same visual primitive, different color
 *  rules per caller. */
@Composable
fun SelectionDot(
    filled: Boolean,
    accent: Color,
    modifier: Modifier = Modifier,
    size: Dp = 8.dp,
    inactiveBorder: Color = accent.copy(alpha = 0.4f),
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(if (filled) accent else Color.Transparent)
            .border(1.dp, if (filled) accent else inactiveBorder, CircleShape),
    )
}
