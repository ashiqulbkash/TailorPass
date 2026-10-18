package com.tech_hunt.tailorpass.ui.diagram

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import com.tech_hunt.tailorpass.model.DiagramSpec
import com.tech_hunt.tailorpass.ui.components.NumberBadge
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.drawable
import com.tech_hunt.tailorpass.ui.util.localDigits
import org.jetbrains.compose.resources.painterResource

/**
 * Numbered body diagram drawn from the model/Diagrams data: outline layer, marks layer, numbered circles.
 * Only the spec's crop range (cropTop..cropBottom) is shown, scaled uniformly to fit and centred, like the design's
 * SVG viewBox. [badgeRadius] and [badgeFontSize] are in viewport units (the SVG's r / font-size), so the circles scale
 * with the drawing: 10 / 13 on Result 9.1, 15 / 18 on 9.3, 9 / 12 on the PDF and PNG.
 */
@Composable
fun NumberedDiagram(
    spec: DiagramSpec,
    modifier: Modifier = Modifier,
    outlineColor: Color = MaapTheme.colors.diagramOutline,
    marksColor: Color = MaterialTheme.colorScheme.primary,
    badgeRadius: Float = 10f,
    badgeFontSize: Float = 13f,
) {
    val outline = painterResource(drawable(spec.outlineName))
    val marks = painterResource(drawable(spec.marksName))
    val density = LocalDensity.current
    BoxWithConstraints(modifier, contentAlignment = Alignment.Center) {
        val cropHeight = spec.cropBottom - spec.cropTop
        val scale = minOf(maxWidth / spec.viewportWidth, maxHeight / cropHeight)
        Box(Modifier.size(scale * spec.viewportWidth, scale * cropHeight).clipToBounds()) {
            Canvas(Modifier.size(scale * spec.viewportWidth, scale * cropHeight)) {
                val px = size.width / spec.viewportWidth
                val full = Size(size.width, spec.viewportHeight * px)
                translate(top = -spec.cropTop * px) {
                    drawLayer(outline, full, outlineColor)
                    drawLayer(marks, full, marksColor)
                }
            }
            val badge = scale * badgeRadius * 2
            val fontSize = with(density) { (scale * badgeFontSize).toSp() }
            spec.badges.forEach { b ->
                NumberBadge(
                    number = localDigits(b.number.toString()),
                    modifier = Modifier.offset(
                        x = scale * b.x - badge / 2,
                        y = scale * (b.y - spec.cropTop) - badge / 2,
                    ),
                    size = badge,
                    fontSize = fontSize,
                    background = marksColor,
                )
            }
        }
    }
}

private fun DrawScope.drawLayer(painter: Painter, size: Size, color: Color) {
    with(painter) { draw(size, colorFilter = ColorFilter.tint(color)) }
}
