package com.tech_hunt.tailorpass.ui.screens.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_close
import com.tech_hunt.tailorpass.resources.cam_start
import com.tech_hunt.tailorpass.resources.cam_timer
import com.tech_hunt.tailorpass.resources.cam_voice_off
import com.tech_hunt.tailorpass.resources.cam_voice_on
import com.tech_hunt.tailorpass.resources.ic_close
import com.tech_hunt.tailorpass.ui.components.CameraBottomPanel
import com.tech_hunt.tailorpass.ui.components.CameraGuideBanner
import com.tech_hunt.tailorpass.ui.components.CameraRoundButton
import com.tech_hunt.tailorpass.ui.components.CameraStepPill
import com.tech_hunt.tailorpass.ui.components.CameraVoiceToggle
import com.tech_hunt.tailorpass.ui.diagram.BodyFigure
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.drawable
import com.tech_hunt.tailorpass.ui.util.localDigits
import com.tech_hunt.tailorpass.ui.util.str
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** Wall/floor split of the camera placeholder: the top 520 of the 800dp design frame. */
private const val WALL_FRACTION = 520f / 800f

/**
 * 5.1–5.4 Camera (front male / female, side): always dark, no bottom nav.
 * Design phase: a two-tone placeholder stands in for the preview; the real CameraPreview comes in Step 10.7.
 */
@Composable
fun CameraScreen(state: CameraState, onAction: (CameraAction) -> Unit) {
    val colors = MaapTheme.colors
    Box(Modifier.fillMaxSize()) {
        // Preview placeholder, full-bleed behind the system bars.
        Column(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxWidth().weight(WALL_FRACTION).background(colors.cameraTop))
            Box(Modifier.fillMaxWidth().weight(1f - WALL_FRACTION).background(colors.cameraFloor))
        }
        Column(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)),
        ) {
            Column(
                Modifier.padding(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space16),
                verticalArrangement = Arrangement.spacedBy(Dimens.space16),
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    CameraRoundButton(
                        icon = painterResource(Res.drawable.ic_close),
                        contentDescription = stringResource(Res.string.action_close),
                        onClick = { onAction(CameraAction.Close) },
                    )
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        CameraStepPill(text = str(state.shot.stepKey), step = state.step, count = state.stepCount)
                    }
                    CameraVoiceToggle(
                        on = state.voiceOn,
                        onToggle = { onAction(CameraAction.SetVoice(it)) },
                        contentDescription = stringResource(if (state.voiceOn) Res.string.cam_voice_on else Res.string.cam_voice_off),
                    )
                }
                state.guidance?.let { g ->
                    CameraGuideBanner(
                        icon = painterResource(drawable(g.iconName)),
                        title = str(g.titleKey),
                        subtitle = str(g.subtitleKey),
                        warning = g.warning,
                    )
                }
            }
            // Body outline: 176×458 under the banner (top 150 in the design), smaller when the screen is shorter.
            Box(
                Modifier.weight(1f).fillMaxWidth().padding(top = 9.dp, bottom = Dimens.space8),
                contentAlignment = Alignment.TopCenter,
            ) {
                BodyFigure(
                    outline = state.outline,
                    modifier = Modifier.heightIn(max = 458.dp).aspectRatio(176f / 458f, matchHeightConstraintsFirst = true),
                    stroke = colors.cameraAccent,
                    dashed = true,
                )
            }
            CameraBottomPanel(
                seconds = localDigits(CameraState.TIMER_SECONDS.toString()),
                title = stringResource(Res.string.cam_timer),
                hint = str(state.timerHintKey),
                buttonText = stringResource(Res.string.cam_start),
                onStart = { onAction(CameraAction.Start) },
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun CameraScreenPreview() {
    MaapTheme(darkTheme = false) { CameraScreen(CameraState.Sample) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun CameraScreenDarkPreview() {
    MaapTheme(darkTheme = true) { CameraScreen(CameraState.Sample) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun CameraFemalePreview() {
    MaapTheme(darkTheme = false) { CameraScreen(CameraState.SampleFemale) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun CameraSidePreview() {
    MaapTheme(darkTheme = false) { CameraScreen(CameraState.SampleSide) {} }
}
