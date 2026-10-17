package com.tech_hunt.tailorpass.ui.screens.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.MeasurementKey
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_start
import com.tech_hunt.tailorpass.resources.app_name
import com.tech_hunt.tailorpass.resources.language_bangla
import com.tech_hunt.tailorpass.resources.language_english
import com.tech_hunt.tailorpass.resources.language_title
import com.tech_hunt.tailorpass.resources.onb_welcome
import com.tech_hunt.tailorpass.resources.value_inch
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.OutlinedPillButton
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.components.TopBarChip
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.LANGUAGE_BN
import com.tech_hunt.tailorpass.ui.util.LANGUAGE_EN
import com.tech_hunt.tailorpass.ui.util.drawable
import com.tech_hunt.tailorpass.ui.util.str
import com.tech_hunt.tailorpass.util.formatInches
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Step 0 test screen: checks that the font, Bangla text, an icon, the primary button, formatInches, str(key)
 * and the language switch work. Replaced by the real first screen (language choice) in Step 1.
 */
data class FoundationState(val language: String)

sealed interface FoundationAction {
    data class ChangeLanguage(val tag: String) : FoundationAction
    data object Start : FoundationAction
}

@Composable
fun FoundationScreen(state: FoundationState, onAction: (FoundationAction) -> Unit) {
    val useBangla = state.language == LANGUAGE_BN
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.app_name),
                subtitle = stringResource(Res.string.onb_welcome),
                trailing = { TopBarChip(formatInches(24.5f, useBangla)) },
            )
        },
        bottomBar = {
            PrimaryButton(
                text = stringResource(Res.string.action_start),
                onClick = { onAction(FoundationAction.Start) },
                modifier = Modifier.fillMaxWidth(),
            )
        },
    ) {
        Icon(
            painter = painterResource(drawable(Garment.Panjabi.iconName)),
            contentDescription = str(Garment.Panjabi.labelKey),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(Dimens.iconTile),
        )
        Spacer(Modifier.height(Dimens.space12))
        Text(stringResource(Res.string.onb_welcome), style = MaterialTheme.typography.headlineLarge)
        Text(
            text = str(MeasurementKey.Chest.labelKey) + " — " +
                stringResource(Res.string.value_inch, formatInches(24.5f, useBangla)),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = str(Garment.SalwarKameez.labelKey),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(Res.string.value_inch, formatInches(40f, useBangla)),
            style = MaapTheme.numbers.inputValue,
        )
        Spacer(Modifier.height(Dimens.space24))
        Text(stringResource(Res.string.language_title), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(Dimens.space8))
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space10)) {
            OutlinedPillButton(
                text = stringResource(Res.string.language_bangla),
                onClick = { onAction(FoundationAction.ChangeLanguage(LANGUAGE_BN)) },
                modifier = Modifier.weight(1f),
            )
            OutlinedPillButton(
                text = stringResource(Res.string.language_english),
                onClick = { onAction(FoundationAction.ChangeLanguage(LANGUAGE_EN)) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
private fun FoundationScreenPreview() {
    MaapTheme(darkTheme = false) { FoundationScreen(FoundationState(LANGUAGE_BN)) {} }
}

@Preview
@Composable
private fun FoundationScreenDarkPreview() {
    MaapTheme(darkTheme = true) { FoundationScreen(FoundationState(LANGUAGE_BN)) {} }
}
