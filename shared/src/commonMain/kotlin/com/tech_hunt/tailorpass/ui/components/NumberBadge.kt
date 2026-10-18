package com.tech_hunt.tailorpass.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Numbered circle (an IconTile) matching a diagram number: primary circle, bold onPrimary digits ([number] is already localised).
 * 20dp / 12 on Result (9.1; 11 on 9.3), 30 / 16 and 26 / 14 on the PDF (10.1, 10.2), 68 / 36 on the PNG (10.3).
 */
@Composable
fun NumberBadge(
    number: String,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    fontSize: TextUnit = 12.sp,
    background: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    IconTile(background = background, modifier = modifier, size = size) {
        Text(
            text = number,
            style = MaterialTheme.typography.labelSmall,
            fontSize = fontSize,
            lineHeight = fontSize,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}
