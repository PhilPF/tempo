package com.temp0.core

/** A single exercise in the (fixed, compile-time) exercise catalog. [key] is the stable
 *  identifier used as a foreign key from routines/session history, so it survives the
 *  catalog being reordered or extended later. */
data class ExerciseDef(
    val key: String,
    val name: String,
    val primary: List<MuscleGroup>,
    val secondary: List<MuscleGroup>,
    val weighted: Boolean,
)

/**
 * Ported from the prototype's `LIBRARY` constant (REGIMEN.dc.html). Same 10 exercises,
 * same primary/secondary muscle tagging, same weighted/bodyweight flag.
 */
object ExerciseLibrary {
    val all: List<ExerciseDef> = listOf(
        ExerciseDef(
            key = "bench_press",
            name = "Barbell Bench Press",
            primary = listOf(MuscleGroup.CHEST),
            secondary = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
            weighted = true,
        ),
        ExerciseDef(
            key = "push_up",
            name = "Push-Up",
            primary = listOf(MuscleGroup.CHEST),
            secondary = listOf(MuscleGroup.TRICEPS),
            weighted = false,
        ),
        ExerciseDef(
            key = "incline_db_press",
            name = "Incline Dumbbell Press",
            primary = listOf(MuscleGroup.CHEST),
            secondary = listOf(MuscleGroup.SHOULDERS),
            weighted = true,
        ),
        ExerciseDef(
            key = "overhead_press",
            name = "Overhead Press",
            primary = listOf(MuscleGroup.SHOULDERS),
            secondary = listOf(MuscleGroup.TRICEPS),
            weighted = true,
        ),
        ExerciseDef(
            key = "lateral_raise",
            name = "Lateral Raise",
            primary = listOf(MuscleGroup.SHOULDERS),
            secondary = emptyList(),
            weighted = true,
        ),
        ExerciseDef(
            key = "triceps_pushdown",
            name = "Triceps Pushdown",
            primary = listOf(MuscleGroup.TRICEPS),
            secondary = emptyList(),
            weighted = true,
        ),
        ExerciseDef(
            key = "pull_up",
            name = "Pull-Up",
            primary = listOf(MuscleGroup.SHOULDERS),
            secondary = listOf(MuscleGroup.TRICEPS),
            weighted = false,
        ),
        ExerciseDef(
            key = "plank",
            name = "Plank",
            primary = listOf(MuscleGroup.ABS),
            secondary = emptyList(),
            weighted = false,
        ),
        ExerciseDef(
            key = "back_squat",
            name = "Back Squat",
            primary = listOf(MuscleGroup.QUADS),
            secondary = listOf(MuscleGroup.ABS),
            weighted = true,
        ),
        ExerciseDef(
            key = "calf_raise",
            name = "Standing Calf Raise",
            primary = listOf(MuscleGroup.CALVES),
            secondary = emptyList(),
            weighted = true,
        ),
    )

    private val byKey: Map<String, ExerciseDef> = all.associateBy { it.key }

    fun byKey(key: String): ExerciseDef =
        byKeyOrNull(key) ?: error("Unknown exercise key: $key")

    fun byKeyOrNull(key: String): ExerciseDef? = byKey[key]
}
