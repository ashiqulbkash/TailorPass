package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_home
import com.tech_hunt.tailorpass.resources.ic_settings
import com.tech_hunt.tailorpass.resources.ic_size_chart
import com.tech_hunt.tailorpass.resources.tab_home
import com.tech_hunt.tailorpass.resources.tab_settings
import com.tech_hunt.tailorpass.resources.tab_size_chart
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.theme.PillShape
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** The three bottom-nav tabs (Home, Size chart, Settings). */
enum class BottomTab(val label: StringResource, val icon: DrawableResource) {
    Home(Res.string.tab_home, Res.drawable.ic_home),
    SizeChart(Res.string.tab_size_chart, Res.drawable.ic_size_chart),
    Settings(Res.string.tab_settings, Res.drawable.ic_settings),
}

/**
 * Bottom navigation (tab screens and Result only): 80dp, surface, 1dp top border, three equal tabs.
 * Tab: 64×32 pill indicator (light blue when selected) with a 24dp icon, 14 label under it.
 * Draws its background under the system navigation bar and pads its content above it.
 */
@Composable
fun BottomNav(
    selected: BottomTab,
    onSelect: (BottomTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val border = MaapTheme.colors.navDivider
    Column(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .drawBehind { drawLine(border, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx()) }
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)),
    ) {
        Row(Modifier.fillMaxWidth().height(Dimens.bottomNavHeight).selectableGroup()) {
            BottomTab.entries.forEach { tab ->
                BottomNavItem(tab, tab == selected, { onSelect(tab) }, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun BottomNavItem(tab: BottomTab, selected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .selectable(selected = selected, role = Role.Tab, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.space4, Alignment.CenterVertically),
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.navIndicatorWidth, Dimens.navIndicatorHeight)
                .then(if (selected) Modifier.background(MaterialTheme.colorScheme.primaryContainer, PillShape) else Modifier),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(tab.icon),
                contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(Dimens.iconSize),
            )
        }
        Text(
            text = stringResource(tab.label),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
        )
    }
}
