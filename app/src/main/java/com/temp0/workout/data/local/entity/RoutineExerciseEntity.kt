package com.temp0.workout.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** One exercise inside a routine. [exerciseKey] references [com.temp0.core.ExerciseLibrary]
 *  (the fixed, compile-time catalog), not another table — the catalog isn't persisted. */
@Entity(
    tableName = "routine_exercises",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("routineId")],
)
data class RoutineExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routineId: String,
    val exerciseKey: String,
    val orderIndex: Int,
    val sets: Int,
    val reps: Int,
    val weight: Float?,
)
