package com.temp0.core

/** In-progress-session state for the active routine: which exercise/set we're on and which
 *  exercises (by index into the routine's item list) are already completed this session. */
data class SessionProgress(
    val selectedExerciseIndex: Int = 0,
    val currentSet: Int = 1,
    val completedExerciseIndices: Set<Int> = emptySet(),
)

/** Result of [SessionLogic.completeSet] — which of the three prototype behaviors applies. */
sealed interface CompleteSetResult {
    /** Still mid-exercise: advance to the next set and start a rest timer. */
    data class SetAdvanced(val progress: SessionProgress) : CompleteSetResult

    /** Finished this exercise, and at least one incomplete exercise remains: jump to it,
     *  staying on the same screen (no return to Home). */
    data class ExerciseAdvanced(val progress: SessionProgress, val nextExerciseIndex: Int) : CompleteSetResult

    /** Finished this exercise and it was the last incomplete one: the whole routine is
     *  done — the caller should record a session and return Home. */
    data class SessionFinished(val progress: SessionProgress) : CompleteSetResult
}

/**
 * Pure port of the prototype's `completeSet` / `ctaLabel` / `logLabel` logic (the
 * `Component` class in REGIMEN.dc.html) — no Android/ViewModel/coroutine concerns here, so
 * it's directly unit-testable.
 */
object SessionLogic {

    fun completeSet(
        progress: SessionProgress,
        totalSetsForCurrentExercise: Int,
        totalExercises: Int,
    ): CompleteSetResult {
        if (progress.currentSet < totalSetsForCurrentExercise) {
            return CompleteSetResult.SetAdvanced(progress.copy(currentSet = progress.currentSet + 1))
        }
        val done = progress.completedExerciseIndices + progress.selectedExerciseIndex
        val next = (0 until totalExercises).firstOrNull { it != progress.selectedExerciseIndex && it !in done }
        return if (next != null) {
            CompleteSetResult.ExerciseAdvanced(
                progress = SessionProgress(selectedExerciseIndex = next, currentSet = 1, completedExerciseIndices = done),
                nextExerciseIndex = next,
            )
        } else {
            CompleteSetResult.SessionFinished(
                progress = SessionProgress(selectedExerciseIndex = progress.selectedExerciseIndex, currentSet = 1, completedExerciseIndices = done),
            )
        }
    }

    fun ctaLabel(completedCount: Int, totalExercises: Int): String = when {
        totalExercises > 0 && completedCount >= totalExercises -> "SESSION COMPLETE"
        completedCount == 0 -> "BEGIN SESSION"
        else -> "CONTINUE SESSION"
    }

    /** [hasAnotherIncompleteExercise] = true if some exercise other than the current one is
     *  still incomplete — mirrors the prototype's `nextIndex >= 0 && nextIndex !== selected`. */
    fun logLabel(currentSet: Int, totalSetsForCurrentExercise: Int, hasAnotherIncompleteExercise: Boolean): String = when {
        currentSet < totalSetsForCurrentExercise -> "FINISH SET"
        hasAnotherIncompleteExercise -> "FINISH EXERCISE"
        else -> "FINISH SESSION"
    }
}
