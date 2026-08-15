package com.temp0.workout.data.repository

import com.temp0.core.CompletedSessionRecord
import com.temp0.core.RoutineSeeds
import com.temp0.workout.data.local.dao.SessionDao
import com.temp0.workout.data.local.entity.SessionEntity
import com.temp0.workout.data.local.entity.SessionWithExercises
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepository(private val dao: SessionDao) {

    val recordsFlow: Flow<List<CompletedSessionRecord>> = dao.observeSessionsWithExercises().map { list -> list.map { it.toDomain() } }

    suspend fun recordCompletedSession(
        routineId: String?,
        routineName: String,
        startedAt: Long,
        completedAt: Long,
        exerciseCompletions: List<Pair<String, Int>>,
    ) {
        dao.recordSession(
            session = SessionEntity(
                routineId = routineId,
                routineName = routineName,
                startedAt = startedAt,
                completedAt = completedAt,
                durationSeconds = ((completedAt - startedAt) / 1000).toInt().coerceAtLeast(0),
            ),
            exerciseKeys = exerciseCompletions,
        )
    }

    /** Seeds a few realistic-looking demo sessions on first launch (dates relative to
     *  "now", not hardcoded, so they never look stale) so Progress isn't a bare zero-state
     *  on a fresh install — an explicit product choice, not the prototype's own behavior. */
    suspend fun seedDemoSessionsIfEmpty() {
        if (dao.count() > 0) return
        val now = System.currentTimeMillis()
        val day = TimeUnit.DAYS.toMillis(1)
        val demoSessions = listOf(
            Triple("push", "Push Day", now - 2 * day),
            Triple("legs", "Leg Day", now - 4 * day),
            Triple("pull", "Pull Day", now - 6 * day),
        )
        demoSessions.forEach { (routineId, name, completedAt) ->
            val seed = RoutineSeeds.all.first { it.id == routineId }
            recordCompletedSession(
                routineId = routineId,
                routineName = name,
                startedAt = completedAt - TimeUnit.MINUTES.toMillis(45),
                completedAt = completedAt,
                exerciseCompletions = seed.items.map { it.exerciseKey to it.sets },
            )
        }
    }

    private fun SessionWithExercises.toDomain(): CompletedSessionRecord = CompletedSessionRecord(
        routineName = session.routineName,
        completedAtEpochMillis = session.completedAt,
        durationSeconds = session.durationSeconds,
        exerciseKeys = exercises.map { it.exerciseKey },
    )
}
