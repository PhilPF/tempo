package com.temp0.workout.domain

import com.temp0.core.ExerciseDef

/** One exercise inside a routine, with its [ExerciseDef] already resolved from the fixed
 *  catalog — everything a screen needs (name, muscles, weighted flag) plus this routine's
 *  configured sets/reps/weight. */
data class RoutineExerciseItem(
    val exercise: ExerciseDef,
    val sets: Int,
    val reps: Int,
    val weight: Float?,
)

data class Routine(
    val id: String,
    val name: String,
    val items: List<RoutineExerciseItem>,
)
