package com.temp0.workout.ui.state

import com.temp0.core.DayState
import com.temp0.core.MannequinPatch
import com.temp0.core.Units

/** The Kotlin analog of the prototype's `renderVals()` — everything the six screens need
 *  to render, derived from repository state + [com.temp0.workout.ui.TransientState] by
 *  [AppUiStateMapper]. Split into per-screen sub-states purely for readability; it's all
 *  produced by one pure mapping pass. */
data class AppUiState(
    val home: HomeUiState,
    val progress: ProgressUiState,
    val profile: ProfileUiState,
    val routines: RoutinesUiState,
    val exercise: ExerciseUiState,
    val builder: BuilderUiState,
)

data class HomeExerciseRow(
    val index: Int,
    val name: String,
    val setsLabel: String,
    val marker: String,
    val isDone: Boolean,
)

data class HomeUiState(
    val dateLabel: String = "",
    val routineName: String = "",
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val ctaLabel: String = "BEGIN SESSION",
    val todayPatches: List<MannequinPatch> = emptyList(),
    val exercises: List<HomeExerciseRow> = emptyList(),
)

data class WeekDayUi(val label: String, val glyph: String, val state: DayState)

data class RecentSessionUi(val name: String, val dateLabel: String, val durationLabel: String)

data class ProgressUiState(
    val totalSessions: Int = 0,
    val dayStreak: Int = 0,
    val weekDays: List<WeekDayUi> = emptyList(),
    val muscleBalancePatches: List<MannequinPatch> = emptyList(),
    val recentSessions: List<RecentSessionUi> = emptyList(),
)

data class ProfileUiState(
    val initials: String = "",
    val name: String = "",
    val memberSince: String = "",
    val restDurationSeconds: Int = 60,
    val units: Units = Units.KG,
    val notificationsEnabled: Boolean = true,
)

data class RoutineRowUi(
    val id: String,
    val name: String,
    val exerciseCount: Int,
    val isActive: Boolean,
    val patches: List<MannequinPatch>,
)

data class RoutinesUiState(
    val rows: List<RoutineRowUi> = emptyList(),
)

enum class TickState { CURRENT, COMPLETED, UPCOMING }

data class SetDotUi(val filled: Boolean)

data class ExerciseUiState(
    val exerciseName: String = "",
    val ticks: List<TickState> = emptyList(),
    val setsRepsLabel: String = "",
    val setDots: List<SetDotUi> = emptyList(),
    val patches: List<MannequinPatch> = emptyList(),
    val restActive: Boolean = false,
    val restRemainingSeconds: Int = 0,
    val logLabel: String = "FINISH SET",
    val weighted: Boolean = false,
    val weightInputText: String = "",
    val units: Units = Units.KG,
)

data class PickedExerciseUi(
    val key: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weighted: Boolean,
    val weight: Float,
)

data class LibraryRowUi(
    val key: String,
    val name: String,
    val muscleLabel: String,
    val selected: Boolean,
)

data class BuilderUiState(
    val title: String = "New Routine",
    val name: String = "",
    val search: String = "",
    val hasPicked: Boolean = false,
    val picked: List<PickedExerciseUi> = emptyList(),
    val previewPatches: List<MannequinPatch> = emptyList(),
    val filteredLibrary: List<LibraryRowUi> = emptyList(),
    val saveLabel: String = "SAVE ROUTINE",
    val saveEnabled: Boolean = false,
    val units: Units = Units.KG,
)
