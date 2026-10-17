package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.resources.ic_share
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import org.jetbrains.compose.resources.painterResource

/**
 * One saved measurement in the Home list card: 17 semibold title, 14 grey subtitle, 48dp share button.
 * [status] adds a green ✓ text after the subtitle ("দর্জিকে পাঠানো"). Tap = open, long-press = options menu.
 * [showDivider] draws the 1dp line under the row (all rows but the last).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onShare: () -> Unit,
    shareContentDescription: String,
    modifier: Modifier = Modifier,
    status: String? = null,
    showDivider: Boolean = false,
) {
    val divider = MaapTheme.colors.divider
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (showDivider) {
                    Modifier.drawBehind {
                        val y = size.height - 0.5.dp.toPx()
                        drawLine(divider, Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
                    }
                } else Modifier,
            )
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(start = Dimens.space14, end = Dimens.space6, top = Dimens.space8, bottom = Dimens.space8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
    ) {
        Column(
            modifier = Modifier.weight(1f).heightIn(min = Dimens.minTouchTarget),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.space4),
            ) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
                if (status != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_check),
                            contentDescription = null,
                            tint = MaapTheme.colors.onSuccessContainer,
                            modifier = Modifier.size(14.dp),
                        )
                        Text(
                            text = status,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaapTheme.colors.onSuccessContainer,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
        MaapIconButton(
            icon = painterResource(Res.drawable.ic_share),
            contentDescription = shareContentDescription,
            onClick = onShare,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}
