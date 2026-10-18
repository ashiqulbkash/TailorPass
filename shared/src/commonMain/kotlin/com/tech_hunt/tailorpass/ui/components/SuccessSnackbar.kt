package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_close
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.resources.ic_close
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val SnackbarShape = RoundedCornerShape(14.dp)

/**
 * Green confirmation snackbar (12.2 "ব্যাকআপ সেভ হয়েছে"): 14dp corners, shadow, 12 / 8 / 12 / 14 padding;
 * 32dp white circle with a green check · 16 bold text (line height 1.35) · close button. Announced politely.
 */
@Composable
fun SuccessSnackbar(text: String, onClose: () -> Unit, modifier: Modifier = Modifier) {
    val colors = MaapTheme.colors
    Row(
        modifier = modifier
            .shadow(12.dp, SnackbarShape)
            .background(colors.successSnackbar, SnackbarShape)
            .padding(start = Dimens.space14, end = Dimens.space8, top = Dimens.space12, bottom = Dimens.space12)
            .semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
    ) {
        IconTile(
            icon = painterResource(Res.drawable.ic_check),
            tint = colors.successSnackbar,
            background = colors.onSuccessSnackbar,
            size = 32.dp,
            iconSize = 20.dp,
        )
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            lineHeight = 21.6.sp,
            color = colors.onSuccessSnackbar,
        )
        MaapIconButton(
            icon = painterResource(Res.drawable.ic_close),
            contentDescription = stringResource(Res.string.action_close),
            onClick = onClose,
            tint = colors.onSuccessSnackbar,
            iconSize = 20.dp,
        )
    }
}
