package com.temp0.workout.ui.state

import com.temp0.core.ExerciseLibrary
import com.temp0.core.MuscleAggregation
import com.temp0.core.SessionLogic
import com.temp0.core.SessionStats
import com.temp0.core.setsRepsLabel
import com.temp0.workout.data.prefs.UserPrefs
import com.temp0.workout.domain.BuilderExerciseConfig
import com.temp0.workout.domain.Routine
import com.temp0.workout.ui.TransientState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Pure derivation of [AppUiState] from repository snapshots + [TransientState] — the
 * Kotlin analog of the prototype's `renderVals()`. No Android/ViewModel/Compose types
 * here, so this is directly unit-testable.
 */
object AppUiStateMapper {

    fun derive(
        routines: List<Routine>,
        prefs: UserPrefs,
        records: List<com.temp0.core.CompletedSessionRecord>,
        transient: TransientState,
        today: LocalDate,
        zone: ZoneId,
    ): AppUiState {
        val activeRoutine = routines.find { it.id == prefs.activeRoutineId } ?: routines.firstOrNull()

        return AppUiState(
            home = deriveHome(activeRoutine, transient, prefs, today),
            progress = deriveProgress(records, today, zone),
            profile = deriveProfile(prefs),
            routines = deriveRoutines(routines, activeRoutine),
            exercise = deriveExercise(activeRoutine, transient, prefs),
            builder = deriveBuilder(transient, prefs),
        )
    }

    private fun deriveHome(activeRoutine: Routine?, transient: TransientState, prefs: UserPrefs, today: LocalDate): HomeUiState {
        if (activeRoutine == null) return HomeUiState(dateLabel = Formatting.homeDateLabel(today))

        val exercises = activeRoutine.items.mapIndexed { index, item ->
            val done = index in transient.completedExerciseIndices
            HomeExerciseRow(
                index = index,
                name = item.exercise.name,
                setsLabel = setsRepsLabel(item.sets, item.reps, item.weight, prefs.units),
                marker = if (done) "✓" else (index + 1).toString().padStart(2, '0'),
                isDone = done,
            )
        }
        val completedCount = transient.completedExerciseIndices.size
        val totalCount = activeRoutine.items.size

        return HomeUiState(
            dateLabel = Formatting.homeDateLabel(today),
            routineName = activeRoutine.name,
            completedCount = completedCount,
            totalCount = totalCount,
            ctaLabel = SessionLogic.ctaLabel(completedCount, totalCount),
            todayPatches = MuscleAggregation.aggregate(activeRoutine.items.map { it.exercise }),
            exercises = exercises,
        )
    }

    private fun deriveProgress(records: List<com.temp0.core.CompletedSessionRecord>, today: LocalDate, zone: ZoneId): ProgressUiState {
        val sessionDates = records.map { Instant.ofEpochMilli(it.completedAtEpochMillis).atZone(zone).toLocalDate() }.toSet()
        val weekDays = SessionStats.weekDayStates(today, sessionDates).mapIndexed { index, info ->
            WeekDayUi(label = Formatting.weekDayLetter(index), glyph = Formatting.weekDayGlyph(info.state), state = info.state)
        }
        val recentSessions = SessionStats.recentSessions(records, limit = 10).map {
            RecentSessionUi(
                name = it.routineName,
                dateLabel = Formatting.sessionDateLabel(it.completedAtEpochMillis, zone),
                durationLabel = Formatting.durationLabel(it.durationSeconds),
            )
        }
        return ProgressUiState(
            totalSessions = SessionStats.totalSessions(records),
            dayStreak = SessionStats.dayStreak(sessionDates, today),
            weekDays = weekDays,
            muscleBalancePatches = SessionStats.muscleBalanceLast7Days(records, today, zone),
            recentSessions = recentSessions,
        )
    }

    private fun deriveProfile(prefs: UserPrefs): ProfileUiState = ProfileUiState(
        initials = "AM",
        name = "Alex Morgan",
        memberSince = "Member since Jan 2026",
        restDurationSeconds = prefs.restDurationSeconds,
        units = prefs.units,
        notificationsEnabled = prefs.notificationsEnabled,
    )

