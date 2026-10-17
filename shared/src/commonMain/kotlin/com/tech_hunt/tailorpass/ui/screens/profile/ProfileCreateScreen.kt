package com.tech_hunt.tailorpass.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tech_hunt.tailorpass.model.Fit
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.profile_create_subtitle
import com.tech_hunt.tailorpass.resources.profile_create_title
import com.tech_hunt.tailorpass.resources.profile_fit
import com.tech_hunt.tailorpass.resources.profile_gender
import com.tech_hunt.tailorpass.resources.profile_height
import com.tech_hunt.tailorpass.resources.profile_height_hint
import com.tech_hunt.tailorpass.resources.profile_height_unit
import com.tech_hunt.tailorpass.resources.profile_name
import com.tech_hunt.tailorpass.resources.profile_save
import com.tech_hunt.tailorpass.resources.unit_cm
import com.tech_hunt.tailorpass.resources.unit_feet
import com.tech_hunt.tailorpass.resources.unit_feet_inch
import com.tech_hunt.tailorpass.resources.unit_inch
import com.tech_hunt.tailorpass.ui.components.FieldLabel
import com.tech_hunt.tailorpass.ui.components.FitCard
import com.tech_hunt.tailorpass.ui.components.LabeledField
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.MaapTextField
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.SegmentedToggle
import com.tech_hunt.tailorpass.ui.components.SelectCard
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.components.UnitNumberField
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.localDigits
import com.tech_hunt.tailorpass.ui.util.str
import org.jetbrains.compose.resources.stringResource

/** 1.5 Create profile: name, gender, height (feet-inch or cm), fit preference. */
@Composable
fun ProfileCreateScreen(state: ProfileCreateState, onAction: (ProfileCreateAction) -> Unit) {
    val numberKeyboard = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.profile_create_title),
                subtitle = stringResource(Res.string.profile_create_subtitle),
                onBack = { onAction(ProfileCreateAction.Back) },
                height = 68.dp,
            )
        },
        bottomBar = {
            PrimaryButton(
                text = stringResource(Res.string.profile_save),
                onClick = { onAction(ProfileCreateAction.Save) },
                enabled = state.canSave,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space8, bottom = Dimens.space16),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            LabeledField(stringResource(Res.string.profile_name)) {
                MaapTextField(
                    value = state.name,
                    onValueChange = { onAction(ProfileCreateAction.NameChanged(it)) },
                    contentDescription = stringResource(Res.string.profile_name),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                )
            }
            LabeledField(stringResource(Res.string.profile_gender)) {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space10)) {
                    Gender.entries.forEach { gender ->
                        SelectCard(
                            text = str(gender.labelKey),
                            selected = state.gender == gender,
                            onClick = { onAction(ProfileCreateAction.GenderSelected(gender)) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space6)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    FieldLabel(stringResource(Res.string.profile_height), Modifier.weight(1f))
                    SegmentedToggle(
                        options = listOf(stringResource(Res.string.unit_feet_inch), stringResource(Res.string.unit_cm)),
                        selectedIndex = state.heightUnit.ordinal,
                        onSelect = { onAction(ProfileCreateAction.HeightUnitSelected(HeightUnit.entries[it])) },
                        contentDescription = stringResource(Res.string.profile_height_unit),
                    )
                }
                when (state.heightUnit) {
                    HeightUnit.FeetInch -> Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space10)) {
                        UnitNumberField(
                            value = localDigits(state.feet),
                            onValueChange = { onAction(ProfileCreateAction.FeetChanged(it)) },
                            unit = stringResource(Res.string.unit_feet),
                            contentDescription = stringResource(Res.string.unit_feet),
                            keyboardOptions = numberKeyboard,
                            modifier = Modifier.weight(1f),
                        )
                        UnitNumberField(
                            value = localDigits(state.inches),
                            onValueChange = { onAction(ProfileCreateAction.InchesChanged(it)) },
                            unit = stringResource(Res.string.unit_inch),
                            contentDescription = stringResource(Res.string.unit_inch),
                            keyboardOptions = numberKeyboard.copy(imeAction = ImeAction.Done),
                            modifier = Modifier.weight(1f),
                        )
                    }
                    HeightUnit.Cm -> UnitNumberField(
                        value = localDigits(state.cm),
                        onValueChange = { onAction(ProfileCreateAction.CmChanged(it)) },
                        unit = stringResource(Res.string.unit_cm),
                        contentDescription = stringResource(Res.string.unit_cm),
                        keyboardOptions = numberKeyboard.copy(imeAction = ImeAction.Done),
                        numberWidth = 64.dp,
                    )
                }
                val cm = state.heightCm
                if (state.heightUnit == HeightUnit.FeetInch && cm != null) {
                    Text(
                        text = stringResource(Res.string.profile_height_hint, localDigits(cm.toString())),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            LabeledField(stringResource(Res.string.profile_fit)) {
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space10)) {
                    Fit.entries.forEach { fit ->
                        FitCard(
                            fit = fit,
                            selected = state.fit == fit,
                            onClick = { onAction(ProfileCreateAction.FitSelected(fit)) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileCreateScreenPreview() {
    MaapTheme(darkTheme = false) { ProfileCreateScreen(ProfileCreateState.Sample) {} }
}

@Preview
@Composable
private fun ProfileCreateScreenCmPreview() {
    MaapTheme(darkTheme = false) {
        ProfileCreateScreen(ProfileCreateState.Sample.reduce(ProfileCreateAction.HeightUnitSelected(HeightUnit.Cm))) {}
    }
}
