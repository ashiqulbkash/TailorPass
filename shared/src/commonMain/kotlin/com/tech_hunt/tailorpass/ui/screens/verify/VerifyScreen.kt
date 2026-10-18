package com.tech_hunt.tailorpass.ui.screens.verify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.model.MeasureMethod
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_close
import com.tech_hunt.tailorpass.resources.action_save
import com.tech_hunt.tailorpass.resources.action_see_result
import com.tech_hunt.tailorpass.resources.badge_tape_measured
import com.tech_hunt.tailorpass.resources.edit_ai_value
import com.tech_hunt.tailorpass.resources.edit_changed
import com.tech_hunt.tailorpass.resources.ic_arrow_forward
import com.tech_hunt.tailorpass.resources.ic_close
import com.tech_hunt.tailorpass.resources.ic_edit
import com.tech_hunt.tailorpass.resources.manual_decrease
import com.tech_hunt.tailorpass.resources.manual_increase
import com.tech_hunt.tailorpass.resources.manual_value_desc
import com.tech_hunt.tailorpass.resources.optional
import com.tech_hunt.tailorpass.resources.tailor_note
import com.tech_hunt.tailorpass.resources.text_dot_pair
import com.tech_hunt.tailorpass.resources.unit_inch
import com.tech_hunt.tailorpass.resources.value_inch
import com.tech_hunt.tailorpass.resources.verify_banner
import com.tech_hunt.tailorpass.resources.verify_banner_short
import com.tech_hunt.tailorpass.resources.verify_count
import com.tech_hunt.tailorpass.resources.verify_pref_hint
import com.tech_hunt.tailorpass.resources.verify_title
import com.tech_hunt.tailorpass.ui.components.BottomSheetContent
import com.tech_hunt.tailorpass.ui.components.InfoBanner
import com.tech_hunt.tailorpass.ui.components.MaapBottomSheet
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapIconButton
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.MeasurementRow
import com.tech_hunt.tailorpass.ui.components.NoteField
import com.tech_hunt.tailorpass.ui.components.NumberStepperField
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.SectionCard
import com.tech_hunt.tailorpass.ui.components.SheetOverlayPreview
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.diagram.TapeDiagramView
import com.tech_hunt.tailorpass.ui.screens.manual.ManualPlan
import com.tech_hunt.tailorpass.ui.screens.prep.garmentProfileSubtitle
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.drawable
import com.tech_hunt.tailorpass.ui.util.localDigits
import com.tech_hunt.tailorpass.ui.util.str
import com.tech_hunt.tailorpass.ui.util.useBanglaDigits
import com.tech_hunt.tailorpass.util.displayInchInput
import com.tech_hunt.tailorpass.util.formatInches
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 8.1 / 8.2 / 8.4 / 8.5 Verify measurements: every value with its source badge (AI accuracy, tape, your choice,
 * missing), tap a row to fix it on the 8.3 edit sheet, an optional note for the tailor, "See result" fixed at the
 * bottom. Salwar kameez (8.4) shows titled kameez / salwar cards above a raised bottom bar. 8.2 is the dark theme.
 */
@Composable
fun VerifyScreen(state: VerifyState, onAction: (VerifyAction) -> Unit) {
    VerifyLayout(state, onAction)
    // The sheet handles back, scrim taps and drag-down itself.
    state.editingRow?.let { row ->
        MaapBottomSheet(
            onDismiss = { onAction(VerifyAction.DismissEdit) },
            contentPadding = EditSheetPadding,
            verticalArrangement = Arrangement.spacedBy(Dimens.space14),
        ) { EditSheetBody(state, row, onAction) }
    }
}

