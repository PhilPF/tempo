package com.temp0.workout.ui.state

import com.temp0.core.CompletedSessionRecord
import com.temp0.core.ExerciseLibrary
import com.temp0.core.RoutineSeeds
import com.temp0.workout.data.prefs.UserPrefs
import com.temp0.workout.domain.Routine
import com.temp0.workout.domain.RoutineExerciseItem
import com.temp0.workout.ui.TransientState
import java.time.LocalDate
import java.time.ZoneOffset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Note: this test lives in :app (not :core) because [AppUiStateMapper] depends on
 * app-module domain models ([Routine], [UserPrefs]). It doesn't touch any android.*
 * API, so it runs as a plain JVM unit test wherever the :app module itself can build —
 * this sandbox can't (see the session's final report), but it will in Android Studio.
 */
class AppUiStateMapperTest {

    private val zone = ZoneOffset.UTC
    private val today = LocalDate.of(2026, 8, 15)

    private fun pushDayRoutine(): Routine {
        val seed = RoutineSeeds.all.first { it.id == "push" }
        return Routine(
            id = seed.id,
            name = seed.name,
            items = seed.items.map { RoutineExerciseItem(ExerciseLibrary.byKey(it.exerciseKey), it.sets, it.reps, it.weight) },
        )
    }

    @Test
    fun `home cta label reflects zero completed exercises`() {
        val routine = pushDayRoutine()
        val prefs = UserPrefs(activeRoutineId = routine.id)

        val state = AppUiStateMapper.derive(listOf(routine), prefs, emptyList(), TransientState(), today, zone)

        assertEquals("BEGIN SESSION", state.home.ctaLabel)
        assertEquals(routine.items.size, state.home.totalCount)
        assertEquals(0, state.home.completedCount)
    }

    @Test
    fun `home exercise rows mark completed exercises with a checkmark and dim marker`() {
        val routine = pushDayRoutine()
        val prefs = UserPrefs(activeRoutineId = routine.id)
        val transient = TransientState(completedExerciseIndices = setOf(0))

        val state = AppUiStateMapper.derive(listOf(routine), prefs, emptyList(), transient, today, zone)

        assertEquals("✓", state.home.exercises[0].marker)
        assertTrue(state.home.exercises[0].isDone)
        // Marker numbering is absolute position (index + 1), not renumbered among the
        // remaining undone exercises — matches REGIMEN.dc.html's `num: String(i + 1)...`.
        assertEquals("02", state.home.exercises[1].marker)
    }

    @Test
    fun `exercise screen reflects the selected exercise and set progress`() {
        val routine = pushDayRoutine()
        val prefs = UserPrefs(activeRoutineId = routine.id)
        val transient = TransientState(selectedExerciseIndex = 1, currentSet = 2)

        val state = AppUiStateMapper.derive(listOf(routine), prefs, emptyList(), transient, today, zone)

        assertEquals(routine.items[1].exercise.name, state.exercise.exerciseName)
        assertEquals(2, state.exercise.setDots.count { it.filled })
        assertEquals(TickState.CURRENT, state.exercise.ticks[1])
        assertEquals(TickState.UPCOMING, state.exercise.ticks[2])
    }

    @Test
    fun `progress stats are computed from real session history, not hardcoded`() {
        val routine = pushDayRoutine()
        val prefs = UserPrefs(activeRoutineId = routine.id)
        val records = listOf(
            CompletedSessionRecord("Push Day", today.atStartOfDay(zone).toInstant().toEpochMilli(), 2400, listOf("bench_press")),
        )

        val state = AppUiStateMapper.derive(listOf(routine), prefs, records, TransientState(), today, zone)

        assertEquals(1, state.progress.totalSessions)
        assertEquals(1, state.progress.dayStreak)
    }

    @Test
    fun `builder save label and enabled state depend on whether anything is picked`() {
        val prefs = UserPrefs()
        val emptyBuilder = TransientState()
        val emptyState = AppUiStateMapper.derive(emptyList(), prefs, emptyList(), emptyBuilder, today, zone)
        assertEquals(false, emptyState.builder.saveEnabled)
        assertEquals("SAVE ROUTINE", emptyState.builder.saveLabel)

        val pickedBuilder = TransientState(builder = com.temp0.workout.domain.BuilderDraft(order = listOf("bench_press")))
        val pickedState = AppUiStateMapper.derive(emptyList(), prefs, emptyList(), pickedBuilder, today, zone)
        assertEquals(true, pickedState.builder.saveEnabled)
        assertEquals(1, pickedState.builder.picked.size)
    }
}
