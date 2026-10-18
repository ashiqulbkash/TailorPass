package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.ui.theme.Dimens

/** Garment icon on a 48dp light-blue rounded square (12dp corners), 30dp primary icon. */
@Composable
fun GarmentTile(icon: Painter, modifier: Modifier = Modifier) {
    IconTile(
        icon = icon,
        tint = MaterialTheme.colorScheme.primary,
        background = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier,
        size = Dimens.iconTile,
        iconSize = Dimens.garmentIconSize,
        shape = RoundedCornerShape(12.dp),
    )
}

/**
 * Garment grid card (Home): 80dp high (70 when Home also lists an incomplete measurement, 7.9), 16dp corners, 1dp border · [GarmentTile] · 17 semibold title over a 13 subtitle.
 * Long titles wrap to two lines; the design uses [titleFontSize] 16 for "সালওয়ার কামিজ".
 */
@Composable
fun GarmentCard(
    icon: Painter,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleFontSize: TextUnit = 17.sp,
    height: Dp = Dimens.garmentCardHeight,
) {
    val shape = MaterialTheme.shapes.large
    Row(
        modifier = modifier
            .height(height)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.space10),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
    ) {
        GarmentTile(icon)
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontSize = titleFontSize,
                lineHeight = titleFontSize * 1.2f,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
            )
        }
    }
}
