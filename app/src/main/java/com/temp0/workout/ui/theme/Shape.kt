package com.temp0.workout.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Corner radii, copied verbatim from the CSS `border-radius` values in REGIMEN.dc.html. */
object Temp0Radius {
    val xs: Dp = 6.dp
    val sm: Dp = 8.dp
    val smMed: Dp = 10.dp
    val md: Dp = 12.dp
    val mdAlt: Dp = 13.dp
    val lg: Dp = 14.dp
    val xl: Dp = 16.dp
    val xxl: Dp = 18.dp
    val xxxl: Dp = 24.dp
    val huge: Dp = 28.dp

    /** Maximum/stadium rounding — used only by the floating bottom-nav pill, per the
     *  user's explicit design direction (a deliberate departure from the prototype's
     *  squared-off bar). */
    val pill: Dp = 999.dp
}
