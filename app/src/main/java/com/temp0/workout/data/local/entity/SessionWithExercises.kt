package com.temp0.workout.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SessionWithExercises(
    @Embedded val session: SessionEntity,
    @Relation(parentColumn = "id", entityColumn = "sessionId")
    val exercises: List<SessionExerciseEntity>,
)
