package com.tech_hunt.tailorpass.ui.screens.capture

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.tech_hunt.tailorpass.resources.action_measure_with_tape
import com.tech_hunt.tailorpass.resources.action_retry_photo
import com.tech_hunt.tailorpass.resources.ai_failed_body
import com.tech_hunt.tailorpass.resources.ai_failed_close
import com.tech_hunt.tailorpass.resources.ai_failed_reasons
import com.tech_hunt.tailorpass.resources.ai_failed_title
import com.tech_hunt.tailorpass.resources.ic_camera
import com.tech_hunt.tailorpass.resources.ic_close
import com.tech_hunt.tailorpass.resources.ic_warning
import com.tech_hunt.tailorpass.ui.components.IconTile
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapIconButton
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.OutlinedPillButton
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.screens.prep.MeasureTarget
import com.tech_hunt.tailorpass.ui.screens.prep.garmentProfileSubtitle
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.drawable
import com.tech_hunt.tailorpass.ui.util.str
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 6.3 The AI could not measure: reassurance, possible reasons, retake or switch to tape. */
@Composable
fun AiFailedScreen(target: MeasureTarget, onAction: (AiFailedAction) -> Unit) {
    MaapScaffold(
        topBar = {
            // Close (to Home) + "garment · name" only, no title.
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(Dimens.appBarHeight)
                    .padding(start = Dimens.space4, end = Dimens.space12, top = Dimens.space8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.space4),
            ) {
                MaapIconButton(
                    icon = painterResource(Res.drawable.ic_close),
                    contentDescription = stringResource(Res.string.ai_failed_close),
                    onClick = { onAction(AiFailedAction.Close) },
                    tint = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = garmentProfileSubtitle(target),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        },
        bottomBar = {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.space10)) {
                PrimaryButton(
                    text = stringResource(Res.string.action_retry_photo),
                    onClick = { onAction(AiFailedAction.Retry) },
                    icon = painterResource(Res.drawable.ic_camera),
                    iconSize = 22.dp,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedPillButton(
                    text = stringResource(Res.string.action_measure_with_tape),
                    onClick = { onAction(AiFailedAction.UseTape) },
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space8, bottom = Dimens.space16),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space16)) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = Dimens.space12),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.space10),
            ) {
                IconTile(
                    icon = painterResource(Res.drawable.ic_warning),
                    tint = MaapTheme.colors.warning,
                    background = MaapTheme.colors.warningCircle,
                    size = 96.dp,
                    iconSize = 48.dp,
                )
                Text(
                    text = stringResource(Res.string.ai_failed_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = Dimens.space6),
                )
                Text(
                    text = stringResource(Res.string.ai_failed_body),
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 25.sp,
                    color = MaapTheme.colors.textEmphasis,
                    textAlign = TextAlign.Center,
                )
            }
            MaapCard(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Dimens.space12)) {
                Text(
                    text = stringResource(Res.string.ai_failed_reasons),
                    style = MaterialTheme.typography.titleSmall,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
                AiFailedReasons.all.forEach { reason ->
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space12)) {
                        IconTile(
                            icon = painterResource(drawable(reason.iconName)),
                            tint = MaapTheme.colors.warning,
                            background = MaapTheme.colors.warningContainer,
                            size = 36.dp,
                            iconSize = 22.dp,
                            shape = RoundedCornerShape(10.dp),
                        )
                        Text(
                            text = str(reason.textKey),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 23.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun AiFailedPreview() {
    MaapTheme(darkTheme = false) { AiFailedScreen(MeasureTarget.SampleRahat) {} }
}
