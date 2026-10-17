package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.ui.diagram.BodyFigure
import com.tech_hunt.tailorpass.ui.diagram.BodyOutline
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import org.jetbrains.compose.resources.painterResource

private val ThumbShape = RoundedCornerShape(14.dp)

/**
 * Accepted photo (6.1): 190dp tile, 3dp green border, grey silhouette, 26dp green check in the corner, label below.
 * Design phase: the silhouette stands in for the photo; the real photo is never stored (it lives in memory only).
 */
@Composable
fun PhotoThumb(label: String, outline: BodyOutline, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(Dimens.space6)) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(MaapTheme.colors.photoPlaceholder, ThumbShape)
                .border(3.dp, MaapTheme.colors.success, ThumbShape)
                .semantics {
                    contentDescription = label
                    role = Role.Image
                },
            contentAlignment = Alignment.Center,
        ) {
            BodyFigure(outline, Modifier.size(width = 62.dp, height = 160.dp), fill = MaterialTheme.colorScheme.outline)
            IconTile(
                icon = painterResource(Res.drawable.ic_check),
                tint = MaapTheme.colors.onSuccess,
                background = MaapTheme.colors.success,
                size = 26.dp,
                iconSize = 16.dp,
                modifier = Modifier.align(Alignment.TopEnd).padding(top = Dimens.space8, end = Dimens.space8),
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
