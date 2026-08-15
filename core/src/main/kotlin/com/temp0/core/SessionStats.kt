package com.temp0.core

import java.time.DayOfWeek
import java.time.LocalDate

/** A completed workout, as needed to derive Progress-tab stats. [exerciseKeys] is a
 *  snapshot of which exercises were performed (not a live reference to a routine), so
 *  editing/deleting a routine later can't retroactively change past stats. */
data class CompletedSessionRecord(
    val routineName: String,
    val completedAtEpochMillis: Long,
    val durationSeconds: Int,
    val exerciseKeys: List<String>,
)

enum class DayState { DONE, TODAY, UPCOMING, NONE }

data class WeekDayInfo(val date: LocalDate, val state: DayState)

/**
 * Real stat derivation for the Progress tab, computed from persisted session history —
 * replacing the prototype's hardcoded mock numbers (`stats.total = "24"`, hand-authored
 * `weekDays`) now that the app has real local persistence.
 */
object SessionStats {

    fun totalSessions(records: List<CompletedSessionRecord>): Int = records.size

    /** Consecutive-day streak ending today (if a session was already logged today) or
     *  yesterday (if today has no session yet, since the day isn't over). */
    fun dayStreak(sessionDates: Set<LocalDate>, today: LocalDate): Int {
        var streak = 0
        var day = if (today in sessionDates) today else today.minusDays(1)
        while (day in sessionDates) {
            streak++
            day = day.minusDays(1)
        }
        return streak
    }

    /** Monday-to-Sunday state for each day of the current week. Days before today with no
     *  logged session are [DayState.NONE] rather than the prototype's hardcoded "rest day"
     *  dash — this app has no rest-day scheduling concept, so it only distinguishes what it
     *  can honestly know: done, today (not yet done), upcoming, or nothing logged. */
    fun weekDayStates(today: LocalDate, sessionDates: Set<LocalDate>): List<WeekDayInfo> {
        val monday = today.with(DayOfWeek.MONDAY)
        return (0..6).map { offset ->
            val date = monday.plusDays(offset.toLong())
            val state = when {
                date.isAfter(today) -> DayState.UPCOMING
                date in sessionDates -> DayState.DONE
                date == today -> DayState.TODAY
                else -> DayState.NONE
            }
            WeekDayInfo(date, state)
        }
    }

    /** Aggregate muscle-group patches for every exercise performed in the last 7 days
     *  (inclusive of today), for the Progress tab's "Muscle Balance · 7 Days" mannequin. */
    fun muscleBalanceLast7Days(records: List<CompletedSessionRecord>, today: LocalDate, zone: java.time.ZoneId): List<MannequinPatch> {
        val cutoff = today.minusDays(6)
        val exercises = records
            .asSequence()
            .filter { record ->
                val date = java.time.Instant.ofEpochMilli(record.completedAtEpochMillis).atZone(zone).toLocalDate()
                !date.isBefore(cutoff) && !date.isAfter(today)
            }
            .flatMap { it.exerciseKeys.asSequence() }
            .distinct()
            .mapNotNull { ExerciseLibrary.byKeyOrNull(it) }
            .toList()
        return MuscleAggregation.aggregate(exercises)
    }

    /** Most recent sessions first, for the "Recent Sessions" list. */
    fun recentSessions(records: List<CompletedSessionRecord>, limit: Int = 10): List<CompletedSessionRecord> =
        records.sortedByDescending { it.completedAtEpochMillis }.take(limit)
}
