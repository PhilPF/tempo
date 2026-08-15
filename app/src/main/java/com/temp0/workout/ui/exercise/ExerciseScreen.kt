package com.temp0.workout.ui.exercise

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.temp0.workout.ui.components.GlassPanel
import com.temp0.workout.ui.components.Mannequin
import com.temp0.workout.ui.components.OutlinedAccentButton
import com.temp0.workout.ui.components.OverlayHeader
import com.temp0.workout.ui.components.PrimaryButton
import com.temp0.workout.ui.components.SelectionDot
import com.temp0.workout.ui.components.Temp0TextField
import com.temp0.workout.ui.components.Temp0TextFieldVariant
import com.temp0.workout.ui.state.ExerciseUiState
import com.temp0.workout.ui.state.TickState
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Radius
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

@Composable
fun ExerciseScreen(
    state: ExerciseUiState,
    onBack: () -> Unit,
    onFinishSet: () -> Unit,
    onSkipRest: () -> Unit,
    onWeightChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTemp0Colors.current

    Column(modifier = modifier.fillMaxSize()) {
        OverlayHeader(title = state.exerciseName, onBack = onBack)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.mdAlt),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(5.dp),
        ) {
            state.ticks.forEach { tick ->
                val color = when (tick) {
                    TickState.CURRENT -> colors.accent
                    TickState.COMPLETED -> colors.accentDim
                    TickState.UPCOMING -> colors.inactiveDot
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(color, RoundedCornerShape(2.dp)),
                )
            }
        }

        GlassPanel(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.mdAlt),
            cornerRadius = Temp0Radius.huge,
            showGrid = true,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Mannequin(
                    patches = state.patches,
                    showGroundShadow = true,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(200.dp)
                        .aspectRatio(200f / 268f),
                )

                SetsRepsBadge(
                    state = state,
                    onWeightChange = onWeightChange,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(Temp0Spacing.mdAlt),
                )

                AnimatedVisibility(
                    visible = state.restActive,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(Temp0Spacing.mdAlt),
                ) {
                    RestCountdownPill(remainingSeconds = state.restRemainingSeconds)
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.lg)) {
            if (state.restActive) {
                OutlinedAccentButton(text = "SKIP REST", onClick = onSkipRest)
            } else {
                PrimaryButton(text = state.logLabel, onClick = onFinishSet)
            }
        }
    }
}

@Composable
private fun SetsRepsBadge(
    state: ExerciseUiState,
    onWeightChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTemp0Colors.current
    val shape = RoundedCornerShape(Temp0Radius.md)
    Row(
        modifier = modifier
            .widthIn(max = 280.dp)
            .background(colors.accent.copy(alpha = 0.12f), shape)
            .border(1.dp, colors.accent.copy(alpha = 0.35f), shape)
            .padding(horizontal = Temp0Spacing.md, vertical = Temp0Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = state.setsRepsLabel, style = Temp0Type.buttonLabel, color = colors.accentBadgeText)

        if (state.weighted) {
            Temp0TextField(
                value = state.weightInputText,
                onValueChange = onWeightChange,
                variant = Temp0TextFieldVariant.GLASS_BOX,
                numeric = true,
                textStyle = Temp0Type.bodyMonoSmall,
                modifier = Modifier
                    .padding(start = Temp0Spacing.sm)
                    .width(48.dp),
            )
            Text(
                text = state.units.label,
                style = Temp0Type.captionTiny,
                color = colors.textCaption,
                modifier = Modifier.padding(start = 4.dp),
            )
        }

        Row(modifier = Modifier.padding(start = Temp0Spacing.sm), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(5.dp)) {
            state.setDots.forEach { dot -> SelectionDot(filled = dot.filled, accent = colors.accent, size = 7.dp) }
        }
    }
}

@Composable
private fun RestCountdownPill(remainingSeconds: Int) {
    val colors = LocalTemp0Colors.current
    val shape = RoundedCornerShape(Temp0Radius.xxl)
    Column(
        modifier = Modifier
            .background(colors.accent.copy(alpha = 0.14f), shape)
            .border(1.dp, colors.accent.copy(alpha = 0.4f), shape)
            .padding(horizontal = Temp0Spacing.xl, vertical = Temp0Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = remainingSeconds.toString(), style = Temp0Type.badgeDigits, color = colors.accentBadgeText)
        Text(
            text = "RESTING",
            style = Temp0Type.captionTiny,
            color = colors.accent,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}
