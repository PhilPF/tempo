package com.temp0.core

/**
 * The muscle-group taxonomy used throughout the app. Intentionally small and fixed — it's
 * tied 1:1 to the mannequin's patch geometry in [MusclePatches], so adding a group means
 * adding new patch geometry too, not just a new enum entry.
 */
enum class MuscleGroup {
    CHEST,
    SHOULDERS,
    TRICEPS,
    ABS,
    QUADS,
    CALVES,
}
