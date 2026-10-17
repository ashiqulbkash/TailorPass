package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_arrow_forward
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import org.jetbrains.compose.resources.painterResource

private val CardShape = RoundedCornerShape(20.dp)
private val TileShape = RoundedCornerShape(18.dp)

/**
 * Measuring-method card (3.1): min 230dp, 20dp corners, 20 padding · 64dp icon tile + tag badge ·
 * 22 bold title · 16 description · "choose" link at the bottom.
 * highlighted (recommended): 2dp primary border and a filled primary tag; otherwise 1dp light border and a grey tag.
 */
@Composable
fun MethodCard(
    icon: Painter,
    tag: String,
    title: String,
    description: String,
    actionText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
    iconSize: Dp = 36.dp,
) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 230.dp)
            .clip(CardShape)
            .background(colors.surface)
            .border(
                if (highlighted) BorderStroke(2.dp, colors.primary) else BorderStroke(1.dp, colors.outlineVariant),
                CardShape,
            )
            .clickable(role = Role.Button, onClick = onClick)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(Dimens.space10),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconTile(
                icon = icon,
                tint = colors.primary,
                background = colors.primaryContainer,
                size = 64.dp,
                iconSize = iconSize,
                shape = TileShape,
            )
            Spacer(Modifier.weight(1f))
            MethodTag(tag, highlighted)
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = colors.onSurface,
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 23.sp,
            color = MaapTheme.colors.textEmphasis,
        )
        Spacer(Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.space6),
        ) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
            )
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_forward),
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

/** 13sp tag, 3 / 10 padding, 12dp corners: filled primary (highlighted) or neutral grey. */
@Composable
private fun MethodTag(text: String, highlighted: Boolean) {
    Box(
        Modifier
            .background(
                if (highlighted) MaterialTheme.colorScheme.primary else MaapTheme.colors.neutralBadge,
                RoundedCornerShape(12.dp),
            )
            .padding(horizontal = Dimens.space10, vertical = 3.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (highlighted) FontWeight.Bold else FontWeight.SemiBold,
            color = if (highlighted) MaterialTheme.colorScheme.onPrimary else MaapTheme.colors.onNeutralBadge,
        )
    }
}
