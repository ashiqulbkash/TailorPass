package com.tech_hunt.tailorpass.ui.screens.result

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.model.TShirtSizeRow
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_see_size_chart
import com.tech_hunt.tailorpass.resources.action_share
import com.tech_hunt.tailorpass.resources.brand_varies
import com.tech_hunt.tailorpass.resources.fit_with_label
import com.tech_hunt.tailorpass.resources.genji_basis
import com.tech_hunt.tailorpass.resources.genji_chest_range
import com.tech_hunt.tailorpass.resources.genji_compare
import com.tech_hunt.tailorpass.resources.genji_compare_hint
import com.tech_hunt.tailorpass.resources.genji_title
import com.tech_hunt.tailorpass.resources.genji_your_size
import com.tech_hunt.tailorpass.resources.ic_share
import com.tech_hunt.tailorpass.resources.ic_size_chart
import com.tech_hunt.tailorpass.resources.text_dot_pair
import com.tech_hunt.tailorpass.ui.components.BottomNav
import com.tech_hunt.tailorpass.ui.components.BottomTab
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.OutlinedPillButton
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.SavedMark
import com.tech_hunt.tailorpass.ui.components.TextBadge
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.components.WarningNote
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.formatDayMonth
import com.tech_hunt.tailorpass.ui.util.str
import com.tech_hunt.tailorpass.ui.util.useBanglaDigits
import com.tech_hunt.tailorpass.util.formatInches
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 9.4 Genji / T-shirt size: the suggested size in a big circle with what it is based on and the fit, the size next
 * to its neighbours (M | L | XL) with a tight / loose hint, "sizes differ between brands", share and size chart.
 */
@Composable
fun GenjiResultScreen(state: GenjiState, onTabSelect: (BottomTab) -> Unit, onAction: (GenjiAction) -> Unit) {
    MaapScaffold(
        topBar = {
            val pair = stringResource(Res.string.text_dot_pair, state.profileName, formatDayMonth(state.date))
            TopBar(
                title = stringResource(Res.string.genji_title),
                subtitle = stringResource(Res.string.text_dot_pair, pair, "").trimEnd(),
                onBack = { onAction(GenjiAction.Back) },
                height = 60.dp,
                subtitleTrailing = { SavedMark() },
            )
        },
        bottomBar = {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                PrimaryButton(
                    text = stringResource(Res.string.action_share),
                    onClick = { onAction(GenjiAction.Share) },
                    icon = painterResource(Res.drawable.ic_share),
                    iconSize = 22.dp,
                    height = 50.dp,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedPillButton(
                    text = stringResource(Res.string.action_see_size_chart),
                    onClick = { onAction(GenjiAction.OpenSizeChart) },
                    icon = painterResource(Res.drawable.ic_size_chart),
                    iconSize = 22.dp,
                    fontSize = 18.sp,
                    height = 50.dp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        contentPadding = PaddingValues(
            start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space4, bottom = Dimens.space12,
        ),
        bottomBarPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, bottom = Dimens.space12),
        bottomNav = { BottomNav(selected = BottomTab.Home, onSelect = onTabSelect) },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space12)) {
            SizeCard(state)
            if (state.compared.isNotEmpty()) CompareCard(state)
            WarningNote(stringResource(Res.string.brand_varies))
        }
    }
}

/** "আপনার সাইজ", 136dp circle with the 88sp size, "বুক ৪০", পুট ১৮", ঝুল ২৮" অনুযায়ী", fit chip. */
@Composable
private fun SizeCard(state: GenjiState) {
    val bangla = useBanglaDigits()
    MaapCard(
        modifier = Modifier.fillMaxWidth().semantics(mergeDescendants = true) {},
        contentPadding = PaddingValues(start = Dimens.space16, end = Dimens.space16, top = Dimens.space16, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(Dimens.space8),
        horizontalAlignment = Alignment.CenterHorizontally,
        shape = RoundedCornerShape(20.dp),
    ) {
        Text(
            text = stringResource(Res.string.genji_your_size),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box(
            Modifier
                .size(136.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = state.size ?: "—",
                style = MaterialTheme.typography.displayLarge,
                lineHeight = 88.sp,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            text = stringResource(
                Res.string.genji_basis,
                formatInches(state.chestInches, bangla),
                formatInches(state.shoulderInches, bangla),
                formatInches(state.lengthInches, bangla),
            ),
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.7.sp,
            color = MaapTheme.colors.textEmphasis,
            textAlign = TextAlign.Center,
        )
        TextBadge(
            text = stringResource(Res.string.fit_with_label, str(state.fit.labelKey)),
            background = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            padding = PaddingValues(horizontal = Dimens.space12, vertical = 2.dp),
        )
    }
}

/** "পাশের সাইজের সাথে তুলনা": the size between its neighbours, then the tight / loose hint. */
@Composable
private fun CompareCard(state: GenjiState) {
    MaapCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = Dimens.space14, vertical = Dimens.space12),
        verticalArrangement = Arrangement.spacedBy(Dimens.space8),
    ) {
        Text(
            text = stringResource(Res.string.genji_compare),
            modifier = Modifier.semantics { heading() },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space6)) {
            state.compared.forEach { row ->
                SizeTile(row, current = row.size == state.size, Modifier.weight(1f))
            }
        }
        val tighter = state.tighter
        val looser = state.looser
        if (tighter != null && looser != null) {
            Text(
                text = stringResource(Res.string.genji_compare_hint, tighter.size, looser.size),
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 19.6.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** 60dp tile: size (24 bold; 26 when current) over "বুক ৩৮–৪০"; the current one is filled primary. */
@Composable
private fun SizeTile(row: TShirtSizeRow, current: Boolean, modifier: Modifier) {
    val bangla = useBanglaDigits()
    val content = if (current) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier = modifier
            .height(60.dp)
            .background(
                if (current) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp),
            )
            .semantics(mergeDescendants = true) { selected = current },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = row.size,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = if (current) 26.sp else 24.sp,
            lineHeight = if (current) 30.sp else 28.sp,
            color = content,
        )
        Text(
            text = stringResource(
                Res.string.genji_chest_range,
                formatInches(row.chestMin, bangla),
                formatInches(row.chestMax, bangla),
            ),
            style = MaterialTheme.typography.bodySmall,
            fontSize = 13.sp,
            lineHeight = 15.sp,
            color = content,
        )
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun GenjiResultPreview() {
    MaapTheme(darkTheme = false) { GenjiResultScreen(GenjiState.Sample, {}) {} }
}
