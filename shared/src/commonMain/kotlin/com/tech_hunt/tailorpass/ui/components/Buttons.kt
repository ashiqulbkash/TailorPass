package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.theme.PillShape

/**
 * Primary button — only one per screen. 56dp pill, primary color, 18 bold.
 * Grey when disabled (#DDE1E6 / #6B7280). Pass Modifier.fillMaxWidth() for full width.
 * trailingIcon: 22dp icon after the text (onboarding "Next →").
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter? = null,
    trailingIcon: Painter? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(Dimens.buttonHeight),
        enabled = enabled,
        shape = PillShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaapTheme.colors.disabledContainer,
            disabledContentColor = MaapTheme.colors.onDisabled,
        ),
        contentPadding = PaddingValues(horizontal = Dimens.space16),
    ) {
        ButtonContent(text, icon, MaterialTheme.typography.labelLarge.fontSize, trailingIcon)
    }
}

/** Secondary button: 56dp pill, 1.5dp primary border, white background, 17 bold (18 on 2.4 / 6.3). */
@Composable
fun OutlinedPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter? = null,
    fontSize: TextUnit = 17.sp,
    iconSize: Dp = Dimens.iconSize,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(Dimens.buttonHeight),
        enabled = enabled,
        shape = PillShape,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        contentPadding = PaddingValues(horizontal = Dimens.space16),
    ) {
        ButtonContent(text, icon, fontSize, iconSize = iconSize)
    }
}

/** Text-only button (onboarding "Skip"): 56dp high, 22dp side padding, 17 semibold grey text. */
@Composable
fun TextPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(Dimens.buttonHeight),
        shape = PillShape,
        contentPadding = PaddingValues(horizontal = 22.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, maxLines = 1)
    }
}

/** Round 48dp icon button without background (help, share, edit). */
@Composable
fun MaapIconButton(
    icon: Painter,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    iconSize: Dp = Dimens.iconSize,
) {
    IconButton(onClick = onClick, modifier = modifier.size(Dimens.minTouchTarget)) {
        Icon(icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(iconSize))
    }
}

@Composable
private fun ButtonContent(
    text: String,
    icon: Painter?,
    fontSize: TextUnit,
    trailingIcon: Painter? = null,
    iconSize: Dp = Dimens.iconSize,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space8),
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(iconSize))
        }
        Text(text = text, style = MaterialTheme.typography.labelLarge, fontSize = fontSize, maxLines = 1)
        if (trailingIcon != null) {
            Icon(trailingIcon, contentDescription = null, modifier = Modifier.size(22.dp))
        }
    }
}
