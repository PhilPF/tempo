package com.temp0.workout.ui.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.unit.dp
import com.temp0.core.DayState
import com.temp0.workout.ui.components.GlassPanel
import com.temp0.workout.ui.components.HairlineDivider
import com.temp0.workout.ui.components.Mannequin
import com.temp0.workout.ui.components.SectionCaption
import com.temp0.workout.ui.components.SelectionDot
import com.temp0.workout.ui.components.StatChip
import com.temp0.workout.ui.state.ProgressUiState
import com.temp0.workout.ui.state.RecentSessionUi
import com.temp0.workout.ui.state.WeekDayUi
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Radius
import com.temp0.workout.ui.theme.Temp0Spacing
import com.temp0.workout.ui.theme.Temp0Type

@Composable
fun ProgressScreen(state: ProgressUiState, modifier: Modifier = Modifier) {
    val colors = LocalTemp0Colors.current

    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            Column(modifier = Modifier.padding(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.xxxl)) {
                Text(text = "THIS WEEK", style = Temp0Type.captionSmall, color = colors.accentDateLabel)
                Text(
                    text = "Progress",
                    style = Temp0Type.displayHeadline,
                    color = colors.textHeadline,
                    modifier = Modifier.padding(top = Temp0Spacing.xxs),
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Temp0Spacing.xxl)
                    .padding(bottom = Temp0Spacing.xl),
            ) {
                StatChip(value = state.totalSessions.toString(), label = "SESSIONS LOGGED", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(Temp0Spacing.sm))
                StatChip(value = state.dayStreak.toString(), label = "DAY STREAK", modifier = Modifier.weight(1f))
            }
        }

        item {
            GlassPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Temp0Spacing.xxl)
                    .padding(bottom = Temp0Spacing.xl),
            ) {
                Column(modifier = Modifier.padding(Temp0Spacing.xl).fillMaxWidth()) {
                    SectionCaption(text = "Weekly Streak", modifier = Modifier.padding(bottom = Temp0Spacing.lg))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween) {
                        state.weekDays.forEach { day -> WeekDayCircle(day) }
                    }
                }
            }
        }

        item {
            GlassPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Temp0Spacing.xxl)
                    .padding(bottom = Temp0Spacing.xl),
                showGrid = true,
            ) {
                Column(modifier = Modifier.padding(Temp0Spacing.xl).fillMaxWidth()) {
                    SectionCaption(text = "Muscle Balance · 7 Days", modifier = Modifier.padding(bottom = Temp0Spacing.lg))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center) {
                        Mannequin(
                            patches = state.muscleBalancePatches,
                            modifier = Modifier
                                .width(150.dp)
                                .aspectRatio(150f / 201f),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Temp0Spacing.mdAlt),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                    ) {
                        LegendItem(label = "PRIMARY", filled = true)
                        Spacer(modifier = Modifier.width(Temp0Spacing.xl))
                        LegendItem(label = "SECONDARY", filled = false)
                    }
                }
            }
        }

        item {
            SectionCaption(
                text = "Recent Sessions",
                modifier = Modifier.padding(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.sm),
            )
        }

        if (state.recentSessions.isEmpty()) {
            item {
                Text(
                    text = "No sessions yet — complete a workout to see it here.",
                    style = Temp0Type.bodyMonoSmall,
                    color = colors.textDim,
                    modifier = Modifier.padding(horizontal = Temp0Spacing.xxl, vertical = Temp0Spacing.md),
                )
            }
        } else {
            items(state.recentSessions) { session -> RecentSessionRow(session) }
        }

        item { Spacer(modifier = Modifier.height(Temp0Spacing.xxl)) }
    }
}

@Composable
private fun WeekDayCircle(day: WeekDayUi) {
    val colors = LocalTemp0Colors.current
    val (bg, border, fg) = when (day.state) {
        DayState.DONE -> Triple(colors.accent.copy(alpha = 0.18f), colors.accent.copy(alpha = 0.6f), colors.accent)
        DayState.TODAY -> Triple(colors.accent, colors.accent, colors.background)
        DayState.NONE -> Triple(androidx.compose.ui.graphics.Color.Transparent, colors.inactiveDot, colors.textDim)
        DayState.UPCOMING -> Triple(androidx.compose.ui.graphics.Color.Transparent, colors.inactiveDot, colors.textDim)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day.label, style = Temp0Type.captionTiny, color = colors.textDim)
        Spacer(modifier = Modifier.height(Temp0Spacing.xs))
        val circleShape = androidx.compose.foundation.shape.CircleShape
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .background(bg, circleShape)
                .border(1.dp, border, circleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = day.glyph, style = Temp0Type.bodyMonoSmall, color = fg)
        }
    }
}

@Composable
private fun LegendItem(label: String, filled: Boolean) {
    val colors = LocalTemp0Colors.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        SelectionDot(filled = true, accent = if (filled) colors.accent else colors.accentDim, size = 8.dp)
        Text(
            text = label,
            style = Temp0Type.captionTiny,
            color = colors.textSecondary,
            modifier = Modifier.padding(start = Temp0Spacing.xs),
        )
    }
}

@Composable
private fun RecentSessionRow(session: RecentSessionUi) {
    val colors = LocalTemp0Colors.current
    Column(modifier = Modifier.padding(horizontal = Temp0Spacing.xxl)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Temp0Spacing.mdAlt),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = session.name, style = Temp0Type.bodyMonoSmall, color = colors.textPrimary)
                Text(
                    text = session.dateLabel,
                    style = Temp0Type.captionSmall,
                    color = colors.textCaption,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Text(text = session.durationLabel, style = Temp0Type.captionSmall, color = colors.accentDateLabel)
        }
        HairlineDivider()
    }
}
