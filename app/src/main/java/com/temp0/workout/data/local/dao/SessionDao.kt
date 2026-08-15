package com.temp0.workout.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.temp0.workout.data.local.entity.SessionEntity
import com.temp0.workout.data.local.entity.SessionExerciseEntity
import com.temp0.workout.data.local.entity.SessionWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Transaction
    @Query("SELECT * FROM sessions ORDER BY completedAt DESC")
    fun observeSessionsWithExercises(): Flow<List<SessionWithExercises>>

    @Query("SELECT COUNT(*) FROM sessions")
    suspend fun count(): Int

    @Insert
    suspend fun insertSession(session: SessionEntity): Long

    @Insert
    suspend fun insertExercises(exercises: List<SessionExerciseEntity>)

    @Transaction
    suspend fun recordSession(session: SessionEntity, exerciseKeys: List<Pair<String, Int>>): Long {
        val sessionId = insertSession(session)
        insertExercises(exerciseKeys.map { (key, setsCompleted) -> SessionExerciseEntity(sessionId = sessionId, exerciseKey = key, setsCompleted = setsCompleted) })
        return sessionId
    }
}
