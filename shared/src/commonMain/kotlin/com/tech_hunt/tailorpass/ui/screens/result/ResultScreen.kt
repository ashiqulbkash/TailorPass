package com.tech_hunt.tailorpass.ui.screens.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_share_image
import com.tech_hunt.tailorpass.resources.action_share_pdf
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.resources.ic_image
import com.tech_hunt.tailorpass.resources.ic_pdf
import com.tech_hunt.tailorpass.resources.result_by_fit
import com.tech_hunt.tailorpass.resources.result_for_readymade
import com.tech_hunt.tailorpass.resources.result_list
import com.tech_hunt.tailorpass.resources.result_suggested_size
import com.tech_hunt.tailorpass.resources.saved_status
import com.tech_hunt.tailorpass.resources.text_dot_pair
import com.tech_hunt.tailorpass.resources.unit_cm
import com.tech_hunt.tailorpass.resources.unit_inch
import com.tech_hunt.tailorpass.resources.value_cm
import com.tech_hunt.tailorpass.resources.value_inch
import com.tech_hunt.tailorpass.ui.components.BottomNav
import com.tech_hunt.tailorpass.ui.components.BottomTab
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.NumberBadge
import com.tech_hunt.tailorpass.ui.components.OutlinedPillButton
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.SavedMark
import com.tech_hunt.tailorpass.ui.components.SegmentedToggle
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.diagram.NumberedDiagram
import com.tech_hunt.tailorpass.ui.screens.manual.MeasureUnit
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.theme.PillShape
import com.tech_hunt.tailorpass.ui.util.formatDate
import com.tech_hunt.tailorpass.ui.util.formatDayMonth
import com.tech_hunt.tailorpass.ui.util.localDigits
import com.tech_hunt.tailorpass.ui.util.str
import com.tech_hunt.tailorpass.ui.util.useBanglaDigits
import com.tech_hunt.tailorpass.util.formatInches
import com.tech_hunt.tailorpass.util.inchesToCmText
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 9.1 / 9.2 / 9.3 Result: "saved" status, numbered diagram + suggested ready-made size, the numbered list in the
 * tailor's words (inch / cm on 9.1), PDF and image share buttons, bottom nav. Salwar kameez (9.3) switches kameez /
 * salwar with tabs and shows "saved" in the subtitle instead. 9.2 is the dark theme.
 */
@Composable
fun ResultScreen(state: ResultState, onTabSelect: (BottomTab) -> Unit, onAction: (ResultAction) -> Unit) {
    MaapScaffold(
        topBar = {
            TopBar(
                title = str(state.titleKey),
                subtitle = subtitle(state),
                onBack = { onAction(ResultAction.Back) },
                height = 60.dp,
                subtitleTrailing = if (state.sectioned) ({ SavedMark() }) else null,
            )
        },
        bottomBar = {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                PrimaryButton(
                    text = stringResource(Res.string.action_share_pdf),
                    onClick = { onAction(ResultAction.SharePdf) },
                    icon = painterResource(Res.drawable.ic_pdf),
                    iconSize = 22.dp,
                    height = Dimens.buttonHeightCompact,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedPillButton(
                    text = stringResource(Res.string.action_share_image),
                    onClick = { onAction(ResultAction.ShareImage) },
                    icon = painterResource(Res.drawable.ic_image),
                    iconSize = 22.dp,
                    fontSize = 18.sp,
                    height = Dimens.buttonHeightCompact,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        contentPadding = PaddingValues(
            start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space4, bottom = Dimens.space10,
        ),
        bottomBarPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, bottom = Dimens.space12),
        bottomNav = { BottomNav(selected = BottomTab.Home, onSelect = onTabSelect) },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space10)) {
            if (state.sectioned) {
                SegmentedToggle(
                    options = state.sections.map {
                        stringResource(Res.string.text_dot_pair, str(it.titleKey.orEmpty()), localDigits(it.rows.size.toString()))
                    },
                    selectedIndex = state.tab,
                    onSelect = { onAction(ResultAction.SelectTab(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    height = Dimens.minTouchTarget,
                    fontSize = 17.sp,
                    fill = true,
                )
            } else {
                SavedChip()
            }
            state.currentSection?.let { section ->
                DiagramCard(state, section)
                ListCard(state, section, onAction)
            }
        }
    }
}

/** "রাহাত · ১৬ অক্টোবর ২০২৬"; with tabs (9.3) "ফারহানা · ১৬ অক্টোবর ·" before the saved mark. */
@Composable
private fun subtitle(state: ResultState): String =
    if (state.sectioned) {
        val pair = stringResource(Res.string.text_dot_pair, state.target.profileName, formatDayMonth(state.date))
        stringResource(Res.string.text_dot_pair, pair, "").trimEnd()
    } else {
        stringResource(Res.string.text_dot_pair, state.target.profileName, formatDate(state.date))
    }

/** 9.1 "✓ সেভ হয়েছে · ফোনেই সংরক্ষিত": 30dp green pill, 18dp check, 15 semibold. */
@Composable
private fun SavedChip() {
    Row(
        modifier = Modifier
            .height(30.dp)
            .background(MaapTheme.colors.successContainer, PillShape)
            .padding(start = Dimens.space10, end = Dimens.space14)
            .semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space6),
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_check),
            contentDescription = null,
            tint = MaapTheme.colors.onSuccessContainer,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = stringResource(Res.string.saved_status),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaapTheme.colors.onSuccessContainer,
            maxLines = 1,
        )
    }
}

