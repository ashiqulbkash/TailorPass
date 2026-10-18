package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tech_hunt.tailorpass.ui.theme.Dimens

/**
 * Icon + text banner: 14dp corners, 10 / 14 padding, optional 1dp border.
 * Tint (garment tip 4.1/4.2), white with border (permission hint 3.2); later orange (7.7), green (12.5), camera (5.x).
 * centerIcon = false puts the icon at the top (2dp down, next to the first text line).
 * verticalPadding: 8 on the helper banner (7.4) and the verify banners (8.x).
 */
@Composable
fun InfoBanner(
    icon: Painter,
    iconTint: Color,
    background: Color,
    modifier: Modifier = Modifier,
    border: Color? = null,
    iconSize: Dp = Dimens.iconSize,
    gap: Dp = Dimens.space10,
    centerIcon: Boolean = true,
    verticalPadding: Dp = Dimens.space10,
    content: @Composable RowScope.() -> Unit,
) {
    val shape = MaterialTheme.shapes.medium
    Row(
        modifier = modifier
            .background(background, shape)
            .then(if (border != null) Modifier.border(1.dp, border, shape) else Modifier)
            .padding(horizontal = Dimens.space14, vertical = verticalPadding),
        verticalAlignment = if (centerIcon) Alignment.CenterVertically else Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(gap),
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.padding(top = if (centerIcon) 0.dp else 2.dp).size(iconSize),
        )
        content()
    }
}
