package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.resources.ic_chevron_right
import com.tech_hunt.tailorpass.resources.ic_people
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import org.jetbrains.compose.resources.painterResource

/** Row state in the tape list: done (value + green check), current ("এখন"), pending ("বাকি" + chevron). */
enum class StepRowState { Done, Current, Pending }

/**
 * One measurement in the tape list (7.2): 1dp top divider, min 52dp, 4 / 12 padding · 30dp number tile
 * (9dp corners) · label (+ optional "helper needed" badge under it) · trailing status. The whole row is tappable.
 * Done: blue-tint tile, 16 semibold label, 18 bold value + 22dp green check. Current: filled primary tile,
 * bold label, "এখন" badge. Pending: grey tile and grey 16 medium label, "বাকি" + chevron.
 */
@Composable
fun ManualStepRow(
    number: String,
    label: String,
    state: StepRowState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    value: String? = null,
    statusText: String,
    helperBadge: String? = null,
) {
    val scheme = MaterialTheme.colorScheme
    val colors = MaapTheme.colors
    Column(modifier.fillMaxWidth()) {
        HorizontalDivider(thickness = 1.dp, color = colors.divider)
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(min = Dimens.listRowMinHeight)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(horizontal = Dimens.space12, vertical = Dimens.space4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
        ) {
            val (tileBg, tileFg) = when (state) {
                StepRowState.Done -> scheme.primaryContainer to scheme.primary
                StepRowState.Current -> scheme.primary to scheme.onPrimary
                StepRowState.Pending -> scheme.surfaceVariant to colors.textTertiary
            }
            IconTile(background = tileBg, size = 30.dp, shape = RoundedCornerShape(9.dp)) {
                Text(number, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = tileFg)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp,
                    fontWeight = when (state) {
                        StepRowState.Done -> FontWeight.SemiBold
                        StepRowState.Current -> FontWeight.Bold
                        StepRowState.Pending -> FontWeight.Medium
                    },
                    color = if (state == StepRowState.Pending) scheme.onSurfaceVariant else scheme.onSurface,
                )
                if (helperBadge != null) {
                    TextBadge(
                        text = helperBadge,
                        background = colors.neutralBadge,
                        contentColor = colors.onNeutralBadge,
                        icon = painterResource(Res.drawable.ic_people),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        padding = PaddingValues(horizontal = Dimens.space8),
                    )
                }
            }
            when (state) {
                StepRowState.Done -> Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.space6),
                ) {
                    if (value != null) {
                        Text(value, style = MaapTheme.numbers.compactValue, color = scheme.onSurface)
                    }
                    IconTile(
                        icon = painterResource(Res.drawable.ic_check),
                        tint = colors.onSuccess,
                        background = colors.success,
                        size = 22.dp,
                        iconSize = 14.dp,
                    )
                }
                StepRowState.Current -> TextBadge(
                    text = statusText,
                    background = scheme.primary,
                    contentColor = scheme.onPrimary,
                    padding = PaddingValues(horizontal = Dimens.space10, vertical = 2.dp),
                )
                StepRowState.Pending -> Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(statusText, style = MaterialTheme.typography.bodySmall, color = colors.textTertiary)
                    Icon(
                        painterResource(Res.drawable.ic_chevron_right),
                        contentDescription = null,
                        tint = colors.textTertiary,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}
