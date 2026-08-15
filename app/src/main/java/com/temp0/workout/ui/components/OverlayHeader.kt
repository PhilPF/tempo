package com.temp0.workout.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

/** Back-arrow + title row used by every full-screen overlay (Routines list, Exercise,
 *  Builder). System back (button/gesture) already pops the destination — this arrow is a
 *  visible, tappable equivalent, not a separate mechanism. */
@Composable
fun OverlayHeader(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTemp0Colors.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = Temp0Spacing.xl),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "←",
            color = colors.textPrimary,
            fontSize = 19.sp,
            modifier = Modifier
                .width(20.dp)
                .clickable(onClick = onBack),
        )
        Text(
            text = title,
            style = Temp0Type.screenTitle,
            color = colors.textHeadline,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f, fill = true)
                .padding(start = Temp0Spacing.md),
        )
    }
}
