package com.temp0.workout.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Type

/** Small uppercase, letter-spaced mono section label ("EXERCISES", "PREFERENCES", "ADD
 *  EXERCISES"...). */
@Composable
fun SectionCaption(text: String, modifier: Modifier = Modifier) {
    val colors = LocalTemp0Colors.current
    Text(
        text = text.uppercase(),
        style = Temp0Type.sectionCaption,
        color = colors.textCaption,
        modifier = modifier,
    )
}
