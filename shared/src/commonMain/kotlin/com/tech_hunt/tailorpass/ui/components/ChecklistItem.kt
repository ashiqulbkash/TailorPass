package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import org.jetbrains.compose.resources.painterResource

private val TileShape = RoundedCornerShape(12.dp)

/**
 * Checklist row (4.1/4.2): 44dp icon tile · 17 semibold title over a 14 subtitle · 28dp checkbox. Min 76dp high.
 * The whole row toggles. Ticked: blue tile + green box; not ticked: grey tile + empty box.
 */
@Composable
fun ChecklistItem(
    icon: Painter,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 76.dp)
            .toggleable(value = checked, role = Role.Checkbox, onValueChange = onCheckedChange)
            .padding(horizontal = Dimens.space14, vertical = Dimens.space10),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
    ) {
        IconTile(
            icon = icon,
            tint = if (checked) MaterialTheme.colorScheme.primary else MaapTheme.colors.textTertiary,
            background = if (checked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            size = 44.dp,
            iconSize = 26.dp,
            shape = TileShape,
        )
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        CheckBox(checked)
    }
}

/** 28dp box, 8dp corners: green with a white check, or white with a 2dp grey border. */
@Composable
private fun CheckBox(checked: Boolean) {
    val shape = MaterialTheme.shapes.extraSmall
    if (checked) {
        Box(
            Modifier.size(28.dp).background(MaapTheme.colors.success, shape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = null,
                tint = MaapTheme.colors.onSuccess,
                modifier = Modifier.size(18.dp),
            )
        }
    } else {
        Box(
            Modifier
                .size(28.dp)
                .background(MaterialTheme.colorScheme.surface, shape)
                .border(2.dp, MaapTheme.colors.checkboxBorder, shape),
        )
    }
}
