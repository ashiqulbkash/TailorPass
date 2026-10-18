package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme

/**
 * Preference picture card (7.10 neck depth; 7.6 length in Step 6): 128dp high, 14dp corners, picture · 15 label ·
 * 18 bold value, centered. Selected: 2dp primary border, primary container, bold label, primary value and a 20dp
 * check badge in the top end corner. Not selected: 1dp light border.
 */
@Composable
fun PresetPictureCard(
    label: String,
    value: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    picture: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier
            .height(128.dp)
            .selectableCard(selected, onClick, unselectedBorder = MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            Modifier.fillMaxSize().padding(horizontal = Dimens.space4, vertical = Dimens.space8),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.space4, Alignment.CenterVertically),
        ) {
            picture()
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = value,
                style = MaapTheme.numbers.compactValue,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            )
        }
        if (selected) {
            CheckBadge(Modifier.align(Alignment.TopEnd).padding(top = 6.dp, end = 6.dp), size = 20.dp, iconSize = 12.dp)
        }
    }
}
