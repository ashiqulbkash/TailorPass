package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_check
import org.jetbrains.compose.resources.painterResource

/**
 * Colored circle or rounded square behind an icon or a number, content centered:
 * 32dp bullet markers (1.2–1.4, 12.2), 96dp tiles (1.1 rounded, 6.3 round), later garment tiles.
 */
@Composable
fun IconTile(
    background: Color,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    shape: Shape = CircleShape,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.size(size).background(background, shape),
        contentAlignment = Alignment.Center,
        content = content,
    )
}

/** IconTile with one tinted icon. */
@Composable
fun IconTile(
    icon: Painter,
    tint: Color,
    background: Color,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    iconSize: Dp = 18.dp,
    shape: Shape = CircleShape,
) {
    IconTile(background = background, modifier = modifier, size = size, shape = shape) {
        Icon(painter = icon, contentDescription = null, tint = tint, modifier = Modifier.size(iconSize))
    }
}

/**
 * Selected mark in a card corner: 22dp primary circle with a 14dp white check (fit card 1.5, restore options 12.3);
 * 20dp with a 12dp check on the preset picture cards (7.6, 7.10).
 */
@Composable
fun CheckBadge(modifier: Modifier = Modifier, size: Dp = 22.dp, iconSize: Dp = 14.dp) {
    IconTile(
        icon = painterResource(Res.drawable.ic_check),
        tint = MaterialTheme.colorScheme.onPrimary,
        background = MaterialTheme.colorScheme.primary,
        modifier = modifier,
        size = size,
        iconSize = iconSize,
    )
}
