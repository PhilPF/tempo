package com.temp0.workout.ui.builder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.temp0.workout.ui.components.GlassPanel
import com.temp0.workout.ui.components.HairlineDivider
import com.temp0.workout.ui.components.Mannequin
import com.temp0.workout.ui.components.OverlayHeader
import com.temp0.workout.ui.components.PrimaryButton
import com.temp0.workout.ui.components.SectionCaption
import com.temp0.workout.ui.components.SelectionDot
import com.temp0.workout.ui.components.StepperControl
import com.temp0.workout.ui.components.Temp0TextField
import com.temp0.workout.ui.components.Temp0TextFieldVariant
import com.temp0.workout.ui.state.BuilderUiState
import com.temp0.workout.ui.state.LibraryRowUi
import com.temp0.workout.ui.state.PickedExerciseUi
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Radius
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

@Composable
fun BuilderScreen(
    state: BuilderUiState,
    onBack: () -> Unit,
    onNameChange: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onToggleExercise: (String) -> Unit,
    onAdjustSets: (String, Int) -> Unit,
    onAdjustReps: (String, Int) -> Unit,
    onWeightChange: (String, String) -> Unit,
    onReorder: (Int, Int) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTemp0Colors.current

    Column(modifier = modifier.fillMaxSize()) {
        OverlayHeader(title = state.title, onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Temp0Spacing.xxl),
        ) {
            Temp0TextField(
                value = state.name,
                onValueChange = onNameChange,
                placeholder = "Routine name",
                variant = Temp0TextFieldVariant.UNDERLINE,
                textStyle = Temp0Type.nameInput,
                modifier = Modifier.padding(bottom = Temp0Spacing.xl),
            )

            if (state.hasPicked) {
                SectionCaption(text = "Order & Volume · drag to reorder", modifier = Modifier.padding(bottom = Temp0Spacing.md))
                DragReorderColumn(
                    items = state.picked,
                    key = { it.key },
                    onMove = onReorder,
                    modifier = Modifier.padding(bottom = Temp0Spacing.xxl),
                ) { item, dragHandleModifier ->
                    PickedExerciseRow(
                        item = item,
                        dragHandleModifier = dragHandleModifier,
                        onIncSets = { onAdjustSets(item.key, 1) },
                        onDecSets = { onAdjustSets(item.key, -1) },
                        onIncReps = { onAdjustReps(item.key, 1) },
                        onDecReps = { onAdjustReps(item.key, -1) },
                        onWeightChange = { onWeightChange(item.key, it) },
                        onRemove = { onToggleExercise(item.key) },
                        units = state.units.label,
                    )
                }

                GlassPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Temp0Spacing.xxl),
                    showGrid = true,
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(Temp0Spacing.xl), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center) {
                        Mannequin(
                            patches = state.previewPatches,
                            modifier = Modifier.width(110.dp).aspectRatio(110f / 147f),
                        )
                    }
                }
            }

            SectionCaption(text = "Add Exercises", modifier = Modifier.padding(bottom = Temp0Spacing.sm))
            Temp0TextField(
                value = state.search,
                onValueChange = onSearchChange,
                placeholder = "Search exercises...",
                variant = Temp0TextFieldVariant.GLASS_PILL,
                modifier = Modifier.padding(bottom = Temp0Spacing.xs),
            )

            Column {
                state.filteredLibrary.forEach { row -> LibraryRow(row = row, onToggle = { onToggleExercise(row.key) }) }
            }

            Spacer(modifier = Modifier.height(Temp0Spacing.xxl))
        }

        Column(modifier = Modifier.padding(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.md).padding(bottom = Temp0Spacing.xxl)) {
            PrimaryButton(text = state.saveLabel, onClick = onSave, enabled = state.saveEnabled)
        }
    }
}

@Composable
private fun PickedExerciseRow(
    item: PickedExerciseUi,
    dragHandleModifier: Modifier,
    onIncSets: () -> Unit,
    onDecSets: () -> Unit,
    onIncReps: () -> Unit,
    onDecReps: () -> Unit,
    onWeightChange: (String) -> Unit,
    onRemove: () -> Unit,
    units: String,
) {
    val colors = LocalTemp0Colors.current
    val shape = RoundedCornerShape(Temp0Radius.xl)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.accent.copy(alpha = 0.04f))
            .border(1.dp, colors.accent.copy(alpha = 0.25f), shape)
            .padding(Temp0Spacing.mdAlt),
        verticalAlignment = Alignment.Top,
    ) {
        DragHandleDots(modifier = dragHandleModifier.padding(top = 5.dp))

        Column(modifier = Modifier.weight(1f).padding(start = Temp0Spacing.smMed)) {
            Text(text = item.name, style = Temp0Type.bodyMonoSmall, color = colors.textPrimary, modifier = Modifier.padding(bottom = Temp0Spacing.smMed))
            Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(Temp0Spacing.lgAlt)) {
                Column {
                    Text(text = "SETS", style = Temp0Type.captionTiny, color = colors.textDim, modifier = Modifier.padding(bottom = Temp0Spacing.xs))
                    StepperControl(value = item.sets, onDecrement = onDecSets, onIncrement = onIncSets)
                }
                Column {
                    Text(text = "REPS", style = Temp0Type.captionTiny, color = colors.textDim, modifier = Modifier.padding(bottom = Temp0Spacing.xs))
                    StepperControl(value = item.reps, onDecrement = onDecReps, onIncrement = onIncReps)
                }
                if (item.weighted) {
                    Column {
                        Text(text = "WEIGHT, $units".uppercase(), style = Temp0Type.captionTiny, color = colors.textDim, modifier = Modifier.padding(bottom = Temp0Spacing.xs))
                        Temp0TextField(
                            value = formatWeight(item.weight),
                            onValueChange = onWeightChange,
                            variant = Temp0TextFieldVariant.GLASS_BOX,
                            numeric = true,
                            textStyle = Temp0Type.bodyMonoSmall,
                            modifier = Modifier.width(56.dp),
                        )
                    }
                }
            }
        }

        Text(
            text = "×",
            style = Temp0Type.bodyMono,
            color = colors.textDim,
            modifier = Modifier.clickable(onClick = onRemove),
        )
    }
}

@Composable
private fun DragHandleDots(modifier: Modifier = Modifier) {
    val colors = LocalTemp0Colors.current
    Column(modifier = modifier.wrapContentWidth()) {
        repeat(3) {
            Row {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .padding(1.5.dp)
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(colors.textDim.copy(alpha = 0.7f)),
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryRow(row: LibraryRowUi, onToggle: () -> Unit) {
    val colors = LocalTemp0Colors.current
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(vertical = Temp0Spacing.mdAlt),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(text = row.name, style = Temp0Type.bodyMonoSmall, color = colors.textPrimary)
                Text(
                    text = row.muscleLabel.uppercase(),
                    style = Temp0Type.captionSmall,
                    color = colors.textCaption,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            SelectionDot(filled = row.selected, accent = colors.accent, size = 8.dp)
        }
        HairlineDivider()
    }
}

private fun formatWeight(weight: Float): String =
    if (weight == weight.toInt().toFloat()) weight.toInt().toString() else weight.toString()
