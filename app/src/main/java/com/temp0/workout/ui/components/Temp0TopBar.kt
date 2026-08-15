package com.temp0.workout.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

/** The persistent "TEMP0" wordmark strip shown on every screen (the prototype's fixed top
 *  bar, minus its fake iOS clock — the real Android status bar already shows the time). */
@Composable
fun Temp0TopBar(modifier: Modifier = Modifier) {
    val colors = LocalTemp0Colors.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(PaddingValues(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.md)),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Text(text = "TEMP0", style = Temp0Type.wordmark, color = colors.textHeadline)
    }
}
