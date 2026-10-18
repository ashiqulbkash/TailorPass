package com.tech_hunt.tailorpass.ui.screens.manual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.model.MeasurementKey
import com.tech_hunt.tailorpass.model.NeckDepthPreset
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_arrow_forward
import com.tech_hunt.tailorpass.resources.ic_list
import com.tech_hunt.tailorpass.resources.ic_people
import com.tech_hunt.tailorpass.resources.ic_warning
import com.tech_hunt.tailorpass.resources.manual_all_measures
import com.tech_hunt.tailorpass.resources.manual_avoid_mistakes
import com.tech_hunt.tailorpass.resources.manual_decrease
import com.tech_hunt.tailorpass.resources.manual_helper_banner
import com.tech_hunt.tailorpass.resources.manual_increase
import com.tech_hunt.tailorpass.resources.manual_later
import com.tech_hunt.tailorpass.resources.manual_next
import com.tech_hunt.tailorpass.resources.manual_step_desc
import com.tech_hunt.tailorpass.resources.manual_title
import com.tech_hunt.tailorpass.resources.manual_type_hint
import com.tech_hunt.tailorpass.resources.manual_value_desc
import com.tech_hunt.tailorpass.resources.outlier_body
import com.tech_hunt.tailorpass.resources.outlier_confirm
import com.tech_hunt.tailorpass.resources.outlier_remeasure
import com.tech_hunt.tailorpass.resources.outlier_title
import com.tech_hunt.tailorpass.resources.pref_badge
import com.tech_hunt.tailorpass.resources.unit_inch
import com.tech_hunt.tailorpass.resources.value_inch
import com.tech_hunt.tailorpass.ui.components.BottomSheetContent
import com.tech_hunt.tailorpass.ui.components.CollapsibleTips
import com.tech_hunt.tailorpass.ui.components.IconTile
import com.tech_hunt.tailorpass.ui.components.InfoBanner
import com.tech_hunt.tailorpass.ui.components.MaapBottomSheet
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapIconButton
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.NeutralPillButton
import com.tech_hunt.tailorpass.ui.components.NumberStepperField
import com.tech_hunt.tailorpass.ui.components.OutlinedPillButton
import com.tech_hunt.tailorpass.ui.components.PresetPictureCard
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.SheetOverlayPreview
import com.tech_hunt.tailorpass.ui.components.StepSegments
import com.tech_hunt.tailorpass.ui.components.StepperHint
import com.tech_hunt.tailorpass.ui.components.TextBadge
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.components.TopBarChip
import com.tech_hunt.tailorpass.ui.diagram.NecklineFigure
import com.tech_hunt.tailorpass.ui.diagram.TapeDiagram
import com.tech_hunt.tailorpass.ui.diagram.TapeDiagramView
import com.tech_hunt.tailorpass.ui.screens.prep.garmentProfileSubtitle
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.emphasize
import com.tech_hunt.tailorpass.ui.util.localDigits
import com.tech_hunt.tailorpass.ui.util.str
import com.tech_hunt.tailorpass.ui.util.useBanglaDigits
import com.tech_hunt.tailorpass.util.displayInchInput
import com.tech_hunt.tailorpass.util.formatInches
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 7.3 / 7.4 / 7.10 One tape measurement, laid out by the current key: body measurement (step bar, helper banner,
 * diagram, how-to, "avoid mistakes", number) or preference measurement (small diagram + question, picture cards,
 * number). 7.5: the unusual-value warning is a sheet over this screen.
 */
@Composable
fun ManualEntryScreen(state: ManualState, onAction: (ManualEntryAction) -> Unit) {
    val key = state.currentKey ?: return
    ManualEntryLayout(state, key, onAction)
    if (state.warningOpen) {
        MaapBottomSheet(
            onDismiss = { onAction(ManualEntryAction.Remeasure) },
            contentPadding = WarningSheetPadding,
            verticalArrangement = Arrangement.spacedBy(Dimens.space14),
        ) { OutlierWarningBody(state, key, onAction) }
    }
}

@Composable
private fun ManualEntryLayout(state: ManualState, key: MeasurementKey, onAction: (ManualEntryAction) -> Unit) {
    MaapScaffold(
        topBar = { EntryTopBar(state, onAction) },
        bottomBar = {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space10)) {
                OutlinedPillButton(
                    text = stringResource(Res.string.manual_later),
                    onClick = { onAction(ManualEntryAction.Later) },
                    modifier = Modifier.weight(1f),
                )
                PrimaryButton(
                    text = stringResource(Res.string.manual_next),
                    onClick = { onAction(ManualEntryAction.Next) },
                    enabled = state.canGoNext,
                    trailingIcon = painterResource(Res.drawable.ic_arrow_forward),
                    modifier = Modifier.weight(1.5f),
                )
            }
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space4, bottom = Dimens.space16),
    ) {
        if (key.isPreference) PreferenceEntry(state, key, onAction) else BodyEntry(state, key, onAction)
    }
}

