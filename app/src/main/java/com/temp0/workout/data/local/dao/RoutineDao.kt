package com.temp0.workout.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.temp0.workout.data.local.entity.RoutineEntity
import com.temp0.workout.data.local.entity.RoutineExerciseEntity
import com.temp0.workout.data.local.entity.RoutineWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {

    @Transaction
    @Query("SELECT * FROM routines ORDER BY createdAt ASC")
    fun observeRoutinesWithItems(): Flow<List<RoutineWithItems>>

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :routineId LIMIT 1")
    suspend fun getRoutineWithItems(routineId: String): RoutineWithItems?

    @Query("SELECT COUNT(*) FROM routines")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRoutine(routine: RoutineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<RoutineExerciseEntity>)

    @Query("DELETE FROM routine_exercises WHERE routineId = :routineId")
    suspend fun deleteItemsForRoutine(routineId: String)

    @Delete
    suspend fun deleteRoutine(routine: RoutineEntity)

    /** Replace a routine's full item list atomically (used by both create and edit-in-place
     *  saves from the Builder screen). */
    @Transaction
    suspend fun replaceItems(routineId: String, items: List<RoutineExerciseEntity>) {
        deleteItemsForRoutine(routineId)
        insertItems(items)
    }

    /** Insert a routine and its items in one transaction — used by first-run seeding and by
     *  the Builder's "save as new routine" path. */
    @Transaction
    suspend fun insertRoutineWithItems(routine: RoutineEntity, items: List<RoutineExerciseEntity>) {
        upsertRoutine(routine)
        insertItems(items)
    }
}
