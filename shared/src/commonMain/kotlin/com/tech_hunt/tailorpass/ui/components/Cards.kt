package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tech_hunt.tailorpass.ui.theme.Dimens

/**
 * Content card used on most screens: 16dp corners, 1dp light border (#D5DAE1), surface background.
 * Default padding 14 / 16 (privacy note 1.1, reasons 6.3). [shape]: 20dp corners on the genji size card (9.4).
 */
@Composable
fun MaapCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = Dimens.space16, vertical = Dimens.space14),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    shape: Shape = MaterialTheme.shapes.large,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, shape)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .padding(contentPadding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content,
    )
}

/**
 * List card with a grey header (7.2 "শরীরের মাপ · ৬টি"): MaapCard without padding, 38dp header on surfaceVariant
 * with a 16 bold title and a 14 grey count at the end; rows below draw their own top divider.
 * 8.4 "কামিজ · ১০টি মাপ": 44dp header, 22dp primary [icon] before an 18 bold title, regular-weight count.
 */
@Composable
fun SectionCard(
    title: String,
    trailing: String?,
    modifier: Modifier = Modifier,
    headerHeight: Dp = 38.dp,
    icon: Painter? = null,
    titleStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
    trailingWeight: FontWeight = FontWeight.Medium,
    content: @Composable ColumnScope.() -> Unit,
) {
    MaapCard(modifier = modifier.clip(MaterialTheme.shapes.large), contentPadding = PaddingValues()) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = Dimens.space14)
                .semantics { heading() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                }
                Text(title, style = titleStyle, color = MaterialTheme.colorScheme.onSurface)
            }
            if (trailing != null) {
                Text(
                    trailing,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = trailingWeight,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        content()
    }
}