@Composable
private fun EntryTopBar(state: ManualState, onAction: (ManualEntryAction) -> Unit) {
    val step = localDigits((state.current + 1).toString())
    val count = localDigits(state.count.toString())
    TopBar(
        title = stringResource(Res.string.manual_title),
        subtitle = garmentProfileSubtitle(state.target),
        onBack = { onAction(ManualEntryAction.Back) },
        endPadding = Dimens.space6,
    ) {
        val stepDescription = stringResource(Res.string.manual_step_desc, step, count)
        TopBarChip(text = "$step/$count", modifier = Modifier.semantics { contentDescription = stepDescription })
        MaapIconButton(
            icon = painterResource(Res.drawable.ic_list),
            contentDescription = stringResource(Res.string.manual_all_measures),
            onClick = { onAction(ManualEntryAction.OpenList) },
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

/**
 * 7.3 / 7.4. A key that needs a helper (7.4) shows the helper banner, so everything below is a little tighter:
 * 150dp diagram, 68dp number field and no keyboard icon in the hint (168 / 72 / icon otherwise).
 */
@Composable
private fun BodyEntry(state: ManualState, key: MeasurementKey, onAction: (ManualEntryAction) -> Unit) {
    val compact = key.needsHelper
    val guide = state.guide
    Column(verticalArrangement = Arrangement.spacedBy(if (compact) Dimens.space10 else Dimens.space12)) {
        StepSegments(count = state.count, filled = state.current + 1)
        if (key.needsHelper) {
            InfoBanner(
                icon = painterResource(Res.drawable.ic_people),
                iconTint = MaterialTheme.colorScheme.primary,
                background = MaterialTheme.colorScheme.primaryContainer,
                iconSize = 22.dp,
                verticalPadding = Dimens.space8,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(Res.string.manual_helper_banner),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        guide.diagram?.let { diagram ->
            DiagramCard(diagram, cardHeight = if (compact) 150.dp else 168.dp, Modifier.fillMaxWidth())
        }
        Column(verticalArrangement = Arrangement.spacedBy(if (compact) 2.dp else Dimens.space4)) {
            MeasureTitle(str(key.labelKey), MaterialTheme.typography.headlineMedium.fontSize, 32.sp)
            guide.howtoKey?.let {
                Text(str(it), style = MaterialTheme.typography.bodyMedium, color = MaapTheme.colors.textEmphasis)
            }
        }
        if (guide.tipKeys.isNotEmpty()) {
            CollapsibleTips(
                title = stringResource(Res.string.manual_avoid_mistakes),
                tips = guide.tipKeys.map { str(it) },
                expanded = state.tipsOpen,
                onToggle = { onAction(ManualEntryAction.ToggleTips) },
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(if (compact) Dimens.space4 else Dimens.space6)) {
            EntryField(state, key, onAction, if (compact) 68.dp else Dimens.numberFieldHeight, if (compact) 42.sp else 44.sp)
            StepperHint(stringResource(Res.string.manual_type_hint), showIcon = !compact)
        }
    }
}

/** 7.10 (and other preference keys): small diagram + badge, title, question · picture cards · number. */
@Composable
private fun PreferenceEntry(state: ManualState, key: MeasurementKey, onAction: (ManualEntryAction) -> Unit) {
    val guide = state.guide
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.space12)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens.space12)) {
            guide.diagram?.let { diagram ->
                MaapCard(modifier = Modifier.size(120.dp, 128.dp), contentPadding = PaddingValues()) {
                    Box(Modifier.fillMaxWidth().height(128.dp), contentAlignment = Alignment.Center) {
                        TapeDiagramView(diagram, Modifier.size(108.dp, 116.dp))
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space4)) {
                TextBadge(
                    text = "✎ " + stringResource(Res.string.pref_badge),
                    background = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                )
                MeasureTitle(str(key.labelKey), MaterialTheme.typography.headlineSmall.fontSize, 27.sp)
                guide.howtoKey?.let {
                    Text(
                        str(it),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = MaapTheme.colors.textEmphasis,
                    )
                }
            }
        }
        if (state.presets.isNotEmpty()) {
            val useBangla = useBanglaDigits()
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                state.presets.forEach { preset ->
                    val selected = state.selectedPreset == preset
                    PresetPictureCard(
                        label = str(preset.labelKey),
                        value = stringResource(Res.string.value_inch, formatInches(preset.sampleInches, useBangla)),
                        selected = selected,
                        onClick = { onAction(ManualEntryAction.PickPreset(preset)) },
                        modifier = Modifier.weight(1f),
                    ) {
                        NecklineFigure(preset.pictureDepth, selected, Modifier.size(64.dp, 52.dp))
                    }
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space6)) {
            EntryField(state, key, onAction, 68.dp, 42.sp)
            StepperHint(stringResource(Res.string.manual_type_hint), showIcon = false)
        }
    }
}

/** Curve depth (control-point y) of each neck-depth picture in the 64×52 viewport (7.10). */
private val NeckDepthPreset.pictureDepth: Float
    get() = when (this) {
        NeckDepthPreset.Low -> 26f
        NeckDepthPreset.Medium -> 36f
        NeckDepthPreset.High -> 48f
    }

@Composable
private fun MeasureTitle(text: String, fontSize: TextUnit, lineHeight: TextUnit) {
    Text(
        text = text,
        modifier = Modifier.semantics { heading() },
        style = MaterialTheme.typography.headlineMedium,
        fontSize = fontSize,
        lineHeight = lineHeight,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun DiagramCard(diagram: TapeDiagram, cardHeight: Dp, modifier: Modifier = Modifier) {
    MaapCard(modifier = modifier.height(cardHeight), contentPadding = PaddingValues()) {
        Box(Modifier.fillMaxWidth().height(cardHeight), contentAlignment = Alignment.Center) {
            val size = when (diagram) {
                TapeDiagram.Sleeve -> Modifier.size(142.dp, 140.dp)
                TapeDiagram.Chest, TapeDiagram.Waist, TapeDiagram.NeckDepth -> Modifier.size(130.dp, 156.dp)
            }
            TapeDiagramView(diagram, size)
        }
    }
}

@Composable
private fun EntryField(
    state: ManualState,
    key: MeasurementKey,
    onAction: (ManualEntryAction) -> Unit,
    height: Dp,
    fontSize: TextUnit,
) {
    NumberStepperField(
        value = displayInchInput(state.input, useBanglaDigits()),
        onValueChange = { onAction(ManualEntryAction.InputChanged(it)) },
        unit = stringResource(Res.string.unit_inch),
        onDecrease = { onAction(ManualEntryAction.Decrease) },
        onIncrease = { onAction(ManualEntryAction.Increase) },
        decreaseLabel = stringResource(Res.string.manual_decrease),
        increaseLabel = stringResource(Res.string.manual_increase),
        contentDescription = stringResource(Res.string.manual_value_desc, str(key.labelKey)),
        warning = state.warningOpen,
        height = height,
        fontSize = fontSize,
    )
}

private val WarningSheetPadding =
    PaddingValues(start = Dimens.space16, end = Dimens.space16, top = Dimens.space10, bottom = Dimens.space24)

/** 7.5 Unusual value: warning circle, title, usual range + the entered value (orange), measure again / it is right. */
@Composable
private fun ColumnScope.OutlierWarningBody(state: ManualState, key: MeasurementKey, onAction: (ManualEntryAction) -> Unit) {
    val useBangla = useBanglaDigits()
    val entered = displayInchInput(state.input, useBangla)
    val body = stringResource(
        Res.string.outlier_body,
        str(key.labelKey),
        formatInches(key.minInches, useBangla),
        formatInches(key.maxInches, useBangla),
        entered,
    )
    IconTile(
        icon = painterResource(Res.drawable.ic_warning),
        tint = MaapTheme.colors.warning,
        background = MaapTheme.colors.warningCircle,
        modifier = Modifier.align(Alignment.CenterHorizontally),
        size = 64.dp,
        iconSize = 34.dp,
    )
    Text(
        text = stringResource(Res.string.outlier_title),
        modifier = Modifier.fillMaxWidth().semantics { heading() },
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
    )
    Text(
        text = emphasize(
            body,
            "$entered\"",
            SpanStyle(color = MaapTheme.colors.onWarningBadge, fontSize = 20.sp, fontWeight = FontWeight.Bold),
            last = true,
        ),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge,
        lineHeight = 26.sp,
        color = MaapTheme.colors.textEmphasis,
        textAlign = TextAlign.Center,
    )
    Column(Modifier.padding(top = Dimens.space4), verticalArrangement = Arrangement.spacedBy(Dimens.space10)) {
        PrimaryButton(
            text = stringResource(Res.string.outlier_remeasure),
            onClick = { onAction(ManualEntryAction.Remeasure) },
            modifier = Modifier.fillMaxWidth(),
        )
        NeutralPillButton(
            text = stringResource(Res.string.outlier_confirm),
            onClick = { onAction(ManualEntryAction.ConfirmValue) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ManualEntryChestPreview() {
    MaapTheme(darkTheme = false) { ManualEntryScreen(ManualState.SampleChest) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ManualEntrySleevePreview() {
    MaapTheme(darkTheme = false) { ManualEntryScreen(ManualState.SampleSleeve) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ManualWarnSheetPreview() {
    val state = ManualState.SampleWarning
    val key = state.currentKey!!
    MaapTheme(darkTheme = false) {
        SheetOverlayPreview(base = { ManualEntryLayout(state, key) {} }) {
            BottomSheetContent(WarningSheetPadding, Arrangement.spacedBy(Dimens.space14)) {
                OutlierWarningBody(state, key) {}
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ManualNeckDepthPreview() {
    MaapTheme(darkTheme = false) { ManualEntryScreen(ManualState.SampleNeckDepth) {} }
}
