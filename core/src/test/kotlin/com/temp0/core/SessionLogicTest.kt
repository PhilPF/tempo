package com.temp0.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionLogicTest {

    @Test
    fun `completeSet mid-exercise advances the set counter`() {
        val progress = SessionProgress(selectedExerciseIndex = 0, currentSet = 2, completedExerciseIndices = emptySet())

        val result = SessionLogic.completeSet(progress, totalSetsForCurrentExercise = 4, totalExercises = 3)

        assertTrue(result is CompleteSetResult.SetAdvanced)
        assertEquals(3, (result as CompleteSetResult.SetAdvanced).progress.currentSet)
    }

    @Test
    fun `completeSet on the last set jumps to the next incomplete exercise, not the next index blindly`() {
        // Exercise 0 done; exercise 1 already completed earlier; should land on exercise 2.
        val progress = SessionProgress(selectedExerciseIndex = 0, currentSet = 4, completedExerciseIndices = setOf(1))

        val result = SessionLogic.completeSet(progress, totalSetsForCurrentExercise = 4, totalExercises = 3)

        assertTrue(result is CompleteSetResult.ExerciseAdvanced)
        val advanced = result as CompleteSetResult.ExerciseAdvanced
        assertEquals(2, advanced.nextExerciseIndex)
        assertEquals(setOf(0, 1), advanced.progress.completedExerciseIndices)
        assertEquals(1, advanced.progress.currentSet)
    }

    @Test
    fun `completeSet on the last remaining exercise finishes the session`() {
        val progress = SessionProgress(selectedExerciseIndex = 2, currentSet = 4, completedExerciseIndices = setOf(0, 1))

        val result = SessionLogic.completeSet(progress, totalSetsForCurrentExercise = 4, totalExercises = 3)

        assertTrue(result is CompleteSetResult.SessionFinished)
        assertEquals(setOf(0, 1, 2), (result as CompleteSetResult.SessionFinished).progress.completedExerciseIndices)
    }

    @Test
    fun `ctaLabel reflects progress`() {
        assertEquals("BEGIN SESSION", SessionLogic.ctaLabel(completedCount = 0, totalExercises = 5))
        assertEquals("CONTINUE SESSION", SessionLogic.ctaLabel(completedCount = 2, totalExercises = 5))
        assertEquals("SESSION COMPLETE", SessionLogic.ctaLabel(completedCount = 5, totalExercises = 5))
    }

    @Test
    fun `logLabel distinguishes set, exercise, and session completion`() {
        assertEquals("FINISH SET", SessionLogic.logLabel(currentSet = 1, totalSetsForCurrentExercise = 4, hasAnotherIncompleteExercise = true))
        assertEquals(
            "FINISH EXERCISE",
            SessionLogic.logLabel(currentSet = 4, totalSetsForCurrentExercise = 4, hasAnotherIncompleteExercise = true),
        )
        assertEquals(
            "FINISH SESSION",
            SessionLogic.logLabel(currentSet = 4, totalSetsForCurrentExercise = 4, hasAnotherIncompleteExercise = false),
        )
    }
}
