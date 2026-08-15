package com.temp0.workout.domain

/** Per-exercise sets/reps/weight while it's being configured in the Builder screen. */
data class BuilderExerciseConfig(
    val sets: Int = 3,
    val reps: Int = 10,
    val weight: Float = 20f,
)

/** The Routine Builder's in-progress draft — order is the single ordered list of picked
 *  exercise keys (see [com.temp0.core.BuilderLogic] for why one list is enough). */
data class BuilderDraft(
    val name: String = "",
    val search: String = "",
    val order: List<String> = emptyList(),
    val config: Map<String, BuilderExerciseConfig> = emptyMap(),
    val editingRoutineId: String? = null,
) {
    val isEditing: Boolean get() = editingRoutineId != null

    companion object {
        fun fromRoutine(routine: Routine): BuilderDraft {
            val order = routine.items.map { it.exercise.key }
            val config = routine.items.associate { item ->
                item.exercise.key to BuilderExerciseConfig(sets = item.sets, reps = item.reps, weight = item.weight ?: 20f)
            }
            return BuilderDraft(name = routine.name, order = order, config = config, editingRoutineId = routine.id)
        }
    }
}