@Composable
private fun VerifyLayout(state: VerifyState, onAction: (VerifyAction) -> Unit) {
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.verify_title),
                subtitle = subtitle(state),
                onBack = { onAction(VerifyAction.Back) },
            )
        },
        bottomBar = {
            val button: @Composable () -> Unit = {
                PrimaryButton(
                    text = stringResource(Res.string.action_see_result),
                    onClick = { onAction(VerifyAction.ShowResult) },
                    trailingIcon = painterResource(Res.drawable.ic_arrow_forward),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (state.sectioned) RaisedBottomBar(button) else button()
        },
        contentPadding = PaddingValues(
            start = Dimens.screenPadding,
            end = Dimens.screenPadding,
            top = Dimens.space4,
            bottom = if (state.sectioned) Dimens.space20 else Dimens.space10,
        ),
        bottomBarPadding = if (state.sectioned) PaddingValues() else
            PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, bottom = Dimens.space16),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space10)) {
            VerifyBanner(state)
            if (state.sectioned) {
                state.sections.forEachIndexed { i, section ->
                    SectionCard(
                        title = section.titleKey?.let { str(it) }.orEmpty(),
                        trailing = stringResource(Res.string.verify_count, localDigits(section.rows.size.toString())),
                        modifier = Modifier.fillMaxWidth(),
                        headerHeight = 44.dp,
                        icon = section.iconName?.let { painterResource(drawable(it)) },
                        titleStyle = MaterialTheme.typography.titleMedium,
                        trailingWeight = FontWeight.Normal,
                    ) {
                        Rows(state, start = state.sectionStart(i), section.rows, firstDivider = true, onAction)
                    }
                }
            } else if (state.rows.isNotEmpty()) {
                MaapCard(
                    modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.large),
                    contentPadding = PaddingValues(),
                ) { Rows(state, start = 0, state.rows, firstDivider = false, onAction) }
            }
            TailorNote(state, onAction)
        }
    }
}

/** "পাঞ্জাবি · রাহাত", plus "· ফিতায় মাপা" after the tape (8.5) and "· ১৪টি মাপ" for salwar kameez (8.4). */
@Composable
private fun subtitle(state: VerifyState): String {
    var text = garmentProfileSubtitle(state.target)
    if (state.method == MeasureMethod.Tape) {
        text = stringResource(Res.string.text_dot_pair, text, stringResource(Res.string.badge_tape_measured))
    }
    if (state.sectioned) {
        text = stringResource(
            Res.string.text_dot_pair,
            text,
            stringResource(Res.string.verify_count, localDigits(state.count.toString())),
        )
    }
    return text
}

/** Tint banner: "tap a value to fix it"; after photos with preference values, a shorter line + the 14 hint (8.4). */
@Composable
private fun VerifyBanner(state: VerifyState) {
    InfoBanner(
        icon = painterResource(Res.drawable.ic_edit),
        iconTint = MaterialTheme.colorScheme.primary,
        background = MaterialTheme.colorScheme.primaryContainer,
        iconSize = 22.dp,
        gap = if (state.showPreferenceHint) Dimens.space10 else Dimens.space12,
        centerIcon = !state.showPreferenceHint,
        verticalPadding = Dimens.space8,
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (state.showPreferenceHint) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space4)) {
                BannerText(stringResource(Res.string.verify_banner_short))
                Text(
                    text = stringResource(Res.string.verify_pref_hint),
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 19.6.sp,
                    color = MaapTheme.colors.textEmphasis,
                )
            }
        } else {
            BannerText(stringResource(Res.string.verify_banner))
        }
    }
}

@Composable
private fun BannerText(text: String) {
    Text(text, style = MaterialTheme.typography.bodyMedium, lineHeight = 21.6.sp, color = MaterialTheme.colorScheme.onSurface)
}

/** Rows of one card; [start] is the index of the first row in [VerifyState.rows]. */
@Composable
private fun Rows(
    state: VerifyState,
    start: Int,
    rows: List<VerifyRow>,
    firstDivider: Boolean,
    onAction: (VerifyAction) -> Unit,
) {
    val useBangla = useBanglaDigits()
    rows.forEachIndexed { offset, row ->
        MeasurementRow(
            name = str(row.key.labelKey),
            value = row.inches?.let { stringResource(Res.string.value_inch, formatInches(it, useBangla)) },
            badge = row.badge,
            onClick = { onAction(VerifyAction.Edit(start + offset)) },
            divider = firstDivider || offset > 0,
            nameSize = if (state.sectioned) 16.sp else MaterialTheme.typography.titleSmall.fontSize,
        )
    }
}

