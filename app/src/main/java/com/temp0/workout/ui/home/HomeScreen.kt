package com.temp0.workout.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.temp0.workout.ui.components.GlassPanel
import com.temp0.workout.ui.components.HairlineDivider
import com.temp0.workout.ui.components.Mannequin
import com.temp0.workout.ui.components.PrimaryButton
import com.temp0.workout.ui.components.SectionCaption
import com.temp0.workout.ui.state.HomeExerciseRow
import com.temp0.workout.ui.state.HomeUiState
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

@Composable
fun HomeScreen(
    state: HomeUiState,
    onOpenRoutines: () -> Unit,
    onExerciseClick: (Int) -> Unit,
    onStartNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTemp0Colors.current

    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenRoutines)
                    .padding(start = Temp0Spacing.xxl, end = Temp0Spacing.xxl, top = Temp0Spacing.xxxl, bottom = Temp0Spacing.xl),
            ) {
                Text(text = state.dateLabel.uppercase(), style = Temp0Type.captionSmall, color = colors.accentDateLabel)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = Temp0Spacing.xxs)) {
                    Text(text = state.routineName, style = Temp0Type.displayHeadline, color = colors.textHeadline)
                    Text(
                        text = "›",
                        color = colors.accentChevron,
                        fontSize = androidx.compose.ui.unit.TextUnit(16f, androidx.compose.ui.unit.TextUnitType.Sp),
                        modifier = Modifier.padding(start = Temp0Spacing.sm),
                    )
                }
            }
        }

        item {
            GlassPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Temp0Spacing.xxl)
                    .padding(bottom = Temp0Spacing.xxl),
                showGrid = true,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Temp0Spacing.xl),
                ) {
                    Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        Text(
                            text = "${state.completedCount} / ${state.totalCount.toString().padStart(2, '0')} EXERCISES",
                            style = Temp0Type.captionSmall,
                            color = colors.textPrimary,
                        )
                        Text(
                            text = "TODAY'S FOCUS",
                            style = Temp0Type.captionSmall,
                            color = colors.textCaption,
                            modifier = Modifier.padding(top = Temp0Spacing.xs),
                        )
                        Spacer(modifier = Modifier.height(Temp0Spacing.mdAlt))
                        PrimaryButton(text = state.ctaLabel, onClick = onStartNext)
                    }
                    Spacer(modifier = Modifier.width(Temp0Spacing.mdAlt))
                    Mannequin(
                        patches = state.todayPatches,
                        modifier = Modifier
                            .width(70.dp)
                            .aspectRatio(70f / 94f),
                    )
                }
            }
        }

        item {
            SectionCaption(
                text = "Exercises",
                modifier = Modifier.padding(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.sm),
            )
        }

        items(state.exercises, key = { it.index }) { row ->
            ExerciseListRow(row = row, onClick = { onExerciseClick(row.index) })
        }

        item { Spacer(modifier = Modifier.height(Temp0Spacing.xxl)) }
    }
}

@Composable
private fun ExerciseListRow(row: HomeExerciseRow, onClick: () -> Unit) {
    val colors = LocalTemp0Colors.current
    Column(modifier = Modifier.padding(horizontal = Temp0Spacing.xxl)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .alpha(if (row.isDone) 0.5f else 1f)
                .padding(vertical = Temp0Spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = row.marker,
                style = Temp0Type.bodyMonoSmall,
                color = if (row.isDone) colors.accent else colors.textDim,
                modifier = Modifier.width(28.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = row.name, style = Temp0Type.bodyMono, color = colors.textPrimary)
                Text(
                    text = row.setsLabel,
                    style = Temp0Type.captionSmall,
                    color = colors.textCaption,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Text(text = "›", color = colors.textDim, fontSize = androidx.compose.ui.unit.TextUnit(16f, androidx.compose.ui.unit.TextUnitType.Sp))
        }
        HairlineDivider()
    }
}
