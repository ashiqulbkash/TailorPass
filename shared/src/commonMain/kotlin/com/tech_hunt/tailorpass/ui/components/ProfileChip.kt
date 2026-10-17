package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_chevron_down
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.PillShape
import org.jetbrains.compose.resources.painterResource

/** Round avatar with the profile's initial ("রা"): 17 bold letter. 40dp in the chip, 44dp in the profile sheet. */
@Composable
fun Avatar(
    initial: String,
    background: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    IconTile(background = background, modifier = modifier, size = size) {
        Text(
            text = initial,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            maxLines = 1,
        )
    }
}

/**
 * Profile switcher (Home top start): 56dp pill, 1dp border · 40dp avatar · small label over the 19 bold name · chevron.
 * [contentDescription] replaces the inner texts for screen readers ("Change profile, now Rahat").
 */
@Composable
fun ProfileChip(
    name: String,
    initial: String,
    label: String,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(Dimens.buttonHeight)
            .clip(PillShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, PillShape)
            .clickable(onClick = onClick)
            .clearAndSetSemantics {
                this.contentDescription = contentDescription
                role = Role.Button
            }
            .padding(start = Dimens.space8, end = Dimens.space14),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
    ) {
        Avatar(
            initial = initial,
            background = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp,
                lineHeight = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 19.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
        }
        Icon(
            painter = painterResource(Res.drawable.ic_chevron_down),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp),
        )
    }
}
