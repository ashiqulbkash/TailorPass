package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * One choice in a group of cards (gender, fit; later profile list, presets, restore options).
 * Selected: primary container + 2dp primary border. Not selected: surface + 1dp [unselectedBorder].
 */
@Composable
fun Modifier.selectableCard(
    selected: Boolean,
    onClick: () -> Unit,
    shape: Shape = MaterialTheme.shapes.medium,
    unselectedBorder: Color = MaterialTheme.colorScheme.outline,
): Modifier = this
    .clip(shape)
    .background(if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
    .border(
        if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, unselectedBorder),
        shape,
    )
    .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)

/** Accessibility label, only when [text] is given. */
internal fun Modifier.optionalContentDescription(text: String?): Modifier =
    if (text != null) semantics { contentDescription = text } else this
