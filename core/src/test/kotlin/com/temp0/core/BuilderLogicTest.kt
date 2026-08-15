package com.temp0.core

import org.junit.Assert.assertEquals
import org.junit.Test

class BuilderLogicTest {

    @Test
    fun `toggleExercise appends a new key at the end`() {
        val order = listOf("bench_press", "push_up")

        val result = BuilderLogic.toggleExercise(order, "plank")

        assertEquals(listOf("bench_press", "push_up", "plank"), result)
    }

    @Test
    fun `toggleExercise removes an existing key`() {
        val order = listOf("bench_press", "push_up", "plank")

        val result = BuilderLogic.toggleExercise(order, "push_up")

        assertEquals(listOf("bench_press", "plank"), result)
    }

    @Test
    fun `re-adding after removal goes to the end, not the old position`() {
        val order = listOf("bench_press", "push_up", "plank")
        val afterRemove = BuilderLogic.toggleExercise(order, "bench_press")

        val afterReadd = BuilderLogic.toggleExercise(afterRemove, "bench_press")

        assertEquals(listOf("push_up", "plank", "bench_press"), afterReadd)
    }

    @Test
    fun `reorder moves an item and shifts the rest`() {
        val order = listOf("a", "b", "c", "d")

        val result = BuilderLogic.reorder(order, from = 0, to = 2)

        assertEquals(listOf("b", "c", "a", "d"), result)
    }

    @Test
    fun `reorder is a no-op for equal indices`() {
        val order = listOf("a", "b", "c")

        assertEquals(order, BuilderLogic.reorder(order, 1, 1))
    }

    @Test
    fun `adjust never drops below the minimum`() {
        assertEquals(1, BuilderLogic.adjust(current = 1, delta = -1, min = 1))
        assertEquals(5, BuilderLogic.adjust(current = 6, delta = -1))
    }

    @Test
    fun `parseWeight clamps invalid or negative input to zero`() {
        assertEquals(0f, BuilderLogic.parseWeight("abc"))
        assertEquals(0f, BuilderLogic.parseWeight("-10"))
        assertEquals(42.5f, BuilderLogic.parseWeight("42.5"))
    }

    @Test
    fun `setsRepsLabel omits weight when absent and formats whole numbers without a decimal`() {
        assertEquals("4×8", setsRepsLabel(4, 8, weight = null, units = Units.KG))
        assertEquals("4×8 · 60kg", setsRepsLabel(4, 8, weight = 60f, units = Units.KG))
        assertEquals("3×10 · 22.5lb", setsRepsLabel(3, 10, weight = 22.5f, units = Units.LB))
    }
}