/** Diagram card (176dp; 180 with tabs) with the suggested-size box filling the rest. */
@Composable
private fun DiagramCard(state: ResultState, section: ResultSection) {
    val tall = state.sectioned
    MaapCard(
        modifier = Modifier.fillMaxWidth().height(if (tall) 180.dp else 176.dp),
        contentPadding = if (tall) PaddingValues(start = 6.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
        else PaddingValues(start = 2.dp, end = 10.dp, top = 6.dp, bottom = 6.dp),
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(if (tall) Dimens.space6 else Dimens.space4),
        ) {
            section.diagram?.let { spec ->
                NumberedDiagram(
                    spec = spec,
                    modifier = if (tall) Modifier.size(112.dp, 168.dp) else Modifier.size(134.dp, 164.dp),
                    badgeRadius = if (tall) 15f else 10f,
                    badgeFontSize = if (tall) 18f else 13f,
                )
            }
            SizeBox(state, Modifier.weight(1f))
        }
    }
}

/** "সাজেস্ট করা সাইজ" / big size / "রেডিমেড পাঞ্জাবি কিনতে" / "রেগুলার ফিট অনুযায়ী" on primary container. */
@Composable
private fun SizeBox(state: ResultState, modifier: Modifier) {
    val secondary = MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp))
            .padding(horizontal = Dimens.space6, vertical = Dimens.space10)
            .semantics(mergeDescendants = true) {},
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CenteredText(stringResource(Res.string.result_suggested_size), 14.sp, secondary)
        SizeLetter(localDigits(state.size ?: "—"), MaterialTheme.typography.displayMedium.fontSize, 56.7.dp, MaterialTheme.colorScheme.primary)
        CenteredText(stringResource(Res.string.result_for_readymade, str(state.readymadeKey)), 14.sp, MaapTheme.colors.textEmphasis)
        CenteredText(
            stringResource(Res.string.result_by_fit, str(state.fit.labelKey)),
            13.sp,
            secondary,
            Modifier.padding(top = 2.dp),
        )
    }
}

/**
 * A big size letter ("L") in a [height] slot, like the design's CSS line-height: Hind Siliguri's line is much taller
 * than the Latin letter (room for Bangla vowel signs), so the text is laid out unbounded and centred in the slot.
 */
@Composable
internal fun SizeLetter(text: String, size: TextUnit, height: Dp, color: Color) {
    Box(Modifier.height(height), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            modifier = Modifier.wrapContentHeight(unbounded = true),
            style = MaterialTheme.typography.displayMedium,
            fontSize = size,
            color = color,
            maxLines = 1,
        )
    }
}

@Composable
private fun CenteredText(text: String, size: TextUnit, color: Color, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        fontSize = size,
        lineHeight = size * 1.3f,
        color = color,
        textAlign = TextAlign.Center,
    )
}

/**
 * Numbered list card. 9.1: "মাপের তালিকা" + inch/cm toggle header, 28dp rows with a line above each.
 * 9.3 (tabs): no header, 26dp rows with a line below each, one size smaller text.
 */
@Composable
private fun ListCard(state: ResultState, section: ResultSection, onAction: (ResultAction) -> Unit) {
    val compact = state.sectioned
    MaapCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = Dimens.space12, end = Dimens.space12, top = Dimens.space4, bottom = if (compact) Dimens.space4 else Dimens.space6),
    ) {
        if (!compact) {
            Row(Modifier.fillMaxWidth().height(50.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Res.string.result_list),
                    modifier = Modifier.weight(1f).semantics { heading() },
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                SegmentedToggle(
                    options = listOf(stringResource(Res.string.unit_inch), stringResource(Res.string.unit_cm)),
                    selectedIndex = state.unit.ordinal,
                    onSelect = { onAction(ResultAction.SetUnit(MeasureUnit.entries[it])) },
                    segmentWidth = 64.dp,
                )
            }
        }
        section.rows.forEachIndexed { i, row ->
            ResultListRow(
                number = localDigits((i + 1).toString()),
                name = str(row.key.labelKey),
                value = resultValue(row.inches, state.unit),
                compact = compact,
            )
        }
    }
}

/** One numbered row: 20dp badge, name, bold value; [compact] = 9.3 sizes and the line below instead of above. */
@Composable
private fun ResultListRow(number: String, name: String, value: String, compact: Boolean) {
    val line = MaterialTheme.colorScheme.surfaceVariant
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (compact) 26.dp else 28.dp)
            .drawBehind {
                val y = if (compact) size.height - 0.5.dp.toPx() else 0.5.dp.toPx()
                drawLine(line, Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
            }
            .semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
    ) {
        NumberBadge(number, fontSize = if (compact) 11.sp else 12.sp)
        Text(
            text = name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = if (compact) 15.sp else 16.sp,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
        Text(
            text = value,
            style = MaapTheme.numbers.compactValue,
            fontSize = if (compact) 17.sp else 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
    }
}

/** A value in the chosen unit: "২৪½"" or "৬২.২ সেমি"; a skipped one (the tailor measures it) is "—". */
@Composable
internal fun resultValue(inches: Float?, unit: MeasureUnit): String {
    if (inches == null) return "—"
    val bangla = useBanglaDigits()
    return when (unit) {
        MeasureUnit.Inch -> stringResource(Res.string.value_inch, formatInches(inches, bangla))
        MeasureUnit.Cm -> stringResource(Res.string.value_cm, inchesToCmText(inches, bangla))
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ResultPreview() {
    MaapTheme(darkTheme = false) { ResultScreen(ResultState.SamplePanjabi, {}) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ResultDarkPreview() {
    MaapTheme(darkTheme = true) { ResultScreen(ResultState.SamplePanjabi, {}) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ResultSalwarKameezPreview() {
    MaapTheme(darkTheme = false) { ResultScreen(ResultState.SampleSalwarKameez, {}) {} }
}
