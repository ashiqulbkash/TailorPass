package com.tech_hunt.tailorpass.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_lock
import com.tech_hunt.tailorpass.resources.privacy_delete_body
import com.tech_hunt.tailorpass.resources.privacy_delete_title
import com.tech_hunt.tailorpass.resources.privacy_net_body
import com.tech_hunt.tailorpass.resources.privacy_net_title
import com.tech_hunt.tailorpass.resources.privacy_photo_body
import com.tech_hunt.tailorpass.resources.privacy_photo_title
import com.tech_hunt.tailorpass.resources.privacy_summary
import com.tech_hunt.tailorpass.resources.privacy_updated
import com.tech_hunt.tailorpass.resources.privacy_what_body
import com.tech_hunt.tailorpass.resources.privacy_what_title
import com.tech_hunt.tailorpass.resources.privacy_where_body
import com.tech_hunt.tailorpass.resources.privacy_where_title
import com.tech_hunt.tailorpass.resources.settings_privacy
import com.tech_hunt.tailorpass.ui.components.IconTile
import com.tech_hunt.tailorpass.ui.components.InfoBanner
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.formatDate
import com.tech_hunt.tailorpass.ui.util.localDigits
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** The five numbered sections of the policy (12.5), in order. */
private val PolicySections = listOf(
    Res.string.privacy_what_title to Res.string.privacy_what_body,
    Res.string.privacy_where_title to Res.string.privacy_where_body,
    Res.string.privacy_photo_title to Res.string.privacy_photo_body,
    Res.string.privacy_net_title to Res.string.privacy_net_body,
    Res.string.privacy_delete_title to Res.string.privacy_delete_body,
)

/** 12.5 Privacy policy: back · title + "updated" date, green summary, card with five numbered sections. */
@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.settings_privacy),
                subtitle = stringResource(Res.string.privacy_updated, formatDate(SettingsState.PolicyUpdated)),
                onBack = onBack,
            )
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space4, bottom = Dimens.space20),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space10)) {
            InfoBanner(
                icon = painterResource(Res.drawable.ic_lock),
                iconTint = MaapTheme.colors.onSuccessContainer,
                background = MaapTheme.colors.successContainer,
                iconSize = 22.dp,
                centerIcon = false,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(Res.string.privacy_summary),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 23.sp,
                    color = MaapTheme.colors.successText,
                )
            }
            MaapCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = Dimens.space14, vertical = Dimens.space4),
            ) {
                PolicySections.forEachIndexed { index, (title, body) ->
                    PolicySection(index + 1, title, body, divider = index < PolicySections.lastIndex)
                }
            }
        }
    }
}

/** 32dp number tile (10dp corners) · 17 bold title over 16 body (line height 1.5); 12dp vertical padding. */
@Composable
private fun PolicySection(number: Int, title: StringResource, body: StringResource, divider: Boolean) {
    val line = MaapTheme.colors.divider
    Row(
        Modifier
            .fillMaxWidth()
            .then(
                if (divider) {
                    Modifier.drawBehind {
                        val y = size.height - 0.5.dp.toPx()
                        drawLine(line, Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
                    }
                } else Modifier,
            )
            .padding(vertical = Dimens.space12),
        horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
    ) {
        IconTile(background = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(10.dp)) {
            Text(
                text = localDigits(number.toString()),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = stringResource(title),
                modifier = Modifier.semantics { heading() },
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaapTheme.colors.textEmphasis,
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun PrivacyPolicyPreview() {
    MaapTheme(darkTheme = false) { PrivacyPolicyScreen {} }
}
