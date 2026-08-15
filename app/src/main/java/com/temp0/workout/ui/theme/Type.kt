package com.temp0.workout.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.temp0.workout.R

/**
 * Set to true to resolve fonts via the Downloadable Fonts API (Google Play Services'
 * Font Provider) instead of the bundled .ttf files below. Left off by default: bundled
 * fonts always work (no Play Services / network dependency at first render), which matters
 * for arbitrary emulators, CI, or devices without Play Services. See
 * [com.temp0.workout.ui.theme.GoogleFontsFallback] for the wiring if this is ever flipped.
 */
internal const val USE_DOWNLOADABLE_FONTS = false

/** 'Libre Caslon Text' — serif, used sparingly as an accent (per the design chat): the
 *  wordmark reference, headlines, exercise name, big numerals. Bundled from Google Fonts
 *  (OFL license, see assets/licenses/OFL_LibreCaslonText.txt). */
val LibreCaslonText = FontFamily(
    Font(R.font.libre_caslon_text_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.libre_caslon_text_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.libre_caslon_text_italic, FontWeight.Normal, FontStyle.Italic),
)

/** 'JetBrains Mono' — the app's workhorse UI font (final pick after the design chat tried
 *  and rejected Courier Prime): labels, body copy, buttons, badges, the TEMP0 wordmark.
 *  Bundled from Google Fonts (OFL license, see assets/licenses/OFL_JetBrainsMono.txt). */
val JetBrainsMono = FontFamily(
    Font(R.font.jetbrains_mono_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.jetbrains_mono_bold, FontWeight.Bold, FontStyle.Normal),
)

/** The type-scale roles actually used by the design (grep-verified against
 *  REGIMEN.dc.html's font-size/weight pairs) — not a generic Material scale. */
object Temp0Type {
    /** 32sp serif bold — Home routine name, "Progress", "Profile". Unified across all
     *  three per the design chat's explicit consistency fix. */
    val displayHeadline = TextStyle(fontFamily = LibreCaslonText, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 34.sp)

    /** 19sp serif regular — overlay screen titles (Your Routines / exercise name / New
     *  Routine, Edit Routine). */
    val screenTitle = TextStyle(fontFamily = LibreCaslonText, fontWeight = FontWeight.Normal, fontSize = 19.sp)

    /** 26sp serif regular, accent color — Progress stat-chip numerals. */
    val statNumeral = TextStyle(fontFamily = LibreCaslonText, fontWeight = FontWeight.Normal, fontSize = 26.sp)

    /** 24sp serif regular — Builder routine-name input. */
    val nameInput = TextStyle(fontFamily = LibreCaslonText, fontWeight = FontWeight.Normal, fontSize = 24.sp)

    /** 20sp serif regular — Profile avatar initials. */
    val avatarInitials = TextStyle(fontFamily = LibreCaslonText, fontWeight = FontWeight.Normal, fontSize = 20.sp)

    /** 13sp mono bold, letter-spaced — the TEMP0 wordmark. */
    val wordmark = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 3.sp)

    /** 15sp mono — list-row primary text (exercise names, routine names). */
    val bodyMono = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Normal, fontSize = 15.sp)

    /** 14sp mono — slightly smaller body text (session rows, preference values). */
    val bodyMonoSmall = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Normal, fontSize = 14.sp)

    /** 13sp mono bold — button labels. */
    val buttonLabel = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 1.sp)

    /** 11sp mono, letter-spaced uppercase — section captions ("EXERCISES", "PREFERENCES"). */
    val sectionCaption = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Normal, fontSize = 11.sp, letterSpacing = 2.sp)

    /** 10sp mono — smaller captions (sets caption under an exercise name, day labels). */
    val captionSmall = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Normal, fontSize = 10.sp, letterSpacing = 0.5.sp)

    /** 9sp mono, uppercase — the smallest labels (bottom nav items, "SETS"/"REPS" stepper labels). */
    val captionTiny = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Normal, fontSize = 9.sp, letterSpacing = 1.sp)

    /** Large mono numerals — rest countdown digits and the sets×reps badge, both called
     *  out in the design chat as needing to be bigger and share one aesthetic. */
    val badgeDigits = TextStyle(fontFamily = JetBrainsMono, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 1.em)
}
