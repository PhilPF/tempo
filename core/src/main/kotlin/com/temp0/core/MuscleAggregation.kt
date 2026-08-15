package com.temp0.core

/** Whether a glow patch should render at full accent brightness (primary muscle for this
 *  exercise/routine) or the dim accent (secondary-only). */
enum class MuscleLevel { PRIMARY, SECONDARY }

/** One resolved, ready-to-draw glow patch: which body piece, its geometry, and its level. */
data class MannequinPatch(
    val group: MuscleGroup,
    val spec: PatchSpec,
    val level: MuscleLevel,
)

/**
 * Ported from the prototype's `aggregatePatches`/`patchesFor` (REGIMEN.dc.html). Two
 * distinct behaviors, preserved exactly:
 *  - [aggregate] combines *multiple* exercises (Home, Progress, Routines list, Builder
 *    preview): each muscle group is deduped, and if it's primary for at least one
 *    exercise in the set it renders PRIMARY even if it's secondary for another.
 *  - [forSingle] is for exactly one exercise (the Exercise screen): primary and secondary
 *    patches are simply concatenated, with no deduping — matching the prototype's
 *    `patchesFor`, which never collides in practice since no single library exercise lists
 *    the same muscle group in both its primary and secondary lists.
 */
object MuscleAggregation {

    fun aggregate(exercises: List<ExerciseDef>): List<MannequinPatch> {
        val levelByGroup = LinkedHashMap<MuscleGroup, MuscleLevel>()
        exercises.forEach { ex ->
            ex.secondary.forEach { group -> levelByGroup.putIfAbsent(group, MuscleLevel.SECONDARY) }
        }
        exercises.forEach { ex ->
            ex.primary.forEach { group -> levelByGroup[group] = MuscleLevel.PRIMARY }
        }
        return levelByGroup.entries.flatMap { (group, level) ->
            (MusclePatches.table[group] ?: emptyList()).map { spec -> MannequinPatch(group, spec, level) }
        }
    }

    fun forSingle(exercise: ExerciseDef): List<MannequinPatch> {
        val primaryPatches = exercise.primary.flatMap { group ->
            (MusclePatches.table[group] ?: emptyList()).map { spec -> MannequinPatch(group, spec, MuscleLevel.PRIMARY) }
        }
        val secondaryPatches = exercise.secondary.flatMap { group ->
            (MusclePatches.table[group] ?: emptyList()).map { spec -> MannequinPatch(group, spec, MuscleLevel.SECONDARY) }
        }
        return primaryPatches + secondaryPatches
    }
}
