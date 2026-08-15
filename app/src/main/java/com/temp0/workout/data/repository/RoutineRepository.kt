package com.temp0.workout.data.repository

import com.temp0.core.ExerciseLibrary
import com.temp0.core.RoutineSeeds
import com.temp0.workout.data.local.dao.RoutineDao
import com.temp0.workout.data.local.entity.RoutineEntity
import com.temp0.workout.data.local.entity.RoutineExerciseEntity
import com.temp0.workout.data.local.entity.RoutineWithItems
import com.temp0.workout.domain.BuilderDraft
import com.temp0.workout.domain.Routine
import com.temp0.workout.domain.RoutineExerciseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoutineRepository(private val dao: RoutineDao) {

    val routinesFlow: Flow<List<Routine>> = dao.observeRoutinesWithItems().map { list -> list.map { it.toDomain() } }

    suspend fun seedIfEmpty() {
        if (dao.count() > 0) return
        RoutineSeeds.all.forEach { seed ->
            val items = seed.items.mapIndexed { index, item ->
                RoutineExerciseEntity(
                    routineId = seed.id,
                    exerciseKey = item.exerciseKey,
                    orderIndex = index,
                    sets = item.sets,
                    reps = item.reps,
                    weight = item.weight,
                )
            }
            dao.insertRoutineWithItems(RoutineEntity(id = seed.id, name = seed.name, createdAt = System.currentTimeMillis()), items)
        }
    }

    /** Create-vs-edit-in-place, exactly mirroring the prototype's `builderSave`: with an
     *  [BuilderDraft.editingRoutineId] the existing routine's items are replaced and its
     *  `createdAt` preserved; otherwise a new routine is created (and its id returned so the
     *  caller can make it the active routine). */
    suspend fun saveRoutine(draft: BuilderDraft): String {
        require(draft.order.isNotEmpty()) { "Cannot save a routine with no exercises picked" }

        val name = draft.name.trim().ifBlank { "Untitled Routine" }
        val routineId = draft.editingRoutineId ?: "r${System.currentTimeMillis()}"
        val createdAt = draft.editingRoutineId
            ?.let { dao.getRoutineWithItems(it)?.routine?.createdAt }
            ?: System.currentTimeMillis()

        val items = draft.order.mapIndexed { index, key ->
            val def = ExerciseLibrary.byKey(key)
            val cfg = draft.config[key] ?: com.temp0.workout.domain.BuilderExerciseConfig()
            RoutineExerciseEntity(
                routineId = routineId,
                exerciseKey = key,
                orderIndex = index,
                sets = cfg.sets,
                reps = cfg.reps,
                weight = if (def.weighted) cfg.weight else null,
            )
        }

        dao.upsertRoutine(RoutineEntity(id = routineId, name = name, createdAt = createdAt))
        dao.replaceItems(routineId, items)
        return routineId
    }

    private fun RoutineWithItems.toDomain(): Routine = Routine(
        id = routine.id,
        name = routine.name,
        items = items.sortedBy { it.orderIndex }.mapNotNull { entity ->
            ExerciseLibrary.byKeyOrNull(entity.exerciseKey)?.let { def ->
                RoutineExerciseItem(exercise = def, sets = entity.sets, reps = entity.reps, weight = entity.weight)
            }
        },
    )
}
