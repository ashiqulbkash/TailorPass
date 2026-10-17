package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_camera
import com.tech_hunt.tailorpass.resources.ic_voice
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.theme.PillShape
import org.jetbrains.compose.resources.painterResource

/*
 * Camera screen controls (5.1–5.4). Camera screens are always dark; in the app's dark theme (5.2) the controls get
 * thin borders and the light-blue accent — the MaapTheme.colors.camera* tokens hold both variants.
 */

/** 48dp round button on the camera (close). */
@Composable
fun CameraRoundButton(
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(Dimens.minTouchTarget)
            .clip(CircleShape)
            .background(MaapTheme.colors.cameraPanel)
            .border(1.dp, MaapTheme.colors.cameraControlBorder, CircleShape)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = MaapTheme.colors.onCamera, modifier = Modifier.size(Dimens.iconSize))
    }
}

/** Voice-guide toggle: on = filled (white / dark blue) with a primary speaker; off = like the close button. */
@Composable
fun CameraVoiceToggle(
    on: Boolean,
    onToggle: (Boolean) -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaapTheme.colors
    Box(
        modifier = modifier
            .size(Dimens.minTouchTarget)
            .clip(CircleShape)
            .background(if (on) colors.cameraToggle else colors.cameraPanel)
            .border(1.dp, if (on) Color.Transparent else colors.cameraControlBorder, CircleShape)
            .toggleable(value = on, role = Role.Switch, onValueChange = onToggle)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_voice),
            contentDescription = null,
            tint = if (on) MaterialTheme.colorScheme.primary else colors.onCamera,
            modifier = Modifier.size(Dimens.iconSize),
        )
    }
}

/** "সামনের ছবি ১/২" pill: 44dp, 17 bold, then one dot per photo (current one 18dp wide). [step] is 0-based. */
@Composable
fun CameraStepPill(text: String, step: Int, count: Int, modifier: Modifier = Modifier) {
    val colors = MaapTheme.colors
    Row(
        modifier = modifier
            .height(44.dp)
            .background(colors.cameraPanel, PillShape)
            .border(1.dp, colors.cameraControlBorder, PillShape)
            .padding(horizontal = Dimens.space16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
    ) {
        Text(text = text, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = colors.onCamera)
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            repeat(count) { i ->
                Box(
                    Modifier
                        .size(width = if (i == step) 18.dp else 6.dp, height = 6.dp)
                        .background(if (i == step) colors.cameraAccent else colors.cameraDotInactive, PillShape),
                )
            }
        }
    }
}

/**
 * Live guidance under the top controls: warning (orange — move back, arms out) or instruction (white — turn).
 * Announced politely to screen readers when it changes.
 */
@Composable
fun CameraGuideBanner(
    icon: Painter,
    title: String,
    subtitle: String,
    warning: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = MaapTheme.colors
    InfoBanner(
        icon = icon,
        iconTint = if (warning) colors.cameraBannerIcon else MaterialTheme.colorScheme.primary,
        background = if (warning) colors.cameraBanner else MaterialTheme.colorScheme.surface,
        border = if (warning) colors.cameraBannerBorder else null,
        modifier = modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite },
        iconSize = 28.dp,
        gap = Dimens.space12,
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 19.sp,
                lineHeight = 24.sp,
                color = if (warning) colors.onCameraBanner else MaterialTheme.colorScheme.primary,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp,
                color = if (warning) colors.onCameraBannerSecondary else colors.textEmphasis,
            )
        }
    }
}

private val PanelShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

/**
 * Bottom panel: 60dp timer ring with the seconds, timer title + hint, and the start button (white on dark).
 * The 1dp top border (dark theme only) is a 1dp strip of the border color showing above the panel.
 * Sits at the screen bottom: it reaches the edge and keeps its content above the navigation bar.
 */
@Composable
fun CameraBottomPanel(
    seconds: String,
    title: String,
    hint: String,
    buttonText: String,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaapTheme.colors
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.cameraPanelBorder, PanelShape)
            .padding(top = 1.dp)
            .background(colors.cameraPanel, PanelShape)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
            .padding(start = Dimens.space16, end = Dimens.space16, top = 18.dp, bottom = Dimens.space20),
        verticalArrangement = Arrangement.spacedBy(Dimens.space14),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Dimens.space14)) {
            Box(
                Modifier.size(60.dp).border(3.dp, colors.cameraAccent, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = seconds,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 26.sp,
                    color = colors.cameraAccent,
                )
            }
            Column {
                Text(text = title, style = MaterialTheme.typography.titleSmall, color = colors.onCamera)
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 18.sp,
                    color = colors.onCameraSecondary,
                )
            }
        }
        PrimaryButton(
            text = buttonText,
            onClick = onStart,
            icon = painterResource(Res.drawable.ic_camera),
            containerColor = colors.cameraButton,
            contentColor = colors.onCameraButton,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