/** "দর্জির জন্য নোট (ঐচ্ছিক)" label (the bracket part regular, grey) over the note box. */
@Composable
private fun TailorNote(state: VerifyState, onAction: (VerifyAction) -> Unit) {
    val title = stringResource(Res.string.tailor_note)
    val optional = stringResource(Res.string.optional)
    val secondary = MaterialTheme.colorScheme.onSurfaceVariant
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.space4)) {
        Text(
            text = buildAnnotatedString {
                append(title)
                append(" ")
                withStyle(SpanStyle(fontWeight = FontWeight.Normal, color = secondary)) { append(optional) }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        NoteField(
            value = state.note,
            onValueChange = { onAction(VerifyAction.NoteChanged(it)) },
            placeholder = str(state.noteHintKey),
            contentDescription = "$title $optional",
        )
    }
}

/** 8.4 bottom bar: surface with a 1dp top border and a soft upward shadow, 12 / 16 / 16 padding. */
@Composable
private fun RaisedBottomBar(content: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .shadow(6.dp, clip = false)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        Box(Modifier.padding(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space12, bottom = Dimens.space16)) {
            content()
        }
    }
}

private val EditSheetPadding =
    PaddingValues(start = Dimens.space16, end = Dimens.space16, top = Dimens.space10, bottom = Dimens.space24)

/**
 * 8.3 Edit sheet: name + close, the tape diagram and how-to when the key has one, the −/+ number field, the AI's value
 * ("· আপনি বদলেছেন" once changed), Save.
 */
@Composable
private fun ColumnScope.EditSheetBody(state: VerifyState, row: VerifyRow, onAction: (VerifyAction) -> Unit) {
    val useBangla = useBanglaDigits()
    val name = str(row.key.labelKey)
    val guide = ManualPlan.guide(row.key)
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            modifier = Modifier.weight(1f).semantics { heading() },
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        MaapIconButton(
            icon = painterResource(Res.drawable.ic_close),
            contentDescription = stringResource(Res.string.action_close),
            onClick = { onAction(VerifyAction.DismissEdit) },
            iconSize = 22.dp,
        )
    }
    if (guide.diagram != null || guide.howtoKey != null) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens.space14)) {
            guide.diagram?.let { diagram ->
                Box(
                    Modifier
                        .size(104.dp, 120.dp)
                        .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center,
                ) { TapeDiagramView(diagram, Modifier.size(96.dp, 112.dp)) }
            }
            guide.howtoKey?.let {
                Text(
                    text = str(it),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaapTheme.colors.textEmphasis,
                )
            }
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
        NumberStepperField(
            value = displayInchInput(state.editInput, useBangla),
            onValueChange = { onAction(VerifyAction.EditInputChanged(it)) },
            unit = stringResource(Res.string.unit_inch),
            onDecrease = { onAction(VerifyAction.EditDecrease) },
            onIncrease = { onAction(VerifyAction.EditIncrease) },
            decreaseLabel = stringResource(Res.string.manual_decrease),
            increaseLabel = stringResource(Res.string.manual_increase),
            contentDescription = stringResource(Res.string.manual_value_desc, name),
        )
        row.aiInches?.let { ai ->
            val aiText = stringResource(Res.string.edit_ai_value, formatInches(ai, useBangla))
            Text(
                text = if (state.editChanged) {
                    stringResource(Res.string.text_dot_pair, aiText, stringResource(Res.string.edit_changed))
                } else {
                    aiText
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    PrimaryButton(
        text = stringResource(Res.string.action_save),
        onClick = { onAction(VerifyAction.SaveEdit) },
        enabled = state.canSave,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun VerifyPreview() {
    MaapTheme(darkTheme = false) { VerifyScreen(VerifyState.SamplePanjabi) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun VerifyDarkPreview() {
    MaapTheme(darkTheme = true) { VerifyScreen(VerifyState.SamplePanjabi) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun EditSheetPreview() {
    val state = VerifyState.SampleEdit
    MaapTheme(darkTheme = false) {
        SheetOverlayPreview(base = { VerifyLayout(state) {} }) {
            BottomSheetContent(EditSheetPadding, Arrangement.spacedBy(Dimens.space14)) {
                EditSheetBody(state, state.editingRow!!) {}
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun VerifySalwarKameezPreview() {
    MaapTheme(darkTheme = false) { VerifyScreen(VerifyState.SampleSalwarKameez) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun VerifyManualPreview() {
    MaapTheme(darkTheme = false) { VerifyScreen(VerifyState.SampleManual) {} }
}
