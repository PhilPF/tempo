package com.temp0.workout.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont

/**
 * Alternative font-resolution path via the Downloadable Fonts API (Play Services' Font
 * Provider), documented here but **not used by default** — [USE_DOWNLOADABLE_FONTS] in
 * Type.kt is false, so [LibreCaslonText]/[JetBrainsMono] in Type.kt resolve from the
 * bundled .ttf files in res/font instead. This path needs Play Services to be present and
 * authenticated on the device, which isn't guaranteed on arbitrary emulators/CI, so it's
 * kept as an opt-in rather than the default.
 *
 * To switch: flip `USE_DOWNLOADABLE_FONTS = true` in Type.kt and replace the `LibreCaslonText`
 * / `JetBrainsMono` FontFamily declarations there with the ones built here.
 *
 * Requires a `certificates` resource array declaring Google's font-provider signing
 * certificates — see the Downloadable Fonts API docs for the standard
 * `res/values/font_certs.xml` snippet (omitted here since this path is unused by default).
 */
object GoogleFontsFallback {
    private val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = emptyList(), // fill in with the standard font-provider cert array if enabling this path
    )

    fun libreCaslonText(): FontFamily {
        val name = GoogleFont("Libre Caslon Text")
        return FontFamily(
            Font(googleFont = name, fontProvider = provider, weight = FontWeight.Normal, style = FontStyle.Normal),
            Font(googleFont = name, fontProvider = provider, weight = FontWeight.Bold, style = FontStyle.Normal),
            Font(googleFont = name, fontProvider = provider, weight = FontWeight.Normal, style = FontStyle.Italic),
        )
    }

    fun jetBrainsMono(): FontFamily {
        val name = GoogleFont("JetBrains Mono")
        return FontFamily(
            Font(googleFont = name, fontProvider = provider, weight = FontWeight.Normal, style = FontStyle.Normal),
            Font(googleFont = name, fontProvider = provider, weight = FontWeight.Bold, style = FontStyle.Normal),
        )
    }
}
