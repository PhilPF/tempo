package com.temp0.workout.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RoutineWithItems(
    @Embedded val routine: RoutineEntity,
    @Relation(parentColumn = "id", entityColumn = "routineId")
    val items: List<RoutineExerciseEntity>,
)
