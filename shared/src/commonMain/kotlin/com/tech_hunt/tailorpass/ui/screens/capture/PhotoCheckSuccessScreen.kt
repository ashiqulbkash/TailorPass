package com.tech_hunt.tailorpass.ui.screens.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.resources.ic_lock
import com.tech_hunt.tailorpass.resources.photo_both_ok
import com.tech_hunt.tailorpass.resources.photo_check_title
import com.tech_hunt.tailorpass.resources.photo_front
import com.tech_hunt.tailorpass.resources.photo_side
import com.tech_hunt.tailorpass.resources.processing_cancel
import com.tech_hunt.tailorpass.resources.processing_offline
import com.tech_hunt.tailorpass.resources.processing_percent
import com.tech_hunt.tailorpass.resources.processing_title
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapProgressBar
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.NeutralPillButton
import com.tech_hunt.tailorpass.ui.components.PhotoThumb
import com.tech_hunt.tailorpass.ui.components.StatusLine
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.screens.prep.garmentProfileSubtitle
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.theme.PillShape
import com.tech_hunt.tailorpass.ui.util.localDigits
import com.tech_hunt.tailorpass.ui.util.str
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** 6.1 Both photos accepted; measurements are being worked out on the phone. */
@Composable
fun PhotoCheckSuccessScreen(state: ProcessingState, onAction: (ProcessingAction) -> Unit) {
    MaapScaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.photo_check_title),
                subtitle = garmentProfileSubtitle(state.target),
                onBack = { onAction(ProcessingAction.Back) },
                height = 68.dp,
            )
        },
        bottomBar = {
            NeutralPillButton(
                text = stringResource(Res.string.processing_cancel),
                onClick = { onAction(ProcessingAction.Cancel) },
                modifier = Modifier.fillMaxWidth(),
            )
        },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space6, bottom = Dimens.space12),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space12)) {
            BothOkChip()
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space10)) {
                PhotoThumb(stringResource(Res.string.photo_front), state.target.outline(CameraShot.Front), Modifier.weight(1f))
                PhotoThumb(stringResource(Res.string.photo_side), state.target.outline(CameraShot.Side), Modifier.weight(1f))
            }
            MaapCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(Dimens.space16),
                verticalArrangement = Arrangement.spacedBy(Dimens.space12),
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.processing_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        lineHeight = 26.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = stringResource(Res.string.processing_percent, localDigits(state.progress.toString())),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    )
                }
                MaapProgressBar(progress = state.progress / 100f)
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                    ProcessingStep.entries.forEach { step ->
                        StatusLine(text = str(step.labelKey), status = state.status(step))
                    }
                }
            }
            Row(
                modifier = Modifier.padding(horizontal = Dimens.space4),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.space8),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_lock),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = stringResource(Res.string.processing_offline),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** "দুটি ছবিই ঠিক আছে": 34dp green chip with a check (only on 6.1). */
@Composable
private fun BothOkChip() {
    val colors = MaapTheme.colors
    Row(
        modifier = Modifier
            .height(34.dp)
            .background(colors.successContainer, PillShape)
            .padding(start = Dimens.space10, end = Dimens.space14)
            .semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space6),
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_check),
            contentDescription = null,
            tint = colors.onSuccessContainer,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = stringResource(Res.string.photo_both_ok),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.onSuccessContainer,
        )
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun PhotoCheckSuccessPreview() {
    MaapTheme(darkTheme = false) { PhotoCheckSuccessScreen(ProcessingState.Sample) {} }
}
