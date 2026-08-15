package com.temp0.core

import java.time.LocalDate
import java.time.ZoneOffset
import org.junit.Assert.assertEquals
import org.junit.Test

class SessionStatsTest {

    private val zone = ZoneOffset.UTC
    private fun epochMillisFor(date: LocalDate) = date.atStartOfDay(zone).toInstant().toEpochMilli()

    @Test
    fun `dayStreak counts consecutive days ending today when today has a session`() {
        val today = LocalDate.of(2026, 8, 15) // Saturday
        val sessionDates = setOf(today, today.minusDays(1), today.minusDays(2), today.minusDays(5))

        assertEquals(3, SessionStats.dayStreak(sessionDates, today))
    }

    @Test
    fun `dayStreak counts back from yesterday when today has no session yet`() {
        val today = LocalDate.of(2026, 8, 15)
        val sessionDates = setOf(today.minusDays(1), today.minusDays(2))

        assertEquals(2, SessionStats.dayStreak(sessionDates, today))
    }

    @Test
    fun `dayStreak is zero with a gap immediately before today`() {
        val today = LocalDate.of(2026, 8, 15)
        val sessionDates = setOf(today.minusDays(2))

        assertEquals(0, SessionStats.dayStreak(sessionDates, today))
    }

    @Test
    fun `weekDayStates marks future days upcoming and past no-session days as none`() {
        val today = LocalDate.of(2026, 8, 13) // Thursday
        val monday = today.with(java.time.DayOfWeek.MONDAY)
        val sessionDates = setOf(monday, monday.plusDays(1)) // Mon + Tue done

        val week = SessionStats.weekDayStates(today, sessionDates)

        assertEquals(DayState.DONE, week[0].state) // Mon
        assertEquals(DayState.DONE, week[1].state) // Tue
        assertEquals(DayState.NONE, week[2].state) // Wed - past, no session
        assertEquals(DayState.TODAY, week[3].state) // Thu - today, no session yet
        assertEquals(DayState.UPCOMING, week[4].state) // Fri
        assertEquals(DayState.UPCOMING, week[6].state) // Sun
    }

    @Test
    fun `muscleBalanceLast7Days only counts sessions within the trailing window`() {
        val today = LocalDate.of(2026, 8, 15)
        val records = listOf(
            CompletedSessionRecord("Push Day", epochMillisFor(today.minusDays(2)), 2400, listOf("bench_press")),
            CompletedSessionRecord("Leg Day", epochMillisFor(today.minusDays(10)), 2400, listOf("back_squat")),
        )

        val patches = SessionStats.muscleBalanceLast7Days(records, today, zone)
        val groups = patches.map { it.group }.toSet()

        assertEquals(setOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS), groups)
    }

    @Test
    fun `recentSessions sorts newest first and respects the limit`() {
        val today = LocalDate.of(2026, 8, 15)
        val records = listOf(
            CompletedSessionRecord("A", epochMillisFor(today.minusDays(5)), 100, emptyList()),
            CompletedSessionRecord("B", epochMillisFor(today), 100, emptyList()),
            CompletedSessionRecord("C", epochMillisFor(today.minusDays(2)), 100, emptyList()),
        )

        val result = SessionStats.recentSessions(records, limit = 2)

        assertEquals(listOf("B", "C"), result.map { it.routineName })
    }
}
