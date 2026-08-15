package com.temp0.workout.ui.routines

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.temp0.workout.ui.components.DashedOutlineButton
import com.temp0.workout.ui.components.Mannequin
import com.temp0.workout.ui.components.OverlayHeader
import com.temp0.workout.ui.components.SelectionDot
import com.temp0.workout.ui.state.RoutineRowUi
import com.temp0.workout.ui.state.RoutinesUiState
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Radius
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

@Composable
fun RoutinesListScreen(
    state: RoutinesUiState,
    onBack: () -> Unit,
    onSelectRoutine: (String) -> Unit,
    onEditRoutine: (String) -> Unit,
    onNewRoutine: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        OverlayHeader(title = "Your Routines", onBack = onBack)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = Temp0Spacing.xxl),
        ) {
            items(state.rows, key = { it.id }) { row ->
                RoutineRow(row = row, onSelect = { onSelectRoutine(row.id) }, onEdit = { onEditRoutine(row.id) })
            }
        }

        DashedOutlineButton(
            text = "NEW ROUTINE",
            onClick = onNewRoutine,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.md)
                .padding(bottom = Temp0Spacing.xxl),
        )
    }
}

@Composable
private fun RoutineRow(row: RoutineRowUi, onSelect: () -> Unit, onEdit: () -> Unit) {
    val colors = LocalTemp0Colors.current
    val shape = RoundedCornerShape(Temp0Radius.xxl)
    val borderColor = if (row.isActive) colors.accent.copy(alpha = 0.6f) else colors.border
    val bgColor = if (row.isActive) colors.accent.copy(alpha = 0.08f) else androidx.compose.ui.graphics.Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Temp0Spacing.smMed)
            .clip(shape)
            .background(bgColor)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onSelect)
            .padding(horizontal = Temp0Spacing.lg, vertical = Temp0Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SelectionDot(filled = row.isActive, accent = colors.accent, size = 6.dp)
        Column(modifier = Modifier.weight(1f).padding(start = Temp0Spacing.md)) {
            Text(text = row.name, style = Temp0Type.bodyMono, color = colors.textPrimary)
            Text(
                text = "${row.exerciseCount} EXERCISES",
                style = Temp0Type.captionSmall,
                color = colors.textCaption,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
        Mannequin(
            patches = row.patches,
            modifier = Modifier
                .width(34.dp)
                .aspectRatio(34f / 46f),
        )
        Row(
            modifier = Modifier
                .padding(start = Temp0Spacing.md)
                .clip(RoundedCornerShape(Temp0Radius.sm))
                .border(1.dp, colors.textDim, RoundedCornerShape(Temp0Radius.sm))
                .clickable(onClick = onEdit)
                .padding(horizontal = Temp0Spacing.md, vertical = Temp0Spacing.xs),
        ) {
            Text(text = "EDIT", style = Temp0Type.captionTiny, color = colors.accent)
        }
    }
}
