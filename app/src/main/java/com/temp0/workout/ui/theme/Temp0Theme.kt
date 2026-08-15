package com.temp0.workout.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/** The app's actual (bespoke, flat, dark-only) palette — not really "Material" — exposed
 *  via a CompositionLocal so components read semantic tokens instead of raw [Temp0Palette]
 *  constants. */
data class Temp0Colors(
    val background: androidx.compose.ui.graphics.Color = Temp0Palette.AppBg,
    val bottomNavBackground: androidx.compose.ui.graphics.Color = Temp0Palette.BottomNavBg,
    val accent: androidx.compose.ui.graphics.Color = Temp0Palette.Accent,
    val accentDim: androidx.compose.ui.graphics.Color = Temp0Palette.AccentDim,
    val accentDateLabel: androidx.compose.ui.graphics.Color = Temp0Palette.AccentDateLabel,
    val accentChevron: androidx.compose.ui.graphics.Color = Temp0Palette.AccentChevron,
    val accentBadgeText: androidx.compose.ui.graphics.Color = Temp0Palette.AccentBadgeText,
    val textHeadline: androidx.compose.ui.graphics.Color = Temp0Palette.Text95,
    val textPrimary: androidx.compose.ui.graphics.Color = Temp0Palette.Text92,
    val textSecondary: androidx.compose.ui.graphics.Color = Temp0Palette.Text60,
    val textCaption: androidx.compose.ui.graphics.Color = Temp0Palette.Text55,
    val textDim: androidx.compose.ui.graphics.Color = Temp0Palette.Text50,
    val textPlaceholder: androidx.compose.ui.graphics.Color = Temp0Palette.Text45,
    val textDisabled: androidx.compose.ui.graphics.Color = Temp0Palette.Text40,
    val divider: androidx.compose.ui.graphics.Color = Temp0Palette.HairlineDivider,
    val border: androidx.compose.ui.graphics.Color = Temp0Palette.PhoneBorder,
    val inactiveDot: androidx.compose.ui.graphics.Color = Temp0Palette.InactiveDot,
    val mannequinBase: androidx.compose.ui.graphics.Color = Temp0Palette.MannequinBase,
)

val LocalTemp0Colors = staticCompositionLocalOf { Temp0Colors() }

private val Temp0DarkColorScheme = darkColorScheme(
    primary = Temp0Palette.Accent,
    onPrimary = Temp0Palette.AppBg,
    background = Temp0Palette.AppBg,
    onBackground = Temp0Palette.Text92,
    surface = Temp0Palette.AppBg,
    onSurface = Temp0Palette.Text92,
    surfaceVariant = Temp0Palette.BottomNavBg,
    outline = Temp0Palette.HairlineDivider,
)

@Composable
fun Temp0Theme(content: @Composable () -> Unit) {
    // The app is dark-themed only by design (see the design chat: "I like wireframe or
    // blueprint like designs... dark themed") — there is no light variant, regardless of
    // the system setting.
    CompositionLocalProvider(LocalTemp0Colors provides Temp0Colors()) {
        MaterialTheme(
            colorScheme = Temp0DarkColorScheme,
            typography = MaterialTheme.typography,
            content = content,
        )
    }
}
