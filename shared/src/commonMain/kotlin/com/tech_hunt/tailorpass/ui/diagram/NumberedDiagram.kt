package com.tech_hunt.tailorpass.ui.diagram

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.model.DiagramSpec
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.drawable
import com.tech_hunt.tailorpass.util.toBanglaDigits
import org.jetbrains.compose.resources.painterResource

/**
 * Numbered body diagram drawn from the model/Diagrams data (Compose Multiplatform).
 * To show only the crop range, add Modifier.clipToBounds() + graphicsLayer(translationY).
 */
@Composable
fun NumberedDiagram(spec: DiagramSpec, modifier: Modifier = Modifier, badgeSize: Dp = 20.dp) {
    BoxWithConstraints(modifier) {
        val sx = maxWidth / spec.viewportWidth
        val sy = maxHeight / spec.viewportHeight
        Image(painterResource(drawable(spec.outlineName)), null, Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(MaapTheme.colors.diagramOutline))
        Image(painterResource(drawable(spec.marksName)), null, Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary))
        spec.badges.forEach { b ->
            Box(
                Modifier.offset(x = sx * b.x - badgeSize / 2, y = sy * b.y - badgeSize / 2)
                    .size(badgeSize).background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(b.number.toString().toBanglaDigits(), color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
