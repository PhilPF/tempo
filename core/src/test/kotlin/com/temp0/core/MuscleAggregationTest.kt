package com.temp0.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MuscleAggregationTest {

    @Test
    fun `aggregate lights up every primary and secondary muscle across the routine`() {
        val pushDay = RoutineSeeds.all.first { it.id == "push" }.items.map { ExerciseLibrary.byKey(it.exerciseKey) }

        val patches = MuscleAggregation.aggregate(pushDay)
        val groups = patches.map { it.group }.toSet()

        // Push Day = Bench Press, Incline DB Press, Overhead Press, Lateral Raise, Triceps Pushdown
        assertEquals(setOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS), groups)
    }

    @Test
    fun `primary overrides secondary when the same muscle appears as both across exercises`() {
        // Triceps is secondary for Bench Press but primary for Triceps Pushdown.
        val exercises = listOf(ExerciseLibrary.byKey("bench_press"), ExerciseLibrary.byKey("triceps_pushdown"))

        val patches = MuscleAggregation.aggregate(exercises)
        val triceps = patches.filter { it.group == MuscleGroup.TRICEPS }

        assertTrue(triceps.isNotEmpty())
        assertTrue(triceps.all { it.level == MuscleLevel.PRIMARY })
    }

    @Test
    fun `aggregate dedupes a muscle group that is secondary in multiple exercises`() {
        // Shoulders is secondary for both Bench Press and Pull-Up.
        val exercises = listOf(ExerciseLibrary.byKey("bench_press"), ExerciseLibrary.byKey("pull_up"))

        val patches = MuscleAggregation.aggregate(exercises)
        val shoulderPatchCount = patches.count { it.group == MuscleGroup.SHOULDERS }

        // Shoulders has exactly 2 patch specs in MusclePatches — dedup means we see those
        // 2 once, not once per contributing exercise (which would be 4).
        assertEquals(MusclePatches.table[MuscleGroup.SHOULDERS]!!.size, shoulderPatchCount)
    }

    @Test
    fun `forSingle concatenates primary then secondary without deduping`() {
        val benchPress = ExerciseLibrary.byKey("bench_press") // primary: chest, secondary: shoulders+triceps

        val patches = MuscleAggregation.forSingle(benchPress)

        assertTrue(patches.filter { it.group == MuscleGroup.CHEST }.all { it.level == MuscleLevel.PRIMARY })
        assertTrue(patches.filter { it.group == MuscleGroup.SHOULDERS }.all { it.level == MuscleLevel.SECONDARY })
        assertTrue(patches.filter { it.group == MuscleGroup.TRICEPS }.all { it.level == MuscleLevel.SECONDARY })
    }
}
