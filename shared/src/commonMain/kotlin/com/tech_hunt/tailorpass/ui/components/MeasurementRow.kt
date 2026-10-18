package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.model.SourceBadge
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_edit
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.str
import org.jetbrains.compose.resources.painterResource

/** Low accuracy and missing values ask for attention: orange row, orange value/icon. */
val SourceBadge.needsAttention: Boolean
    get() = this == SourceBadge.AccuracyLow || this == SourceBadge.Missing

/**
 * Where a value came from (8.1–8.5): 13 semibold label on a 10dp-rounded tint, 8dp side padding. The symbol (● ◐ ▲ ✎)
 * is part of the text, so colour never carries the meaning alone.
 */
@Composable
fun SourceBadgeChip(badge: SourceBadge, modifier: Modifier = Modifier) {
    val colors = MaapTheme.colors
    val (background, content) = when (badge) {
        SourceBadge.AccuracyHigh -> colors.successContainer to colors.onSuccessContainer
        SourceBadge.AccuracyMedium -> colors.neutralBadge to colors.onNeutralBadge
        SourceBadge.AccuracyLow, SourceBadge.Missing -> colors.warningBadge to colors.onWarningBadge
        SourceBadge.YourChoice -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
        SourceBadge.TapeMeasured -> MaterialTheme.colorScheme.surfaceVariant to colors.onTapeBadge
    }
    TextBadge(
        text = str(badge.labelKey),
        background = background,
        contentColor = content,
        modifier = modifier,
        fontWeight = FontWeight.SemiBold,
        padding = PaddingValues(horizontal = Dimens.space8),
    )
}

/**
 * One measurement in a list card (8.1–8.5): name (17 semibold; 16 in the sectioned 8.4) over its badge, 24 bold
 * value, 20dp edit icon. The whole row (≥52dp) opens the edit sheet. Low accuracy / missing: orange row background,
 * orange icon, and a missing value shows an orange "—". [divider] draws a 1dp line at the top.
 */
@Composable
fun MeasurementRow(
    name: String,
    value: String?,
    badge: SourceBadge,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    divider: Boolean = false,
    nameSize: TextUnit = MaterialTheme.typography.titleSmall.fontSize,
) {
    val attention = badge.needsAttention
    val dividerColor = MaapTheme.colors.divider
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (attention) Modifier.background(MaapTheme.colors.warningRow) else Modifier)
            .clickable(role = Role.Button, onClick = onClick)
            .drawBehind {
                if (divider) drawLine(dividerColor, Offset(0f, 0.5.dp.toPx()), Offset(size.width, 0.5.dp.toPx()), 1.dp.toPx())
            }
            .heightIn(min = Dimens.listRowMinHeight)
            .padding(start = Dimens.space14, end = Dimens.space12, top = Dimens.space4, bottom = Dimens.space4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                fontSize = nameSize,
                lineHeight = (nameSize.value * 1.2f).sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            SourceBadgeChip(badge)
        }
        Text(
            text = value ?: "—",
            style = MaapTheme.numbers.listValue,
            color = if (value == null) MaapTheme.colors.onWarningBadge else MaterialTheme.colorScheme.onSurface,
        )
        Icon(
            painter = painterResource(Res.drawable.ic_edit),
            contentDescription = null,
            tint = if (attention) MaapTheme.colors.onWarningBadge else MaapTheme.colors.textTertiary,
            modifier = Modifier.size(20.dp),
        )
    }
}
