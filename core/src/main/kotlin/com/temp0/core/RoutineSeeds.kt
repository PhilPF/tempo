package com.temp0.core

/** One exercise entry inside a routine: which exercise, how many sets/reps, and (for
 *  `weighted` exercises) a target weight in the user's configured unit. */
data class RoutineItem(
    val exerciseKey: String,
    val sets: Int,
    val reps: Int,
    val weight: Float? = null,
)

data class RoutineSeed(
    val id: String,
    val name: String,
    val items: List<RoutineItem>,
)

/**
 * Ported from the prototype's `ROUTINES_SEED` constant — used to populate the database on
 * first launch so the app isn't empty on install.
 */
object RoutineSeeds {
    val all: List<RoutineSeed> = listOf(
        RoutineSeed(
            id = "push",
            name = "Push Day",
            items = listOf(
                RoutineItem("bench_press", sets = 4, reps = 8, weight = 60f),
                RoutineItem("incline_db_press", sets = 3, reps = 10, weight = 22f),
                RoutineItem("overhead_press", sets = 4, reps = 8, weight = 40f),
                RoutineItem("lateral_raise", sets = 3, reps = 15, weight = 8f),
                RoutineItem("triceps_pushdown", sets = 3, reps = 12, weight = 25f),
            ),
        ),
        RoutineSeed(
            id = "pull",
            name = "Pull Day",
            items = listOf(
                RoutineItem("pull_up", sets = 4, reps = 8),
                RoutineItem("plank", sets = 3, reps = 30),
            ),
        ),
        RoutineSeed(
            id = "legs",
            name = "Leg Day",
            items = listOf(
                RoutineItem("back_squat", sets = 4, reps = 10, weight = 80f),
                RoutineItem("calf_raise", sets = 4, reps = 15, weight = 40f),
            ),
        ),
    )
}
