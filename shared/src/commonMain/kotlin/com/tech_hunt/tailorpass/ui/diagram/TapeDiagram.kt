package com.tech_hunt.tailorpass.ui.diagram

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.str
import kotlin.math.min

/** A text label in a diagram: string key, position (viewport units, baseline), centered or start-aligned. */
data class DiagramLabel(val key: String, val x: Float, val y: Float, val centered: Boolean = false)

/** Which colour a mark uses: primary (tape), warning (orange start/end dots), tertiary grey (neckline). */
enum class MarkColor { Primary, Warning, Tertiary }

/** One mark: SVG path data, stroke width (0 = filled), dash pattern (viewport units). */
class DiagramMark(
    val path: String,
    val color: MarkColor = MarkColor.Primary,
    val strokeWidth: Float = 0f,
    val dash: FloatArray? = null,
)

/**
 * Tape-entry diagrams (7.3, 7.4, 7.10), from the design SVGs: grey body outline + tape marks + labels.
 * Drawn in code because the tape is dashed, which VectorDrawable cannot do (diagram_tape_*.xml hold only the mark).
 * Waist has no design screen: its mark is diagram_tape_waist.xml on the chest outline, labelled at the navel.
 */
enum class TapeDiagram(
    val viewportWidth: Float,
    val viewportHeight: Float,
    val outline: String,
    val outlineWidth: Float,
    val marks: List<DiagramMark>,
    val labels: List<DiagramLabel>,
    val labelColor: LabelColor,
) {
    Chest(
        200f, 240f, UPPER_BODY, 2.4f,
        marks = listOf(
            DiagramMark("M58,118 a42,9 0 1,0 84,0 a42,9 0 1,0 -84,0 Z", strokeWidth = 3.4f, dash = floatArrayOf(7f, 5f)),
            DiagramMark("M146 118 H180 M174 113 L180 118 L174 123", strokeWidth = 2.6f),
            DiagramMark(circle(60f, 112f, 3f), MarkColor.Warning),
            DiagramMark(circle(140f, 112f, 3f), MarkColor.Warning),
        ),
        labels = listOf(DiagramLabel("label_underarm", 100f, 146f, centered = true)),
        labelColor = LabelColor.Secondary,
    ),
    Waist(
        200f, 240f, UPPER_BODY, 2.4f,
        marks = listOf(
            DiagramMark("M60,160 a40,9 0 1,0 80,0 a40,9 0 1,0 -80,0 Z", strokeWidth = 3.4f, dash = floatArrayOf(7f, 5f)),
            DiagramMark("M144 160 H178 M172 155 L178 160 L172 165", strokeWidth = 2.6f),
        ),
        labels = listOf(DiagramLabel("label_navel", 100f, 188f, centered = true)),
        labelColor = LabelColor.Secondary,
    ),
    Sleeve(
        230f, 226f,
        "M73,28 a17,21 0 1,0 34,0 a17,21 0 1,0 -34,0 Z " +
            "M81 49 L81 60 C65 63 47 67 37 77 C29 108 23 158 19 218 L33 220 C39 178 45 140 51 112 L53 226 " +
            "M127 226 L129 112 C131 130 136 144 144 150 L136 206 L150 210 L160 148 C156 118 152 95 143 77 " +
            "C133 67 115 63 99 60 L99 49",
        2.4f,
        marks = listOf(
            DiagramMark("M146 74 L166 148 L154 214", strokeWidth = 4f, dash = floatArrayOf(8f, 5f)),
            DiagramMark(circle(146f, 74f, 6f)),
            DiagramMark(circle(166f, 148f, 6f)),
            DiagramMark(circle(154f, 214f, 6f)),
        ),
        labels = listOf(
            DiagramLabel("label_shoulder", 156f, 66f),
            DiagramLabel("label_elbow", 174f, 152f),
            DiagramLabel("label_wrist", 164f, 222f),
        ),
        labelColor = LabelColor.Emphasis,
    ),
    NeckDepth(
        120f, 130f,
        "M47,18 a13,16 0 1,0 26,0 a13,16 0 1,0 -26,0 Z " +
            "M54 34 V42 C40 44 26 48 18 56 L12 130 M66 34 V42 C80 44 94 48 102 56 L108 130",
        2f,
        marks = listOf(
            DiagramMark("M46 43 Q60 92 74 43", MarkColor.Tertiary, strokeWidth = 2.2f),
            DiagramMark("M46 43 H32", strokeWidth = 1.6f, dash = floatArrayOf(3f, 3f)),
            DiagramMark("M36 43 V76 M32 70 L36 76 L40 70", strokeWidth = 2.6f),
            DiagramMark("M36 76 H58", strokeWidth = 1.6f, dash = floatArrayOf(3f, 3f)),
            DiagramMark(circle(46f, 43f, 3.5f)),
        ),
        labels = emptyList(),
        labelColor = LabelColor.Secondary,
    ),
}

enum class LabelColor { Secondary, Emphasis }

