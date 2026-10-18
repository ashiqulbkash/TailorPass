package com.tech_hunt.tailorpass.ui.screens.manual

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
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
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.tech_hunt.tailorpass.model.LengthPreset
import com.tech_hunt.tailorpass.model.MeasurementKey
import com.tech_hunt.tailorpass.model.NeckDepthPreset
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.exit_body
import com.tech_hunt.tailorpass.resources.exit_continue
import com.tech_hunt.tailorpass.resources.exit_later
import com.tech_hunt.tailorpass.resources.exit_title
import com.tech_hunt.tailorpass.resources.height_feet_inches
import com.tech_hunt.tailorpass.resources.ic_arrow_forward
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.resources.ic_lightbulb
import com.tech_hunt.tailorpass.resources.ic_list
import com.tech_hunt.tailorpass.resources.ic_pause
import com.tech_hunt.tailorpass.resources.ic_people
import com.tech_hunt.tailorpass.resources.ic_warning
import com.tech_hunt.tailorpass.resources.label_knee
import com.tech_hunt.tailorpass.resources.length_by_height
import com.tech_hunt.tailorpass.resources.manual_all_measures
import com.tech_hunt.tailorpass.resources.manual_avoid_mistakes
import com.tech_hunt.tailorpass.resources.manual_decrease
import com.tech_hunt.tailorpass.resources.manual_finish
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
import com.tech_hunt.tailorpass.resources.text_dot_pair
import com.tech_hunt.tailorpass.resources.unit_inch
import com.tech_hunt.tailorpass.resources.value_inch
import com.tech_hunt.tailorpass.ui.components.BottomSheetContent
import com.tech_hunt.tailorpass.ui.components.CollapsibleTips
import com.tech_hunt.tailorpass.ui.components.ConfirmDialog
import com.tech_hunt.tailorpass.ui.components.ConfirmDialogCard
import com.tech_hunt.tailorpass.ui.components.DialogOverlayPreview
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
import com.tech_hunt.tailorpass.ui.diagram.LengthFigure
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
 * 7.3 / 7.4 / 7.6 / 7.10 One tape measurement, laid out by the current key: body measurement (step bar, helper banner,
 * diagram, how-to, "avoid mistakes", number), length (7.6: picture cards from the height, number, tip) or another
 * preference measurement (small diagram + question, picture cards, number). The last step has one Finish button.
 * 7.5: the unusual-value warning is a sheet over this screen; 7.8: back (also the system back / iOS swipe) asks
 * whether to stop.
 */
@Composable
fun ManualEntryScreen(state: ManualState, onAction: (ManualEntryAction) -> Unit) {
    val key = state.currentKey ?: return
    ManualEntryLayout(state, key, onAction)
    // Sheet and dialog handle back themselves while open.
    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = !state.warningOpen && !state.exitOpen,
        onBackCompleted = { onAction(ManualEntryAction.Back) },
    )
    if (state.exitOpen) {
        ConfirmDialog(
            onDismissRequest = { onAction(ManualEntryAction.KeepGoing) },
            icon = painterResource(Res.drawable.ic_pause),
            title = stringResource(Res.string.exit_title),
            body = stringResource(Res.string.exit_body, localDigits(state.doneCount.toString())),
            cancelText = stringResource(Res.string.exit_later),
            onCancel = { onAction(ManualEntryAction.StopForLater) },
            confirmText = stringResource(Res.string.exit_continue),
            onConfirm = { onAction(ManualEntryAction.KeepGoing) },
        )
    }
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
        topBar = { EntryTopBar(state, onAction, showStep = key != MeasurementKey.Length) },
        bottomBar = {
            if (state.isLast) {
                PrimaryButton(
                    text = stringResource(Res.string.manual_finish),
                    onClick = { onAction(ManualEntryAction.Next) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.canGoNext,
                    icon = painterResource(Res.drawable.ic_check),
                    iconSize = 22.dp,
                )
            } else {
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
            }
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space4, bottom = Dimens.space16),
    ) {
        when {
            key == MeasurementKey.Length -> LengthEntry(state, key, onAction)
            key.isPreference -> PreferenceEntry(state, key, onAction)
            else -> BodyEntry(state, key, onAction)
        }
    }
}

/** "3/8" in the step's language. */
@Composable
private fun stepText(state: ManualState): String =
    localDigits((state.current + 1).toString()) + "/" + localDigits(state.count.toString())

/** Top bar with the "3/8" chip; 7.6 shows the position in its title instead ([showStep] = false). */
@Composable
private fun EntryTopBar(state: ManualState, onAction: (ManualEntryAction) -> Unit, showStep: Boolean = true) {
    TopBar(
        title = stringResource(Res.string.manual_title),
        subtitle = garmentProfileSubtitle(state.target),
        onBack = { onAction(ManualEntryAction.Back) },
        endPadding = Dimens.space6,
    ) {
        if (showStep) {
            val stepDescription = stringResource(
                Res.string.manual_step_desc,
                localDigits((state.current + 1).toString()),
                localDigits(state.count.toString()),
            )
            TopBarChip(text = stepText(state), modifier = Modifier.semantics { contentDescription = stepDescription })
        }
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
        if (state.presets.isNotEmpty()) PresetCards(state, onAction)
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space6)) {
            EntryField(state, key, onAction, 68.dp, 42.sp)
            StepperHint(stringResource(Res.string.manual_type_hint), showIcon = false)
        }
    }
}

