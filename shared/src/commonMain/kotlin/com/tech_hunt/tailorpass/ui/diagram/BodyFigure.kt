package com.tech_hunt.tailorpass.ui.diagram

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import kotlin.math.min

/**
 * Body silhouettes of the camera flow, in a 200×520 viewport (same paths as the body_*.xml drawables and the
 * 5.x / 6.x designs). [body] is the outline (head + body); [details] are extra open lines drawn only as strokes
 * (side pose: arm line and floor). Drawn in code because the overlay is dashed, which VectorDrawable cannot do.
 */
enum class BodyOutline(val body: String, val details: String? = null) {
    MaleFront(
        "M74,40 a26,32 0 1,0 52,0 a26,32 0 1,0 -52,0 Z " +
            "M88,74 L88,88 C72,92 54,96 44,108 C36,140 30,200 24,290 L38,294 C46,240 52,190 60,150 L62,250 " +
            "C60,330 64,420 66,500 L92,500 L98,300 L102,300 L108,500 L134,500 C136,420 140,330 138,250 L140,150 " +
            "C148,190 154,240 162,294 L176,290 C170,200 164,140 156,108 C146,96 128,92 112,88 L112,74",
    ),
    FemaleFront(
        "M76,40 a24,31 0 1,0 48,0 a24,31 0 1,0 -48,0 Z " +
            "M90,74 L90,88 C76,92 62,96 54,106 C46,140 40,200 34,286 L46,290 C52,240 58,196 64,158 " +
            "C62,180 64,205 70,232 C64,255 58,275 58,300 C60,380 66,450 70,500 L94,500 L99,320 L101,320 L106,500 " +
            "L130,500 C134,450 140,380 142,300 C142,275 136,255 130,232 C136,205 138,180 136,158 C142,196 148,240 154,290 " +
            "L166,286 C160,200 154,140 146,106 C138,96 124,92 110,88 L110,74",
    ),
    MaleSide(
        "M80,40 a24,32 0 1,0 48,0 a24,32 0 1,0 -48,0 Z " +
            "M96,72 L96,88 C82,94 78,112 80,150 C82,200 80,240 84,272 L86,500 L112,500 L114,282 " +
            "C120,242 124,200 122,150 C120,112 116,92 108,86 L110,72",
        details = "M102,100 C108,160 108,220 104,290 M78,500 H124",
    ),
    FemaleSide(
        "M81,40 a23,31 0 1,0 46,0 a23,31 0 1,0 -46,0 Z " +
            "M96,72 L96,88 C84,94 80,110 80,140 C72,158 74,182 84,194 C82,215 80,240 82,268 C80,290 82,300 84,320 " +
            "L86,500 L112,500 L116,320 C124,296 126,278 122,258 C120,236 118,214 120,196 C122,160 120,112 108,86 L110,72",
        details = "M102,100 C108,160 108,220 104,290 M78,500 H124",
    ),
}

private const val VIEW_W = 200f
private const val VIEW_H = 520f

/**
 * Draws [outline] scaled to fit (centered, aspect kept, like SVG "meet"). Stroke width and dashes are in viewport
 * units, as in the SVG. The body is left open at the neck (as in the designs); filling closes it. fill = null → outline only; stroke = null → filled silhouette only (details are skipped).
 */
@Composable
fun BodyFigure(
    outline: BodyOutline,
    modifier: Modifier = Modifier,
    fill: Color? = null,
    stroke: Color? = null,
    strokeWidth: Float = 3f,
    dashed: Boolean = false,
) {
    val body = remember(outline) { PathParser().parsePathString(outline.body).toPath() }
    val details = remember(outline) { outline.details?.let { PathParser().parsePathString(it).toPath() } }
    Canvas(modifier) {
        val s = min(size.width / VIEW_W, size.height / VIEW_H)
        translate((size.width - VIEW_W * s) / 2f, (size.height - VIEW_H * s) / 2f) {
            scale(s, s, pivot = Offset.Zero) {
                if (fill != null) drawPath(body, fill, style = Fill)
                if (stroke != null) {
                    val style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                        pathEffect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(9f, 7f)) else null,
                    )
                    drawPath(body, stroke, style = style)
                    details?.let { drawPath(it, stroke, style = style) }
                }
            }
        }
    }
}
