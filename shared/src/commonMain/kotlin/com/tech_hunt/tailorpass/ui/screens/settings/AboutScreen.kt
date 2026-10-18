package com.tech_hunt.tailorpass.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.about_contact
import com.tech_hunt.tailorpass.resources.about_contact_email
import com.tech_hunt.tailorpass.resources.about_made_for
import com.tech_hunt.tailorpass.resources.about_offline_badge
import com.tech_hunt.tailorpass.resources.about_tagline
import com.tech_hunt.tailorpass.resources.app_logo
import com.tech_hunt.tailorpass.resources.app_name
import com.tech_hunt.tailorpass.resources.ic_lock
import com.tech_hunt.tailorpass.resources.ic_mail
import com.tech_hunt.tailorpass.resources.ic_shield
import com.tech_hunt.tailorpass.resources.settings_about
import com.tech_hunt.tailorpass.resources.settings_privacy
import com.tech_hunt.tailorpass.resources.settings_version
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.SettingsGroup
import com.tech_hunt.tailorpass.ui.components.SettingsRow
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.theme.PillShape
import com.tech_hunt.tailorpass.ui.util.localDigits
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

sealed interface AboutAction {
    data object Back : AboutAction
    data object Contact : AboutAction
    data object OpenPrivacy : AboutAction
}

/**
 * 12.6 About: back · title, 104dp logo, app name (TailorPass), version, tagline, green offline badge, contact and
 * privacy rows; "made for people in Bangladesh" fixed at the bottom.
 */
@Composable
fun AboutScreen(onAction: (AboutAction) -> Unit) {
    MaapScaffold(
        topBar = { TopBar(title = stringResource(Res.string.settings_about), onBack = { onAction(AboutAction.Back) }) },
        bottomBar = {
            Text(
                text = stringResource(Res.string.about_made_for),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space8, bottom = Dimens.space16),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space16)) {
            Hero()
            SettingsGroup {
                SettingsRow(
                    icon = painterResource(Res.drawable.ic_mail),
                    title = stringResource(Res.string.about_contact),
                    subtitle = stringResource(Res.string.about_contact_email),
                    minHeight = 64.dp,
                    onClick = { onAction(AboutAction.Contact) },
                    divider = true,
                )
                SettingsRow(
                    icon = painterResource(Res.drawable.ic_shield),
                    title = stringResource(Res.string.settings_privacy),
                    minHeight = 56.dp,
                    onClick = { onAction(AboutAction.OpenPrivacy) },
                )
            }
        }
    }
}

/** Logo, 34 bold name, 15 grey version, 17 tagline (line height 1.5), green "offline" badge; 8dp apart. */
@Composable
private fun Hero() {
    Column(
        Modifier.fillMaxWidth().padding(start = Dimens.space8, end = Dimens.space8, top = Dimens.space20, bottom = Dimens.space8),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.space8),
    ) {
        Image(painterResource(Res.drawable.app_logo), contentDescription = null, modifier = Modifier.size(104.dp))
        Text(
            text = stringResource(Res.string.app_name),
            modifier = Modifier.padding(top = Dimens.space6),
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 34.sp,
            lineHeight = 41.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = stringResource(Res.string.settings_version, localDigits(SettingsState.APP_VERSION)),
            style = MaterialTheme.typography.bodySmall,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(Res.string.about_tagline),
            modifier = Modifier.padding(top = Dimens.space4),
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 25.5.sp,
            color = MaapTheme.colors.textEmphasis,
            textAlign = TextAlign.Center,
        )
        Row(
            Modifier
                .padding(top = Dimens.space6)
                .background(MaapTheme.colors.successContainer, PillShape)
                .padding(horizontal = Dimens.space14, vertical = Dimens.space6),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.space6),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_lock),
                contentDescription = null,
                tint = MaapTheme.colors.onSuccessContainer,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = stringResource(Res.string.about_offline_badge),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaapTheme.colors.successText,
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun AboutPreview() {
    MaapTheme(darkTheme = false) { AboutScreen {} }
}
