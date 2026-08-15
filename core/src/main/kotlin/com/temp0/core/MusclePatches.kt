package com.temp0.core

/**
 * One glow-patch's geometry, in the mannequin's 200×268 reference box (matching the
 * design's px values verbatim). Callers scale [top]/[left]/[width]/[height] by their own
 * rendered size divided by 200/268.
 */
data class PatchSpec(
    val top: Float,
    val left: Float,
    val width: Float,
    val height: Float,
    val cornerRadius: Float,
    val isCircle: Boolean = false,
    val rotationDeg: Float = 0f,
)

/**
 * Ported verbatim from the prototype's `MUSCLE_PATCHES` table (REGIMEN.dc.html). Every
 * top/left/width/height/radius/rotation value below is copied from that source, not
 * re-derived — this is the canonical body-patch layout for the mannequin.
 */
object MusclePatches {
    val table: Map<MuscleGroup, List<PatchSpec>> = mapOf(
        MuscleGroup.CHEST to listOf(
            PatchSpec(top = 50f, left = 65f, width = 70f, height = 34f, cornerRadius = 14f),
        ),
        MuscleGroup.SHOULDERS to listOf(
            PatchSpec(top = 44f, left = 36f, width = 26f, height = 26f, cornerRadius = 13f, isCircle = true),
            PatchSpec(top = 44f, left = 138f, width = 26f, height = 26f, cornerRadius = 13f, isCircle = true),
        ),
        MuscleGroup.TRICEPS to listOf(
            PatchSpec(top = 54f, left = 36f, width = 20f, height = 50f, cornerRadius = 10f, rotationDeg = -4f),
            PatchSpec(top = 54f, left = 144f, width = 20f, height = 50f, cornerRadius = 10f, rotationDeg = 4f),
        ),
        MuscleGroup.ABS to listOf(
            PatchSpec(top = 100f, left = 72f, width = 56f, height = 46f, cornerRadius = 14f),
        ),
        MuscleGroup.QUADS to listOf(
            PatchSpec(top = 134f, left = 70f, width = 24f, height = 60f, cornerRadius = 12f, rotationDeg = -2f),
            PatchSpec(top = 134f, left = 106f, width = 24f, height = 60f, cornerRadius = 12f, rotationDeg = 2f),
        ),
        MuscleGroup.CALVES to listOf(
            PatchSpec(top = 196f, left = 73f, width = 20f, height = 56f, cornerRadius = 10f, rotationDeg = -2f),
            PatchSpec(top = 196f, left = 107f, width = 20f, height = 56f, cornerRadius = 10f, rotationDeg = 2f),
        ),
    )
}
