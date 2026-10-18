package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_chevron_right
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import org.jetbrains.compose.resources.painterResource

/** Card holding [SettingsRow]s (12.1, 12.6): a MaapCard without padding, rows clipped to its corners. */
@Composable
fun SettingsGroup(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    MaapCard(
        modifier = modifier.fillMaxWidth().clip(MaterialTheme.shapes.large),
        contentPadding = PaddingValues(),
        content = content,
    )
}

/**
 * Tappable settings row (12.1, 12.6): 24dp icon · 17 medium title (+ 14 grey subtitle, line height 1.3) ·
 * 16 grey [value] · 20dp chevron, 14dp apart. Min 58dp high, 66 with a subtitle (About: 56 / 64 via [minHeight]);
 * 16 start / 12 end padding, 8 top and bottom with a subtitle. [divider]: 1dp line under the row (not the last one).
 * "সব ডেটা মুছুন" (12.1): red colors, bold title, no chevron, 16 end padding.
 */
@Composable
fun SettingsRow(
    icon: Painter,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    value: String? = null,
    divider: Boolean = false,
    chevron: Boolean = true,
    minHeight: Dp = if (subtitle != null) 66.dp else 58.dp,
    endPadding: Dp = Dimens.space12,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    titleWeight: FontWeight = FontWeight.Medium,
    subtitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val dividerColor = MaapTheme.colors.divider
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .clickable(role = Role.Button, onClick = onClick)
            .then(
                if (divider) {
                    Modifier.drawBehind {
                        val y = size.height - 0.5.dp.toPx()
                        drawLine(dividerColor, Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
                    }
                } else Modifier,
            )
            .padding(
                start = Dimens.space16,
                end = endPadding,
                top = if (subtitle != null) Dimens.space8 else 0.dp,
                bottom = if (subtitle != null) Dimens.space8 else 0.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space14),
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(Dimens.iconSize))
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = titleWeight,
                lineHeight = 22.sp,
                color = titleColor,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 18.sp,
                    color = subtitleColor,
                )
            }
        }
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
        if (chevron) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null,
                tint = MaapTheme.colors.textTertiary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
