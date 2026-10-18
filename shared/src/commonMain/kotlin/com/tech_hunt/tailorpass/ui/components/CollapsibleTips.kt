package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_chevron_down
import com.tech_hunt.tailorpass.resources.ic_chevron_up
import com.tech_hunt.tailorpass.resources.ic_warning
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import org.jetbrains.compose.resources.painterResource

/**
 * Collapsible tips ("ভুল এড়াতে", 7.3 / 7.4 / 7.8): 14dp corners, orange warning icon, 16 semibold title, chevron.
 * Closed: 48dp, white with a 1dp light border. Open: orange border, 46dp orange-tint header, bullet list below.
 */
@Composable
fun CollapsibleTips(
    title: String,
    tips: List<String>,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaapTheme.colors
    val shape = MaterialTheme.shapes.medium
    Column(
        modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, if (expanded) colors.warningBorder else MaterialTheme.colorScheme.outlineVariant, shape),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(min = if (expanded) 46.dp else Dimens.minTouchTarget)
                .background(if (expanded) colors.warningContainer else MaterialTheme.colorScheme.surface)
                .clickable(role = Role.Button, onClick = onToggle)
                .semantics { if (expanded) collapse { onToggle(); true } else expand { onToggle(); true } }
                .padding(start = Dimens.space14, end = Dimens.space12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
        ) {
            Icon(painterResource(Res.drawable.ic_warning), null, tint = colors.warning, modifier = Modifier.size(22.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                painterResource(if (expanded) Res.drawable.ic_chevron_up else Res.drawable.ic_chevron_down),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp),
            )
        }
        if (expanded) {
            BulletList(tips, PaddingValues(start = 22.dp, end = Dimens.space14, top = Dimens.space10, bottom = Dimens.space12))
        }
    }
}

/** Bulleted lines, 16sp, 6dp apart; the bullet hangs in the start padding like an HTML list. */
@Composable
private fun BulletList(lines: List<String>, padding: PaddingValues) {
    Column(Modifier.fillMaxWidth().padding(padding), verticalArrangement = Arrangement.spacedBy(Dimens.space6)) {
        lines.forEach { line ->
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                Text("•", style = MaterialTheme.typography.bodyMedium, lineHeight = 23.sp, color = MaapTheme.colors.textEmphasis)
                Text(line, style = MaterialTheme.typography.bodyMedium, lineHeight = 23.sp, color = MaapTheme.colors.textEmphasis)
            }
        }
    }
}