/**
 * 7.6 Length: badge, "ঝুল (Length) · ৮/৮", question · three 168dp picture cards · knee line legend with the height
 * the values come from · 64dp number · tip card.
 */
@Composable
private fun LengthEntry(state: ManualState, key: MeasurementKey, onAction: (ManualEntryAction) -> Unit) {
    val guide = state.guide
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.space12)) {
        StepSegments(count = state.count, filled = state.current + 1)
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space4)) {
            TextBadge(
                text = "✎ " + stringResource(Res.string.pref_badge),
                background = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            )
            MeasureTitle(
                stringResource(Res.string.text_dot_pair, str(key.labelKey), stepText(state)),
                MaterialTheme.typography.headlineMedium.fontSize,
                32.sp,
            )
            guide.howtoKey?.let {
                Text(str(it), style = MaterialTheme.typography.bodyMedium, color = MaapTheme.colors.textEmphasis)
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
            if (state.presets.isNotEmpty()) PresetCards(state, onAction)
            KneeLegend(state.target.heightInches)
        }
        EntryField(state, key, onAction, 64.dp, 40.sp)
        guide.noteKey?.let { note ->
            InfoBanner(
                icon = painterResource(Res.drawable.ic_lightbulb),
                iconTint = MaterialTheme.colorScheme.primary,
                background = MaterialTheme.colorScheme.surface,
                border = MaterialTheme.colorScheme.outlineVariant,
                iconSize = 20.dp,
                gap = Dimens.space8,
                centerIcon = false,
                verticalPadding = Dimens.space8,
                horizontalPadding = Dimens.space12,
                shape = TipCardShape,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = str(note),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    lineHeight = 21.75.sp,
                    color = MaapTheme.colors.textEmphasis,
                )
            }
        }
    }
}

private val TipCardShape = RoundedCornerShape(12.dp)

/** Dashed orange knee line + "হাঁটু · আপনার উচ্চতা (৫ ফুট ৭ ইঞ্চি) অনুযায়ী হিসাব"; just "হাঁটু" without a height. */
@Composable
private fun KneeLegend(heightInches: Int?) {
    val knee = stringResource(Res.string.label_knee)
    val text = if (heightInches == null) {
        knee
    } else {
        val height = stringResource(
            Res.string.height_feet_inches,
            localDigits((heightInches / 12).toString()),
            localDigits((heightInches % 12).toString()),
        )
        stringResource(Res.string.text_dot_pair, knee, stringResource(Res.string.length_by_height, height))
    }
    val color = MaapTheme.colors.warning
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens.space6)) {
        // SVG: 14dp, 24 viewport, "M2 12 H22", 2.4 wide, dashes 3 3.
        Canvas(Modifier.size(14.dp)) {
            val u = size.width / 24f
            drawLine(
                color, Offset(2 * u, 12 * u), Offset(22 * u, 12 * u), strokeWidth = 2.4f * u,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(3 * u, 3 * u)),
            )
        }
        Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** The picture cards of a preference measurement: 3 equal cards, 8dp apart (7.6: 168dp / 14 label; 7.10: 128 / 15). */
@Composable
private fun PresetCards(state: ManualState, onAction: (ManualEntryAction) -> Unit) {
    val useBangla = useBanglaDigits()
    Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space8)) {
        state.presets.forEach { preset ->
            val selected = state.selectedPreset == preset
            val isLength = preset is PresetOption.Length
            PresetPictureCard(
                label = str(preset.labelKey),
                value = stringResource(Res.string.value_inch, formatInches(preset.inches, useBangla)),
                selected = selected,
                onClick = { onAction(ManualEntryAction.PickPreset(preset)) },
                modifier = Modifier.weight(1f),
                height = if (isLength) 168.dp else 128.dp,
                labelFontSize = if (isLength) 14.sp else 15.sp,
                labelLineHeight = if (isLength) 17.5.sp else 20.sp,
            ) {
                when (preset) {
                    is PresetOption.Neck -> NecklineFigure(preset.depth.pictureDepth, selected, Modifier.size(64.dp, 52.dp))
                    is PresetOption.Length -> {
                        val (hem, halfWidth) = preset.length.pictureHem
                        LengthFigure(hem, halfWidth, selected, Modifier.size(56.dp, 100.dp))
                    }
                }
            }
        }
    }
}

/** Hem y and half width of each length picture in the 60×120 viewport (7.6); the knee line is at y = 84. */
private val LengthPreset.pictureHem: Pair<Float, Float>
    get() = when (this) {
        LengthPreset.AboveKnee -> 74f to 10f
        LengthPreset.AtKnee -> 84f to 11f
        LengthPreset.BelowKnee -> 96f to 12f
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

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ManualLengthPreview() {
    MaapTheme(darkTheme = false) { ManualEntryScreen(ManualState.SampleLength) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ManualExitDialogPreview() {
    val state = ManualState.SampleExit
    MaapTheme(darkTheme = false) {
        DialogOverlayPreview(base = { ManualEntryLayout(state, state.currentKey!!) {} }, top = 210.dp) {
            ConfirmDialogCard(
                icon = painterResource(Res.drawable.ic_pause),
                title = stringResource(Res.string.exit_title),
                body = stringResource(Res.string.exit_body, localDigits(state.doneCount.toString())),
                cancelText = stringResource(Res.string.exit_later),
                onCancel = {},
                confirmText = stringResource(Res.string.exit_continue),
                onConfirm = {},
            )
        }
    }
}
