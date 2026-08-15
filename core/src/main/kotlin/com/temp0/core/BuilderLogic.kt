package com.temp0.core

import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Pure functions behind the Routine Builder screen — order/selection is a single ordered,
 * de-duplicated list of exercise keys (simpler than the prototype's parallel
 * `builderSelected`/`builderOrder` lists, since one list is enough to answer both "is this
 * picked" and "in what order" — membership check + list order do the same job).
 */
object BuilderLogic {

    /** Toggle [key] in [order]: removes it if present, appends it at the end if not — so
     *  re-adding an exercise after removing it goes to the end, not its old position
     *  (matches the prototype's `toggleLibrary`). */
    fun toggleExercise(order: List<String>, key: String): List<String> =
        if (key in order) order - key else order + key

    /** Move the item at [from] to [to], shifting the rest — a direct port of the
     *  prototype's `dropAt` splice logic. No-ops for an out-of-range or no-op move. */
    fun reorder(order: List<String>, from: Int, to: Int): List<String> {
        if (from == to || from !in order.indices || to !in order.indices) return order
        val mutable = order.toMutableList()
        val moved = mutable.removeAt(from)
        mutable.add(to, moved)
        return mutable
    }

    /** Sets/reps stepper adjustment — never drops below [min]. */
    fun adjust(current: Int, delta: Int, min: Int = 1): Int = max(min, current + delta)

    /** Weight input parsing — never negative, blank/invalid input clamps to 0. */
    fun parseWeight(raw: String): Float = max(0f, raw.trim().toFloatOrNull() ?: 0f)
}

/** "4×8" or "4×8 · 60kg" — the compact sets/reps(/weight) caption used on Home's exercise
 *  rows and the Exercise screen's badge. */
fun setsRepsLabel(sets: Int, reps: Int, weight: Float?, units: Units): String {
    val base = "$sets×$reps"
    if (weight == null || weight <= 0f) return base
    val weightText = if (weight == weight.roundToInt().toFloat()) {
        weight.roundToInt().toString()
    } else {
        weight.toString()
    }
    return "$base · $weightText${units.label}"
}
