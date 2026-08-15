package com.temp0.workout.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Radius
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

/** The app's 3 bottom-nav destinations. */
enum class BottomTab(val route: String, val label: String) {
    HOME("home", "ROUTINES"),
    PROGRESS("progress", "PROGRESS"),
    PROFILE("profile", "PROFILE"),
}

/**
 * A floating pill nav bar — a deliberate departure from the prototype's flush, squared-off
 * full-width bar, per the user's explicit design direction: maximum/stadium corner
 * rounding, translucent accent glass fill, floating with margin from the screen edges
 * rather than docking flush to the chrome.
 */
@Composable
fun Temp0BottomNav(
    selected: BottomTab,
    onSelect: (BottomTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTemp0Colors.current
    val shape = RoundedCornerShape(Temp0Radius.pill)
    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(bottom = Temp0Spacing.lg)
            .clip(shape)
            .background(colors.accent.copy(alpha = 0.10f))
            .border(1.dp, colors.accent.copy(alpha = 0.30f), shape)
            .padding(horizontal = Temp0Spacing.lg, vertical = Temp0Spacing.md),
    ) {
        BottomTab.entries.forEach { tab ->
            val isSelected = tab == selected
            Text(
                text = tab.label,
                style = Temp0Type.captionTiny,
                color = if (isSelected) colors.accent else colors.textDim,
                modifier = Modifier
                    .width(76.dp)
                    .clickable { onSelect(tab) },
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}
