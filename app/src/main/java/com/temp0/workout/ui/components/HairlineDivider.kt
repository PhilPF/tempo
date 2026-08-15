package com.temp0.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.temp0.workout.ui.theme.LocalTemp0Colors

/** The 1px bottom-border used instead of cards on every plain list (exercises, routines
 *  library, recent sessions, preferences) — a deliberate flat/editorial look. */
@Composable
fun HairlineDivider(modifier: Modifier = Modifier) {
    val colors = LocalTemp0Colors.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colors.divider),
    )
}
