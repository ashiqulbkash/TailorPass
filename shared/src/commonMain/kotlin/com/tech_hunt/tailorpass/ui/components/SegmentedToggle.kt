package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.PillShape
import org.jetbrains.compose.resources.painterResource

/**
 * Pill toggle (feet-inch / cm, later inch / cm on Result): 44dp high, 1dp outline border.
 * Selected segment: primary background, 15 bold white; others: surface, 15 medium.
 * segmentWidth: fixed width of every segment (72dp on 7.1, 64dp on 9.1); null sizes each segment to its text.
 * fill = true: equal segments across the given width (kameez / salwar tabs 9.3, 48dp high, 17sp; later 11.1).
 */
@Composable
fun SegmentedToggle(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    segmentWidth: Dp? = null,
    height: Dp = Dimens.segmentedHeight,
    fontSize: TextUnit = 15.sp,
    fill: Boolean = false,
) {
    Row(
        modifier = modifier
            .height(height)
            .clip(PillShape)
            .border(1.dp, MaterialTheme.colorScheme.outline, PillShape)
            .selectableGroup()
            .optionalContentDescription(contentDescription),
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .then(
                        when {
                            fill -> Modifier.weight(1f)
                            segmentWidth != null -> Modifier.width(segmentWidth)
                            else -> Modifier
                        },
                    )
                    .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                    .selectable(selected = selected, role = Role.RadioButton, onClick = { onSelect(index) })
                    .then(
                        if (fill || segmentWidth != null) Modifier
                        else Modifier.padding(horizontal = if (selected) Dimens.space14 else Dimens.space16),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = fontSize,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                )
            }
        }
    }
}

/**
 * One choice of a two-or-more card group (male / female): 56dp high, 14dp corners.
 * Selected: 2dp primary border, primary container, check icon + 18 bold primary text.
 * Not selected: 1dp outline border, surface, 18 medium text.
 */
@Composable
fun SelectCard(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(Dimens.buttonHeight)
            .selectableCard(selected, onClick),
        horizontalArrangement = Arrangement.spacedBy(Dimens.space8, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selected) {
            Icon(
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 18.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
    }
}
