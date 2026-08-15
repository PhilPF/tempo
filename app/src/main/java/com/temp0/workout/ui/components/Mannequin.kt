package com.temp0.workout.ui.components

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.temp0.core.MannequinPatch
import com.temp0.core.MuscleLevel
import com.temp0.workout.ui.theme.LocalTemp0Colors

/**
 * The shared, static, flat front-view mannequin silhouette reused across every screen that
 * shows one (Home, Progress, Routines list, Builder preview, Exercise). All geometry is
 * defined in a fixed 200×268 reference box — copied verbatim from the base-body `<div>`
 * positions in REGIMEN.dc.html — and scaled to whatever size the caller actually renders
 * at, so one set of proportions drives every usage size.
 *
 * Deliberately non-interactive: an early iteration made this draggable/rotatable and the
 * design chat explicitly walked that back ("forget about the mannequin being
 * interactable... simply use something that looks good and realistic").
 */
private const val REF_WIDTH = 200f
private const val REF_HEIGHT = 268f

private data class BodyPiece(
    val top: Float,
    val left: Float,
    val width: Float,
    val height: Float,
    val cornerRadius: Float,
    val isCircle: Boolean = false,
    val rotationDeg: Float = 0f,
)

// The 7-piece base silhouette: head, torso, hips, two arms, two legs. Values match the
// prototype's base-body <div>s exactly (REGIMEN.dc.html lines 55–61).
private val BASE_BODY = listOf(
    BodyPiece(top = 44f, left = 57f, width = 86f, height = 60f, cornerRadius = 24f), // torso
    BodyPiece(top = 96f, left = 67f, width = 66f, height = 64f, cornerRadius = 24f), // hips
    BodyPiece(top = 50f, left = 38f, width = 20f, height = 130f, cornerRadius = 10f, rotationDeg = -4f), // left arm
    BodyPiece(top = 50f, left = 142f, width = 20f, height = 130f, cornerRadius = 10f, rotationDeg = 4f), // right arm
    BodyPiece(top = 130f, left = 72f, width = 26f, height = 140f, cornerRadius = 13f, rotationDeg = -2f), // left leg
    BodyPiece(top = 130f, left = 102f, width = 26f, height = 140f, cornerRadius = 13f, rotationDeg = 2f), // right leg
    BodyPiece(top = 6f, left = 80f, width = 40f, height = 40f, cornerRadius = 20f, isCircle = true), // head
)

// Ground shadow ellipse under the feet (Exercise screen only). REGIMEN.dc.html line 264.
private val GROUND_SHADOW = BodyPiece(top = 254f, left = 45f, width = 110f, height = 14f, cornerRadius = 7f, isCircle = true)

@Composable
fun Mannequin(
    patches: List<MannequinPatch>,
    modifier: Modifier = Modifier,
    showGroundShadow: Boolean = false,
) {
    val colors = LocalTemp0Colors.current
    val baseColor = colors.mannequinBase
    val accent = colors.accent
    val accentDim = colors.accentDim

    Canvas(modifier = modifier) {
        val sx = size.width / REF_WIDTH
        val sy = size.height / REF_HEIGHT

        if (showGroundShadow) {
            drawPiece(GROUND_SHADOW, sx, sy, Color.Black.copy(alpha = 0.35f))
        }
        BASE_BODY.forEach { piece -> drawPiece(piece, sx, sy, baseColor) }
        patches.forEach { patch ->
            val color = if (patch.level == MuscleLevel.PRIMARY) accent else accentDim
            drawGlowPatch(patch, sx, sy, color)
        }
    }
}

private fun DrawScope.drawPiece(piece: BodyPiece, sx: Float, sy: Float, color: Color) {
    val rect = Rect(
        offset = Offset(piece.left * sx, piece.top * sy),
        size = Size(piece.width * sx, piece.height * sy),
    )
    rotate(degrees = piece.rotationDeg, pivot = rect.center) {
        if (piece.isCircle) {
            drawOval(color = color, topLeft = rect.topLeft, size = rect.size)
        } else {
            drawRoundRect(
                color = color,
                topLeft = rect.topLeft,
                size = rect.size,
                cornerRadius = CornerRadius(piece.cornerRadius * ((sx + sy) / 2f)),
            )
        }
    }
}

/** Draws one muscle patch twice: a blurred halo pass (approximating the CSS
 *  `box-shadow: 0 0 Npx color` glow — Compose's DrawScope has no native shadow, so this
 *  goes through the framework Paint's [BlurMaskFilter]) and a crisp fill on top, so the
 *  result reads as "solid shape with a glow around it" rather than a uniformly hazy blob. */
private fun DrawScope.drawGlowPatch(patch: MannequinPatch, sx: Float, sy: Float, color: Color) {
    val spec = patch.spec
    val rect = Rect(
        offset = Offset(spec.left * sx, spec.top * sy),
        size = Size(spec.width * sx, spec.height * sy),
    )
    val cornerRadiusPx = spec.cornerRadius * ((sx + sy) / 2f)
    val blurRadiusPx = 9f * ((sx + sy) / 2f) // ~half the CSS 18px box-shadow spread, matching a halo rather than a flat glow

    rotate(degrees = spec.rotationDeg, pivot = rect.center) {
        drawIntoCanvas { canvas ->
            val glowPaint = android.graphics.Paint().apply {
                isAntiAlias = true
                this.color = color.toArgb()
                if (blurRadiusPx > 0f) {
                    maskFilter = BlurMaskFilter(blurRadiusPx, BlurMaskFilter.Blur.NORMAL)
                }
            }
            if (spec.isCircle) {
                canvas.nativeCanvas.drawOval(
                    rect.left, rect.top, rect.right, rect.bottom, glowPaint,
                )
            } else {
                canvas.nativeCanvas.drawRoundRect(
                    rect.left, rect.top, rect.right, rect.bottom, cornerRadiusPx, cornerRadiusPx, glowPaint,
                )
            }
        }
        if (spec.isCircle) {
            drawOval(color = color, topLeft = rect.topLeft, size = rect.size)
        } else {
            drawRoundRect(color = color, topLeft = rect.topLeft, size = rect.size, cornerRadius = CornerRadius(cornerRadiusPx))
        }
    }
}
