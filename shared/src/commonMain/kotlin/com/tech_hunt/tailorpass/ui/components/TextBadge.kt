package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.tech_hunt.tailorpass.ui.theme.Dimens

/**
 * Small rounded label (10dp corners): "✎ পছন্দের মাপ" (7.6, 7.10: 13 bold, primary on primary container),
 * "সাহায্যকারী লাগবে" (7.2: 12 semibold with a 14dp icon, neutral badge colors), "এখন" (7.2: white on primary),
 * "আপনার" (11.1: 12 bold white on primary, 8dp corners).
 */
@Composable
fun TextBadge(
    text: String,
    background: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    fontSize: TextUnit = MaterialTheme.typography.labelSmall.fontSize,
    fontWeight: FontWeight = FontWeight.Bold,
    padding: PaddingValues = PaddingValues(horizontal = Dimens.space10, vertical = 1.dp),
    cornerRadius: Dp = 10.dp,
) {
    Row(
        modifier = modifier
            .background(background, RoundedCornerShape(cornerRadius))
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space4),
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(14.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = contentColor,
            maxLines = 1,
        )
    }
}
