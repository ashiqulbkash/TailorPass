package com.tech_hunt.tailorpass.ui.screens.manual

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_edit
import com.tech_hunt.tailorpass.resources.ic_warning
import com.tech_hunt.tailorpass.resources.summary_measure_now
import com.tech_hunt.tailorpass.resources.summary_missing
import com.tech_hunt.tailorpass.resources.summary_missing_banner
import com.tech_hunt.tailorpass.resources.summary_missing_bold
import com.tech_hunt.tailorpass.resources.summary_skip
import com.tech_hunt.tailorpass.resources.summary_title
import com.tech_hunt.tailorpass.resources.summary_verify
import com.tech_hunt.tailorpass.resources.text_dot_pair
import com.tech_hunt.tailorpass.resources.unit_inch
import com.tech_hunt.tailorpass.resources.value_inch
import com.tech_hunt.tailorpass.ui.components.InfoBanner
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.SectionCard
import com.tech_hunt.tailorpass.ui.components.TextBadge
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.screens.prep.garmentProfileSubtitle
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.theme.PillShape
import com.tech_hunt.tailorpass.ui.util.emphasize
import com.tech_hunt.tailorpass.ui.util.localDigits
import com.tech_hunt.tailorpass.ui.util.str
import com.tech_hunt.tailorpass.ui.util.useBanglaDigits
import com.tech_hunt.tailorpass.util.formatInches
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 7.7 All tape measurements at a glance, in tape order: value + edit per row, missing ones as orange rows with
 * "এখন মাপুন". Orange banner while something is missing. Review (primary) and "continue without it" (link, only when
 * something is missing) stay at the bottom. Values are in inches, like the entry screens (cm entry: plan.md Step 6).
 */
@Composable
fun ManualSummaryScreen(state: ManualState, onAction: (ManualSummaryAction) -> Unit) {
    val missing = state.missing
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.summary_title),
                subtitle = stringResource(
                    Res.string.text_dot_pair,
                    garmentProfileSubtitle(state.target),
                    stringResource(Res.string.unit_inch),
                ),
                onBack = { onAction(ManualSummaryAction.Back) },
            )
        },
        bottomBar = {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space4)) {
                PrimaryButton(
                    text = stringResource(Res.string.summary_verify),
                    onClick = { onAction(ManualSummaryAction.Verify) },
                    modifier = Modifier.fillMaxWidth(),
                )
                if (missing.isNotEmpty()) {
                    LinkButton(stringResource(Res.string.summary_skip)) { onAction(ManualSummaryAction.ContinueWithoutMissing) }
                }
            }
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space4, bottom = Dimens.space10),
        bottomBarPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, bottom = Dimens.space20),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space10)) {
            if (missing.isNotEmpty()) MissingBanner(missing.size)
            state.summaryGroups.forEach { group ->
                val rows: @Composable () -> Unit = {
                    group.indices.forEachIndexed { position, index ->
                        SummaryRow(state, index, showDivider = position < group.indices.lastIndex, onAction)
                    }
                }
                if (group.titleKey == null) {
                    MaapCard(
                        modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.large),
                        contentPadding = PaddingValues(),
                    ) { rows() }
                } else {
                    SectionCard(title = str(group.titleKey), trailing = null, modifier = Modifier.fillMaxWidth()) { rows() }
                }
            }
        }
    }
}

/** "১টি মাপ বাদ আছে। বাদ রাখলে দর্জিকে মেপে নিতে বলা হবে।" with the first sentence bold. */
@Composable
private fun MissingBanner(count: Int) {
    val n = localDigits(count.toString())
    InfoBanner(
        icon = painterResource(Res.drawable.ic_warning),
        iconTint = MaapTheme.colors.warning,
        background = MaapTheme.colors.warningContainer,
        border = MaapTheme.colors.warningBorder,
        iconSize = 22.dp,
        centerIcon = false,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = emphasize(
                stringResource(Res.string.summary_missing_banner, n),
                stringResource(Res.string.summary_missing_bold, n),
            ),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            lineHeight = 21.75.sp,
            color = MaapTheme.colors.onWarningContainerBody,
        )
    }
}

/**
 * One measurement: 16 semibold name · 22 bold value · 20dp edit icon, whole row opens the step (48dp min).
 * Missing: orange row (56dp min) with the name over a "বাদ আছে" badge and an orange "এখন মাপুন" button.
 */
@Composable
private fun SummaryRow(state: ManualState, index: Int, showDivider: Boolean, onAction: (ManualSummaryAction) -> Unit) {
    val key = state.steps[index]
    val value = state.values[index]
    val divider = MaapTheme.colors.divider
    val edit = { onAction(ManualSummaryAction.Edit(index)) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (value == null) Modifier.background(MaapTheme.colors.warningRow) else Modifier.clickable(onClick = edit))
            .drawBehind {
                if (showDivider) {
                    val y = size.height - 0.5.dp.toPx()
                    drawLine(divider, Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
                }
            }
            .heightIn(min = if (value == null) 56.dp else Dimens.minTouchTarget)
            .padding(start = Dimens.space14, end = Dimens.space10, top = if (value == null) 4.dp else 2.dp, bottom = if (value == null) 4.dp else 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = str(key.labelKey),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 19.2.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (value == null) {
                TextBadge(
                    text = stringResource(Res.string.summary_missing),
                    background = MaapTheme.colors.warningBadge,
                    contentColor = MaapTheme.colors.onWarningBadge,
                    padding = PaddingValues(horizontal = Dimens.space8),
                )
            }
        }
        if (value == null) {
            MeasureNowButton(edit)
        } else {
            Text(
                text = stringResource(Res.string.value_inch, formatInches(value, useBanglaDigits())),
                style = MaapTheme.numbers.listValue,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                painter = painterResource(Res.drawable.ic_edit),
                contentDescription = null,
                tint = MaapTheme.colors.textTertiary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

/** 44dp orange outlined pill, 15 bold (7.7 missing row). */
@Composable
private fun MeasureNowButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.height(44.dp),
        shape = PillShape,
        border = BorderStroke(1.5.dp, MaapTheme.colors.warning),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaapTheme.colors.warningRow,
            contentColor = MaapTheme.colors.onWarningBadge,
        ),
        contentPadding = PaddingValues(horizontal = Dimens.space14),
    ) {
        Text(
            text = stringResource(Res.string.summary_measure_now),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
    }
}

/** Underlined primary text link, 44dp high, 16 semibold ("বাদ রেখেই এগিয়ে যান"). */
@Composable
private fun LinkButton(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(44.dp),
        shape = PillShape,
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            maxLines = 1,
        )
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ManualSummaryPreview() {
    MaapTheme(darkTheme = false) { ManualSummaryScreen(ManualState.SampleSummary) {} }
}
