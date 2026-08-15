package com.temp0.workout.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * The prototype's palette is expressed entirely in OKLCH (e.g. `oklch(0.68 0.14 250)`).
 * Compose has no OKLCH color type, so every token below is the one-time, offline-converted
 * sRGB equivalent (Björn Ottosson's OKLab↔linear-sRGB matrices + sRGB gamma companding) —
 * no runtime color-space math. Each constant's comment records the source OKLCH value it
 * was converted from, so the palette can be re-derived or adjusted later without redoing
 * the conversion from scratch.
 */
object Temp0Palette {
    // Backgrounds
    val AppBg = Color(0xFF090E12) // oklch(0.16 0.012 250) — phone/app background
    val BottomNavBg = Color(0xFF060A0E) // oklch(0.14 0.012 250)

    // Accent — the single accent color used throughout, plus the dim "secondary muscle" variant.
    // Every translucent border/fill in the app is this same Accent hue at a different alpha,
    // not a separate color — see GlassPanel.
    val Accent = Color(0xFF4C9DEB) // oklch(0.68 0.14 250)
    val AccentDim = Color(0xD9436685) // oklch(0.5 0.07 250 / 0.85) — secondary-muscle glow
    val AccentDateLabel = Color(0xFF3A84CA) // oklch(0.6 0.13 250) — small uppercase date/caption accents
    val AccentChevron = Color(0xFF1666AA) // oklch(0.5 0.13 250) — list-row chevrons
    val AccentBadgeText = Color(0xFF89D4FF) // oklch(0.85 0.13 250) — bright badge digits (rest countdown, sets×reps)

    // Neutral text ramp, brightest to dimmest (all oklch(L 0.01 250))
    val Text95 = Color(0xFFEAEFF5) // headline text
    val Text92 = Color(0xFFE0E5EB) // primary body text
    val Text90 = Color(0xFFD9DFE5)
    val Text85 = Color(0xFFC9CED4)
    val Text60 = Color(0xFF7C8186) // secondary/duration text
    val Text55 = Color(0xFF6D7277) // captions
    val Text50 = Color(0xFF5F6469) // dim captions, chevrons
    val Text45 = Color(0xFF51565B) // placeholders
    val Text40 = Color(0xFF44484D) // dimmest — inactive nav labels, disabled borders

    // Structural
    val HairlineDivider = Color(0xFF1C2024) // oklch(0.24 0.01 250)
    val PhoneBorder = Color(0xFF25292E) // oklch(0.28 0.01 250)
    val BottomNavBorder = Color(0xFF202429) // oklch(0.26 0.01 250)
    val InactiveDot = Color(0xFF2A2E33) // oklch(0.3 0.01 250)
    val EditButtonBorder = Color(0xFF44484D) // oklch(0.4 0.01 250)
    val DragHandleDot = Color(0xFF9A9FA5) // oklch(0.7 0.01 250)

    // Mannequin
    val MannequinBase = Color(0xFF2B343D) // oklch(0.32 0.02 250) — base body silhouette
}
