package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.resources.ic_close
import com.tech_hunt.tailorpass.resources.ic_step_current
import com.tech_hunt.tailorpass.resources.ic_step_pending
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.theme.PillShape
import org.jetbrains.compose.resources.painterResource

/** Rounded progress bar: primary fill on a light track. [progress] 0..1. 12dp (6.1) by default. */
@Composable
fun MaapProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = Dimens.progressBarHeight,
) {
    val value = progress.coerceIn(0f, 1f)
    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .clip(PillShape)
            .background(MaapTheme.colors.progressTrack)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo(value, 0f..1f) },
    ) {
        Box(Modifier.fillMaxWidth(value).fillMaxHeight().background(MaterialTheme.colorScheme.primary, PillShape))
    }
}

/** State of one line in a status list. */
enum class LineStatus { Done, Current, Pending, Failed }

/**
 * Status list line: 22dp icon + 16sp text. Done = green check, Current = primary ring + bold,
 * Pending = grey circle + grey text, Failed = orange ✕ + orange semibold (6.1 processing, 6.2 photo checks).
 * The icon shape carries the meaning together with the color.
 */
@Composable
fun StatusLine(text: String, status: LineStatus, modifier: Modifier = Modifier) {
    val colors = MaapTheme.colors
    val (icon, tint) = when (status) {
        LineStatus.Done -> Res.drawable.ic_check to colors.success
        LineStatus.Current -> Res.drawable.ic_step_current to MaterialTheme.colorScheme.primary
        LineStatus.Pending -> Res.drawable.ic_step_pending to MaterialTheme.colorScheme.outline
        LineStatus.Failed -> Res.drawable.ic_close to colors.warning
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
    ) {
        Icon(painterResource(icon), contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = when (status) {
                LineStatus.Current -> FontWeight.Bold
                LineStatus.Failed -> FontWeight.SemiBold
                LineStatus.Done, LineStatus.Pending -> FontWeight.Normal
            },
            color = when (status) {
                LineStatus.Pending -> colors.textTertiary
                LineStatus.Failed -> colors.onWarningBadge
                LineStatus.Done, LineStatus.Current -> MaterialTheme.colorScheme.onSurface
            },
        )
    }
}
