package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.emphasize

/**
 * Unfinished measurement on Home (7.9): 16dp card with a 5dp orange start border (1dp light border elsewhere),
 * a 17 bold [title] with its [status] part in orange ("পাঞ্জাবি · অসমাপ্ত") over a 14 progress line, a 44dp
 * Continue button, and an 8dp orange bar.
 * [progress] 0..1; [progressDescription] is read out for the bar.
 */
@Composable
fun IncompleteCard(
    title: String,
    status: String,
    progressText: String,
    progress: Float,
    progressDescription: String,
    actionText: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = MaapTheme.colors.problem
    val border = MaterialTheme.colorScheme.outlineVariant
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .drawBehind {
                drawRect(border)
                // The orange start border takes the rounded start corners, as a CSS border-left does.
                drawRect(accent, size = Size(16.dp.toPx(), size.height))
            }
            .padding(start = 5.dp, top = 1.dp, end = 1.dp, bottom = 1.dp)
            .background(MaterialTheme.colorScheme.surface, InnerShape)
            .padding(horizontal = Dimens.space14, vertical = Dimens.space12),
        verticalArrangement = Arrangement.spacedBy(Dimens.space8),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens.space12)) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = emphasize(title, status, SpanStyle(color = MaapTheme.colors.onWarningBadge), last = true),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = progressText,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            PrimaryButton(text = actionText, onClick = onAction, height = 44.dp, fontSize = 16.sp)
        }
        MaapProgressBar(
            progress = progress,
            modifier = Modifier.semantics { contentDescription = progressDescription },
            height = Dimens.space8,
            color = accent,
        )
    }
}

/** Inner corners: 16dp outer radius minus the 5dp start / 1dp other border widths. */
private val InnerShape = RoundedCornerShape(topStart = 11.dp, bottomStart = 11.dp, topEnd = 15.dp, bottomEnd = 15.dp)
