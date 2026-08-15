package com.temp0.workout.ui

import com.temp0.workout.domain.BuilderDraft

/** In-memory-only state that isn't persisted: which exercise/set the active session is on,
 *  the rest timer, and the Builder's in-progress draft. Deliberately does *not* include
 *  which screen is showing — that's [androidx.navigation.NavController]'s job now (see the
 *  plan's switch to Navigation-Compose), not the ViewModel's. */
data class TransientState(
    val selectedExerciseIndex: Int = 0,
    val currentSet: Int = 1,
    val completedExerciseIndices: Set<Int> = emptySet(),
    val restActive: Boolean = false,
    val restRemainingSeconds: Int = 0,
    val sessionStartedAt: Long? = null,
    val exerciseWeightInputs: Map<String, String> = emptyMap(),
    val builder: BuilderDraft = BuilderDraft(),
)
