package com.tech_hunt.tailorpass.ui.screens.manual

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_start
import com.tech_hunt.tailorpass.resources.ic_arrow_forward
import com.tech_hunt.tailorpass.resources.ic_fit_shirt
import com.tech_hunt.tailorpass.resources.ic_people
import com.tech_hunt.tailorpass.resources.ic_tape
import com.tech_hunt.tailorpass.resources.illus_tape_finger
import com.tech_hunt.tailorpass.resources.manual_prep_clothes
import com.tech_hunt.tailorpass.resources.manual_prep_finger
import com.tech_hunt.tailorpass.resources.manual_prep_finger_bold
import com.tech_hunt.tailorpass.resources.manual_prep_helper
import com.tech_hunt.tailorpass.resources.manual_prep_need
import com.tech_hunt.tailorpass.resources.manual_prep_need_bold
import com.tech_hunt.tailorpass.resources.manual_prep_title
import com.tech_hunt.tailorpass.resources.manual_unit
import com.tech_hunt.tailorpass.resources.unit_cm
import com.tech_hunt.tailorpass.resources.unit_inch
import com.tech_hunt.tailorpass.ui.components.IconTile
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.SegmentedToggle
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.screens.prep.garmentProfileSubtitle
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.emphasize
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 7.1 Before measuring with a tape: what you need, helper advice, one-finger tightness, unit, Start. */
@Composable
fun ManualPrepScreen(state: ManualState, onAction: (ManualPrepAction) -> Unit) {
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.manual_prep_title),
                subtitle = garmentProfileSubtitle(state.target),
                onBack = { onAction(ManualPrepAction.Back) },
            )
        },
        bottomBar = {
            PrimaryButton(
                text = stringResource(Res.string.action_start),
                onClick = { onAction(ManualPrepAction.Start) },
                trailingIcon = painterResource(Res.drawable.ic_arrow_forward),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space4, bottom = Dimens.space16),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space10)) {
            MaapCard(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues()) {
                PrepRows.forEachIndexed { index, row ->
                    if (index > 0) HorizontalDivider(thickness = 1.dp, color = MaapTheme.colors.divider)
                    PrepRow(row)
                }
            }
            MaapCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = Dimens.space14, vertical = Dimens.space12),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens.space12)) {
                    Image(painterResource(Res.drawable.illus_tape_finger), contentDescription = null, modifier = Modifier.size(130.dp, 104.dp))
                    Text(
                        text = emphasize(
                            stringResource(Res.string.manual_prep_finger),
                            stringResource(Res.string.manual_prep_finger_bold),
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 23.sp,
                        color = MaapTheme.colors.textEmphasis,
                    )
                }
            }
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 2.dp, vertical = Dimens.space4),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                val unitLabel = stringResource(Res.string.manual_unit)
                Text(unitLabel, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                SegmentedToggle(
                    options = listOf(stringResource(Res.string.unit_inch), stringResource(Res.string.unit_cm)),
                    selectedIndex = state.unit.ordinal,
                    onSelect = { onAction(ManualPrepAction.SetUnit(MeasureUnit.entries[it])) },
                    contentDescription = unitLabel,
                    segmentWidth = 72.dp,
                )
            }
        }
    }
}

private class PrepRowData(val icon: DrawableResource, val text: StringResource, val bold: StringResource? = null)

private val PrepRows = listOf(
    PrepRowData(Res.drawable.ic_tape, Res.string.manual_prep_need, Res.string.manual_prep_need_bold),
    PrepRowData(Res.drawable.ic_people, Res.string.manual_prep_helper),
    PrepRowData(Res.drawable.ic_fit_shirt, Res.string.manual_prep_clothes),
)

/** Icon tile (40dp, 12dp corners) + 16sp text; min 62dp, 8 / 14 padding. */
@Composable
private fun PrepRow(row: PrepRowData) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 62.dp).padding(horizontal = Dimens.space14, vertical = Dimens.space8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
    ) {
        IconTile(
            icon = painterResource(row.icon),
            tint = MaterialTheme.colorScheme.primary,
            background = MaterialTheme.colorScheme.primaryContainer,
            size = Dimens.iconTileSmall,
            iconSize = Dimens.iconSize,
            shape = RoundedCornerShape(12.dp),
        )
        val bold = row.bold?.let { stringResource(it) }.orEmpty()
        Text(
            text = emphasize(stringResource(row.text), bold),
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ManualPrepPreview() {
    MaapTheme(darkTheme = false) { ManualPrepScreen(ManualState.SamplePrep) {} }
}
