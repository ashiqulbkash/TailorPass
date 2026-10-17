package com.tech_hunt.tailorpass.ui.screens.prep

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_measure_with_tape
import com.tech_hunt.tailorpass.resources.cam_perm_allow
import com.tech_hunt.tailorpass.resources.cam_perm_body
import com.tech_hunt.tailorpass.resources.cam_perm_hint
import com.tech_hunt.tailorpass.resources.cam_perm_title
import com.tech_hunt.tailorpass.resources.ic_camera_large
import com.tech_hunt.tailorpass.resources.ic_lock
import com.tech_hunt.tailorpass.ui.components.IconTile
import com.tech_hunt.tailorpass.ui.components.InfoBanner
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 3.2 Camera permission, shown before the OS prompt: why the camera is needed and that photos are never kept.
 * Design phase: "Allow" only navigates; the real OS request comes in Step 10.7 (CameraPermission).
 */
@Composable
fun CameraPermissionScreen(onAction: (CameraPermissionAction) -> Unit) {
    MaapScaffold(
        topBar = { TopBar(title = "", onBack = { onAction(CameraPermissionAction.Back) }) },
        scrollable = false,
        contentPadding = PaddingValues(horizontal = Dimens.space24),
        bottomBar = {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                PrimaryButton(
                    text = stringResource(Res.string.cam_perm_allow),
                    onClick = { onAction(CameraPermissionAction.Allow) },
                    modifier = Modifier.fillMaxWidth(),
                )
                UnderlinedLink(
                    text = stringResource(Res.string.action_measure_with_tape),
                    onClick = { onAction(CameraPermissionAction.UseTape) },
                )
            }
        },
        bottomBarPadding = PaddingValues(start = Dimens.space24, end = Dimens.space24, bottom = 28.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.space16, Alignment.CenterVertically),
        ) {
            IconTile(
                icon = painterResource(Res.drawable.ic_camera_large),
                tint = MaterialTheme.colorScheme.primary,
                background = MaterialTheme.colorScheme.primaryContainer,
                size = 112.dp,
                iconSize = 56.dp,
            )
            Text(
                text = stringResource(Res.string.cam_perm_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = Dimens.space8),
            )
            Text(
                text = stringResource(Res.string.cam_perm_body),
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 26.sp,
                color = MaapTheme.colors.textEmphasis,
                textAlign = TextAlign.Center,
            )
            InfoBanner(
                icon = painterResource(Res.drawable.ic_lock),
                iconTint = MaapTheme.colors.onSuccessContainer,
                background = MaterialTheme.colorScheme.surface,
                border = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(top = Dimens.space6),
            ) {
                Text(
                    text = stringResource(Res.string.cam_perm_hint),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    color = MaapTheme.colors.textEmphasis,
                )
            }
        }
    }
}

/** Secondary action as an underlined 17 semibold primary link, 48dp high (only on 3.2). */
@Composable
private fun UnderlinedLink(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(Dimens.minTouchTarget),
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun CameraPermissionScreenPreview() {
    MaapTheme(darkTheme = false) { CameraPermissionScreen {} }
}
