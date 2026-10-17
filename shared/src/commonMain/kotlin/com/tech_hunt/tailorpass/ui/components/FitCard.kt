package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tech_hunt.tailorpass.model.Fit
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_fit_shirt
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.str
import org.jetbrains.compose.resources.painterResource

/**
 * Fit preference card (1.5): 108dp high, 14dp corners, shirt drawn wider for looser fits + label.
 * Selected: 2dp primary border, primary container, 22dp check badge in the top end corner.
 */
@Composable
fun FitCard(
    fit: Fit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    Box(
        modifier = modifier
            .height(108.dp)
            .selectableCard(selected, onClick, unselectedBorder = MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.space4),
        ) {
            // Design: 52dp box, 40-unit viewBox, shirt scaled (1.05 | 1.3 | 1.6) × 1.45 → dp = 24 × scale × 1.3.
            Box(Modifier.size(52.dp), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(Res.drawable.ic_fit_shirt),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(if (selected) color else MaapTheme.colors.textTertiary),
                    modifier = Modifier.size(width = (31.2f * fit.widthScale).dp, height = 45.2.dp),
                )
            }
            Text(
                text = str(fit.labelKey),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = color,
                maxLines = 1,
            )
        }
        if (selected) {
            CheckBadge(Modifier.align(Alignment.TopEnd).padding(top = Dimens.space6, end = Dimens.space6))
        }
    }
}

private val Fit.widthScale: Float
    get() = when (this) {
        Fit.Slim -> 1.05f
        Fit.Regular -> 1.3f
        Fit.Loose -> 1.6f
    }