    private fun deriveRoutines(routines: List<Routine>, activeRoutine: Routine?): RoutinesUiState = RoutinesUiState(
        rows = routines.map { routine ->
            RoutineRowUi(
                id = routine.id,
                name = routine.name,
                exerciseCount = routine.items.size,
                isActive = routine.id == activeRoutine?.id,
                patches = MuscleAggregation.aggregate(routine.items.map { it.exercise }),
            )
        },
    )

    private fun deriveExercise(activeRoutine: Routine?, transient: TransientState, prefs: UserPrefs): ExerciseUiState {
        val items = activeRoutine?.items.orEmpty()
        val currentIndex = transient.selectedExerciseIndex.coerceIn(0, (items.size - 1).coerceAtLeast(0))
        val currentItem = items.getOrNull(currentIndex) ?: return ExerciseUiState(units = prefs.units)

        val ticks = items.indices.map { index ->
            when {
                index == currentIndex -> TickState.CURRENT
                index in transient.completedExerciseIndices -> TickState.COMPLETED
                else -> TickState.UPCOMING
            }
        }
        val setDots = (1..currentItem.sets).map { setNumber -> SetDotUi(filled = setNumber <= transient.currentSet) }
        val hasAnotherIncomplete = items.indices.any { it != currentIndex && it !in transient.completedExerciseIndices }
        val defaultWeightText = currentItem.weight?.let { formatWeightForInput(it) } ?: ""

        return ExerciseUiState(
            exerciseName = currentItem.exercise.name,
            ticks = ticks,
            setsRepsLabel = setsRepsLabel(currentItem.sets, currentItem.reps, currentItem.weight, prefs.units),
            setDots = setDots,
            patches = MuscleAggregation.forSingle(currentItem.exercise),
            restActive = transient.restActive,
            restRemainingSeconds = transient.restRemainingSeconds,
            logLabel = SessionLogic.logLabel(transient.currentSet, currentItem.sets, hasAnotherIncomplete),
            weighted = currentItem.exercise.weighted,
            weightInputText = transient.exerciseWeightInputs[currentItem.exercise.key] ?: defaultWeightText,
            units = prefs.units,
        )
    }

    private fun deriveBuilder(transient: TransientState, prefs: UserPrefs): BuilderUiState {
        val draft = transient.builder
        val picked = draft.order.mapNotNull { key ->
            val def = ExerciseLibrary.byKeyOrNull(key) ?: return@mapNotNull null
            val cfg = draft.config[key] ?: BuilderExerciseConfig()
            PickedExerciseUi(key = key, name = def.name, sets = cfg.sets, reps = cfg.reps, weighted = def.weighted, weight = cfg.weight)
        }
        val previewPatches = MuscleAggregation.aggregate(draft.order.mapNotNull { ExerciseLibrary.byKeyOrNull(it) })
        val search = draft.search.trim().lowercase()
        val filteredLibrary = ExerciseLibrary.all
            .filter { search.isBlank() || it.name.lowercase().contains(search) }
            .map { def ->
                LibraryRowUi(
                    key = def.key,
                    name = def.name,
                    muscleLabel = (def.primary + def.secondary).joinToString(" · ") { it.displayName() },
                    selected = def.key in draft.order,
                )
            }

        return BuilderUiState(
            title = if (draft.isEditing) "Edit Routine" else "New Routine",
            name = draft.name,
            search = draft.search,
            hasPicked = picked.isNotEmpty(),
            picked = picked,
            previewPatches = previewPatches,
            filteredLibrary = filteredLibrary,
            saveLabel = if (draft.isEditing) "SAVE CHANGES" else "SAVE ROUTINE",
            saveEnabled = draft.order.isNotEmpty(),
            units = prefs.units,
        )
    }

    private fun formatWeightForInput(weight: Float): String =
        if (weight == weight.toInt().toFloat()) weight.toInt().toString() else weight.toString()
}