/** Upper body (head + torso + arms) of the chest/waist diagrams, cut at y = 240. */
private const val UPPER_BODY =
    "M83,28 a17,21 0 1,0 34,0 a17,21 0 1,0 -34,0 Z " +
        "M91 49 L91 60 C75 63 57 67 47 77 C39 108 33 158 29 218 L43 220 C49 178 55 140 61 112 L63 240 " +
        "M137 240 L139 112 C145 140 151 178 157 220 L171 218 C167 158 161 108 153 77 C143 67 125 63 109 60 L109 49"

private fun circle(cx: Float, cy: Float, r: Float): String =
    "M${cx - r},$cy a$r,$r 0 1,0 ${2 * r},0 a$r,$r 0 1,0 ${-2 * r},0 Z"

/** Label font size in viewport units (SVG font-size="13"). */
private const val LABEL_SIZE = 13f

/** Draws [diagram] scaled to fit (centered, aspect kept, like SVG "meet"); labels in Hind Siliguri. */
@Composable
fun TapeDiagramView(diagram: TapeDiagram, modifier: Modifier = Modifier) {
    val outline = remember(diagram) { PathParser().parsePathString(diagram.outline).toPath() }
    val marks = remember(diagram) { diagram.marks.map { it to PathParser().parsePathString(it.path).toPath() } }
    val measurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.bodySmall.copy(
        color = when (diagram.labelColor) {
            LabelColor.Secondary -> MaterialTheme.colorScheme.onSurfaceVariant
            LabelColor.Emphasis -> MaapTheme.colors.textEmphasis
        },
    )
    val labels = diagram.labels.map { it to str(it.key) }
    val outlineColor = MaapTheme.colors.diagramOutline
    val primary = MaterialTheme.colorScheme.primary
    val warning = MaapTheme.colors.warning
    val tertiary = MaapTheme.colors.textTertiary
    Canvas(modifier) {
        val s = min(size.width / diagram.viewportWidth, size.height / diagram.viewportHeight)
        val left = (size.width - diagram.viewportWidth * s) / 2f
        val top = (size.height - diagram.viewportHeight * s) / 2f
        translate(left, top) {
            scale(s, s, pivot = Offset.Zero) {
                drawStroke(outline, outlineColor, diagram.outlineWidth)
                marks.forEach { (mark, path) ->
                    val color = when (mark.color) {
                        MarkColor.Primary -> primary
                        MarkColor.Warning -> warning
                        MarkColor.Tertiary -> tertiary
                    }
                    if (mark.strokeWidth == 0f) drawPath(path, color) else drawStroke(path, color, mark.strokeWidth, mark.dash)
                }
            }
        }
        // As in the SVG, the 13 font size is in viewport units, so the text scales with the diagram.
        val scaledStyle = labelStyle.copy(fontSize = (LABEL_SIZE * s).toSp(), lineHeight = (LABEL_SIZE * s * 1.3f).toSp())
        labels.forEach { (label, text) ->
            drawLabel(measurer.measure(text, scaledStyle), label, left + label.x * s, top + label.y * s)
        }
    }
}

private fun DrawScope.drawStroke(path: Path, color: Color, width: Float, dash: FloatArray? = null) {
    drawPath(
        path, color,
        style = Stroke(
            width = width,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = dash?.let { PathEffect.dashPathEffect(it) },
        ),
    )
}

private fun DrawScope.drawLabel(
    layout: androidx.compose.ui.text.TextLayoutResult,
    label: DiagramLabel,
    x: Float,
    baselineY: Float,
) {
    val startX = if (label.centered) x - layout.size.width / 2f else x
    drawText(layout, topLeft = Offset(startX, baselineY - layout.firstBaseline))
}

/**
 * Neckline picture of a neck-depth card (7.10): shoulders + neckline curve, 64×52 viewport. [depth] is the
 * curve's control-point y (26 low, 36 medium, 48 high). Selected: primary curve on surface; else grey on light grey.
 */
@Composable
fun NecklineFigure(depth: Float, selected: Boolean, modifier: Modifier = Modifier) {
    val shoulders = remember {
        PathParser().parsePathString("M4 18 C14 12 20 10 24 10 M60 18 C50 12 44 10 40 10 M4 18 V50 M60 18 V50").toPath()
    }
    val neck = remember(depth) { PathParser().parsePathString("M24 10 Q32 $depth 40 10").toPath() }
    val outlineColor = MaapTheme.colors.diagramOutline
    val curve = if (selected) MaterialTheme.colorScheme.primary else MaapTheme.colors.textTertiary
    val fill = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
    Canvas(modifier) {
        val s = min(size.width / 64f, size.height / 52f)
        translate((size.width - 64f * s) / 2f, (size.height - 52f * s) / 2f) {
            scale(s, s, pivot = Offset.Zero) {
                drawStroke(shoulders, outlineColor, 2f)
                drawPath(neck, fill)
                drawStroke(neck, curve, 2.4f)
            }
        }
    }
}
