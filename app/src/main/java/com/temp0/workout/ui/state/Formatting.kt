package com.temp0.workout.ui.state

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Presentation-only date/duration formatting — kept out of :core since it's a UI concern
 *  (locale, exact copy), not domain logic. */
object Formatting {
    private val homeDateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.US)
    private val sessionDateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d", Locale.US)

    fun homeDateLabel(today: LocalDate): String = today.format(homeDateFormatter)

    fun sessionDateLabel(epochMillis: Long, zone: ZoneId): String =
        java.time.Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate().format(sessionDateFormatter)

    fun durationLabel(seconds: Int): String {
        val minutes = (seconds / 60).coerceAtLeast(1)
        return "$minutes min"
    }

    fun weekDayGlyph(state: com.temp0.core.DayState): String = when (state) {
        com.temp0.core.DayState.DONE -> "✓"
        com.temp0.core.DayState.TODAY -> "●"
        com.temp0.core.DayState.NONE -> "–"
        com.temp0.core.DayState.UPCOMING -> ""
    }

    private val weekDayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
    fun weekDayLetter(index: Int): String = weekDayLabels[index]
}

/** Lowercase muscle-group label as used in the Builder's exercise-library captions
 *  ("chest · shoulders"), matching the prototype's own lowercase group ids. */
fun com.temp0.core.MuscleGroup.displayName(): String = name.lowercase(Locale.US)

