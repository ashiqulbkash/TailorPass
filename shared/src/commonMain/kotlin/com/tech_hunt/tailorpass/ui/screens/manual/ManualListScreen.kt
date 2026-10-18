package com.tech_hunt.tailorpass.ui.screens.manual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.garment_profile_subtitle
import com.tech_hunt.tailorpass.resources.home_saved_count
import com.tech_hunt.tailorpass.resources.ic_arrow_forward
import com.tech_hunt.tailorpass.resources.manual_continue_progress
import com.tech_hunt.tailorpass.resources.manual_helper_needed
import com.tech_hunt.tailorpass.resources.manual_list_time
import com.tech_hunt.tailorpass.resources.manual_list_title
import com.tech_hunt.tailorpass.resources.manual_now
import com.tech_hunt.tailorpass.resources.manual_pending
import com.tech_hunt.tailorpass.resources.manual_tap_to_go
import com.tech_hunt.tailorpass.resources.value_inch
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.ManualStepRow
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.SectionCard
import com.tech_hunt.tailorpass.ui.components.StepRowState
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.screens.prep.garmentProfileSubtitle
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.localDigits
import com.tech_hunt.tailorpass.ui.util.str
import com.tech_hunt.tailorpass.ui.util.useBanglaDigits
import com.tech_hunt.tailorpass.util.formatInches
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 7.2 All tape measurements by section: done / now / pending, "helper needed" badges; a row jumps to it. */
@Composable
fun ManualListScreen(state: ManualState, onAction: (ManualListAction) -> Unit) {
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.manual_list_title, localDigits(state.count.toString())),
                // "about 5 minutes · panjabi · Rahat": the same " · " joiner as the garment/profile subtitle.
                subtitle = stringResource(
                    Res.string.garment_profile_subtitle,
                    stringResource(Res.string.manual_list_time, localDigits(state.minutes.toString())),
                    garmentProfileSubtitle(state.target),
                ),
                onBack = { onAction(ManualListAction.Back) },
            )
        },
        bottomBar = {
            PrimaryButton(
                text = stringResource(
                    Res.string.manual_continue_progress,
                    localDigits((state.current + 1).toString()),
                    localDigits(state.count.toString()),
                ),
                onClick = { onAction(ManualListAction.Continue) },
                enabled = state.count > 0,
                trailingIcon = painterResource(Res.drawable.ic_arrow_forward),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space4, bottom = Dimens.space16),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space10)) {
            val useBangla = useBanglaDigits()
            state.sections.forEachIndexed { sectionIndex, section ->
                val start = state.sectionStart(sectionIndex)
                SectionCard(
                    title = section.titleKey?.let { str(it) }.orEmpty(),
                    trailing = stringResource(Res.string.home_saved_count, localDigits(section.keys.size.toString())),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    section.keys.forEachIndexed { offset, key ->
                        val index = start + offset
                        val rowState = when (state.status(index)) {
                            StepStatus.Done -> StepRowState.Done
                            StepStatus.Current -> StepRowState.Current
                            StepStatus.Pending -> StepRowState.Pending
                        }
                        ManualStepRow(
                            number = localDigits((index + 1).toString()),
                            label = str(key.labelKey),
                            state = rowState,
                            onClick = { onAction(ManualListAction.Open(index)) },
                            value = state.values[index]?.let { stringResource(Res.string.value_inch, formatInches(it, useBangla)) },
                            statusText = stringResource(
                                if (rowState == StepRowState.Current) Res.string.manual_now else Res.string.manual_pending,
                            ),
                            helperBadge = if (key.needsHelper) stringResource(Res.string.manual_helper_needed) else null,
                        )
                    }
                }
            }
            Text(
                text = stringResource(Res.string.manual_tap_to_go),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ManualListPreview() {
    MaapTheme(darkTheme = false) { ManualListScreen(ManualState.SampleList) {} }
}
