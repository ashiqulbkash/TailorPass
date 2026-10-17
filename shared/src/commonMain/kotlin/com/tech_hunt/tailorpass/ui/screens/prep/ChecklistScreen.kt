package com.tech_hunt.tailorpass.ui.screens.prep

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_continue
import com.tech_hunt.tailorpass.resources.checklist_locked_hint
import com.tech_hunt.tailorpass.resources.checklist_progress
import com.tech_hunt.tailorpass.resources.checklist_title
import com.tech_hunt.tailorpass.ui.components.ChecklistItem
import com.tech_hunt.tailorpass.ui.components.InfoBanner
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.drawable
import com.tech_hunt.tailorpass.ui.util.localDigits
import com.tech_hunt.tailorpass.ui.util.str
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 4.1 / 4.2 Preparation checklist. "Continue" is enabled only when every item is ticked. */
@Composable
fun ChecklistScreen(state: ChecklistState, onAction: (ChecklistAction) -> Unit) {
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.checklist_title),
                subtitle = garmentProfileSubtitle(state.target),
                onBack = { onAction(ChecklistAction.Back) },
                height = 68.dp,
            )
        },
        bottomBar = {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                PrimaryButton(
                    text = stringResource(Res.string.action_continue),
                    onClick = { onAction(ChecklistAction.Continue) },
                    enabled = state.canContinue,
                    modifier = Modifier.fillMaxWidth(),
                )
                if (!state.canContinue) {
                    Text(
                        text = stringResource(Res.string.checklist_locked_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space8, bottom = Dimens.space12),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space12)) {
            // Clipped like the design (overflow: hidden), so the last row's line stays inside the corners.
            MaapCard(modifier = Modifier.clip(MaterialTheme.shapes.large), contentPadding = PaddingValues()) {
                state.items.forEachIndexed { index, item ->
                    ChecklistItem(
                        icon = painterResource(drawable(item.iconName)),
                        title = str(item.titleKey),
                        subtitle = str(item.subtitleKey),
                        checked = index in state.checked,
                        onCheckedChange = { onAction(ChecklistAction.Toggle(index)) },
                    )
                    // The design draws a line under every row, the last one too.
                    HorizontalDivider(thickness = 1.dp, color = MaapTheme.colors.divider)
                }
            }
            state.tip?.let { tip ->
                InfoBanner(
                    icon = painterResource(drawable(tip.iconName)),
                    iconTint = MaterialTheme.colorScheme.primary,
                    background = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth(),
                    iconSize = 26.dp,
                    gap = Dimens.space12,
                    centerIcon = false,
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = str(tip.titleKey),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 19.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = str(tip.bodyKey),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 15.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
            Text(
                text = stringResource(
                    Res.string.checklist_progress,
                    localDigits(state.items.size.toString()),
                    localDigits(state.checkedCount.toString()),
                ),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaapTheme.colors.textEmphasis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite },
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ChecklistScreenPreview() {
    MaapTheme(darkTheme = false) { ChecklistScreen(ChecklistState.Sample) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ChecklistFemalePreview() {
    MaapTheme(darkTheme = false) { ChecklistScreen(ChecklistState.SampleFemale) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ChecklistAllTickedPreview() {
    MaapTheme(darkTheme = false) { ChecklistScreen(ChecklistState.Sample.copy(checked = setOf(0, 1, 2, 3, 4))) {} }
}
