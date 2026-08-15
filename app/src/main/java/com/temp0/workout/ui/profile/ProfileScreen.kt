package com.temp0.workout.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.temp0.core.Units
import com.temp0.workout.ui.components.HairlineDivider
import com.temp0.workout.ui.components.SectionCaption
import com.temp0.workout.ui.state.ProfileUiState
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

private val REST_DURATION_OPTIONS = listOf(30, 45, 60, 90, 120)

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onSetUnits: (Units) -> Unit,
    onSetRestDuration: (Int) -> Unit,
    onSetNotifications: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTemp0Colors.current
    var showRestDialog by remember { mutableStateOf(false) }
    var showUnitsDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.xxxl)) {
            Text(text = "ACCOUNT", style = Temp0Type.captionSmall, color = colors.accentDateLabel)
            Text(
                text = "Profile",
                style = Temp0Type.displayHeadline,
                color = colors.textHeadline,
                modifier = Modifier.padding(top = Temp0Spacing.xxs),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Temp0Spacing.xxl)
                .padding(bottom = Temp0Spacing.xxl),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(colors.accent.copy(alpha = 0.1f))
                    .border(1.dp, colors.accent.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = state.initials, style = Temp0Type.avatarInitials, color = colors.accent)
            }
            Column(modifier = Modifier.padding(start = Temp0Spacing.lg)) {
                Text(text = state.name, style = Temp0Type.bodyMono, color = colors.textPrimary)
                Text(
                    text = state.memberSince,
                    style = Temp0Type.captionSmall,
                    color = colors.textCaption,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }

        SectionCaption(text = "Preferences", modifier = Modifier.padding(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.sm))

        Column(modifier = Modifier.padding(horizontal = Temp0Spacing.xxl)) {
            PreferenceRow(label = "Rest Timer Duration", value = "${state.restDurationSeconds}s", onClick = { showRestDialog = true })
            PreferenceRow(label = "Units", value = state.units.label, onClick = { showUnitsDialog = true })
            PreferenceRow(
                label = "Notifications",
                value = if (state.notificationsEnabled) "On" else "Off",
                onClick = { onSetNotifications(!state.notificationsEnabled) },
            )
        }
    }

    if (showRestDialog) {
        RestDurationPickerDialog(
            current = state.restDurationSeconds,
            onSelect = { onSetRestDuration(it); showRestDialog = false },
            onDismiss = { showRestDialog = false },
        )
    }
    if (showUnitsDialog) {
        UnitsPickerDialog(
            current = state.units,
            onSelect = { onSetUnits(it); showUnitsDialog = false },
            onDismiss = { showUnitsDialog = false },
        )
    }
}

@Composable
private fun PreferenceRow(label: String, value: String, onClick: () -> Unit) {
    val colors = LocalTemp0Colors.current
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = Temp0Spacing.lg),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = label, style = Temp0Type.bodyMonoSmall, color = colors.textPrimary)
            Text(text = value, style = Temp0Type.captionSmall, color = colors.accent)
        }
        HairlineDivider()
    }
}

@Composable
private fun RestDurationPickerDialog(current: Int, onSelect: (Int) -> Unit, onDismiss: () -> Unit) {
    PickerDialog(title = "Rest Timer Duration", options = REST_DURATION_OPTIONS, current = current, labelFor = { "${it}s" }, onSelect = onSelect, onDismiss = onDismiss)
}

@Composable
private fun UnitsPickerDialog(current: Units, onSelect: (Units) -> Unit, onDismiss: () -> Unit) {
    PickerDialog(title = "Units", options = Units.entries, current = current, labelFor = { it.label }, onSelect = onSelect, onDismiss = onDismiss)
}

@Composable
private fun <T> PickerDialog(
    title: String,
    options: List<T>,
    current: T,
    labelFor: (T) -> String,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = LocalTemp0Colors.current
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.background,
        title = { Text(text = title, style = Temp0Type.screenTitle, color = colors.textHeadline) },
        text = {
            Column {
                options.forEach { option ->
                    val isSelected = option == current
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(option) }
                            .padding(vertical = Temp0Spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        com.temp0.workout.ui.components.SelectionDot(filled = isSelected, accent = colors.accent, size = 10.dp)
                        Text(
                            text = labelFor(option),
                            style = Temp0Type.bodyMonoSmall,
                            color = if (isSelected) colors.accent else colors.textPrimary,
                            modifier = Modifier.padding(start = Temp0Spacing.md),
                        )
                    }
                }
            }
        },
        confirmButton = {
            Text(
                text = "CLOSE",
                style = Temp0Type.captionSmall,
                color = colors.accent,
                modifier = Modifier
                    .clickable(onClick = onDismiss)
                    .padding(Temp0Spacing.md),
            )
        },
    )
}
