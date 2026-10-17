package com.tech_hunt.tailorpass.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_arrow_forward
import com.tech_hunt.tailorpass.resources.ic_lock
import com.tech_hunt.tailorpass.resources.ic_tape
import com.tech_hunt.tailorpass.resources.language_bangla
import com.tech_hunt.tailorpass.resources.language_bangla_hint
import com.tech_hunt.tailorpass.resources.language_english
import com.tech_hunt.tailorpass.resources.language_english_hint
import com.tech_hunt.tailorpass.resources.language_subtitle
import com.tech_hunt.tailorpass.resources.language_title
import com.tech_hunt.tailorpass.resources.privacy_promise
import com.tech_hunt.tailorpass.ui.components.IconTile
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.LANGUAGE_BN
import com.tech_hunt.tailorpass.ui.util.LANGUAGE_EN
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 1.1 Language choice. Picking a language applies it (setAppLanguage) and goes on to onboarding. */
sealed interface LanguageAction {
    data class Choose(val tag: String) : LanguageAction
}

@Composable
fun LanguageScreen(onAction: (LanguageAction) -> Unit) {
    MaapScaffold(
        scrollable = false,
        contentPadding = PaddingValues(horizontal = Dimens.space20),
        bottomBarPadding = PaddingValues(start = Dimens.space20, end = Dimens.space20, bottom = 28.dp),
        bottomBar = {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space14)) {
                LanguageOption(
                    title = stringResource(Res.string.language_bangla),
                    hint = stringResource(Res.string.language_bangla_hint),
                    filled = true,
                    onClick = { onAction(LanguageAction.Choose(LANGUAGE_BN)) },
                )
                LanguageOption(
                    title = stringResource(Res.string.language_english),
                    hint = stringResource(Res.string.language_english_hint),
                    filled = false,
                    onClick = { onAction(LanguageAction.Choose(LANGUAGE_EN)) },
                )
            }
            Spacer(Modifier.height(28.dp))
            PrivacyNote(stringResource(Res.string.privacy_promise))
        },
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.space14, Alignment.CenterVertically),
        ) {
            IconTile(
                icon = painterResource(Res.drawable.ic_tape),
                tint = MaterialTheme.colorScheme.primary,
                background = MaterialTheme.colorScheme.primaryContainer,
                size = 96.dp,
                iconSize = 56.dp,
                shape = RoundedCornerShape(28.dp),
            )
            Text(
                text = stringResource(Res.string.language_title),
                modifier = Modifier.padding(top = Dimens.space8),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(Res.string.language_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/** 72dp language card (only on 1.1): filled (default language) or outlined. Name 24 bold + hint 14 + arrow. */
@Composable
private fun LanguageOption(title: String, hint: String, filled: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    val content = if (filled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(shape)
            .background(if (filled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
            .then(if (filled) Modifier else Modifier.border(BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary), shape))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 22.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                lineHeight = 29.sp,
                color = content,
            )
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 17.sp,
                color = if (filled) content.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Icon(
            painter = painterResource(Res.drawable.ic_arrow_forward),
            contentDescription = null,
            tint = content,
            modifier = Modifier.size(28.dp),
        )
    }
}

/** Privacy line: green lock + 16 text in a card. */
@Composable
private fun PrivacyNote(text: String) {
    MaapCard(Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_lock),
                contentDescription = null,
                tint = MaapTheme.colors.onSuccessContainer,
                modifier = Modifier.size(28.dp),
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Composable
private fun LanguageScreenPreview() {
    MaapTheme(darkTheme = false) { LanguageScreen {} }
}
