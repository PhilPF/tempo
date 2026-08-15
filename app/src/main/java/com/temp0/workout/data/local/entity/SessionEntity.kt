package com.temp0.workout.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** A completed workout. [routineName] is a snapshot (not a live join) so renaming or
 *  deleting the routine later can't change past history. */
@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routineId: String?,
    val routineName: String,
    val startedAt: Long,
    val completedAt: Long,
    val durationSeconds: Int,
)
