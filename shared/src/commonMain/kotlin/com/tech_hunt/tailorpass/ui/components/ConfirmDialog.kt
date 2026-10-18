package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme

/**
 * Centered confirm dialog (overlay, not a route): 56dp icon circle, 22 bold title, 16 body, then a neutral cancel
 * button and a primary confirm button side by side (52dp). 7.8 "stop measuring?".
 * Back press and a tap outside call [onDismissRequest].
 */
@Composable
fun ConfirmDialog(
    onDismissRequest: () -> Unit,
    icon: Painter,
    title: String,
    body: String,
    cancelText: String,
    onCancel: () -> Unit,
    confirmText: String,
    onConfirm: () -> Unit,
) {
    MaapDialog(onDismissRequest) {
        ConfirmDialogCard(icon, title, body, cancelText, onCancel, confirmText, onConfirm)
    }
}

/** Full-width platform dialog for a [DialogCard] (7.8, 12.3, 12.4). */
@Composable
fun MaapDialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false), content = content)
}

/** [DialogCard] with one centered text body (7.8). */
@Composable
fun ConfirmDialogCard(
    icon: Painter,
    title: String,
    body: String,
    cancelText: String,
    onCancel: () -> Unit,
    confirmText: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DialogCard(icon, title, cancelText, onCancel, confirmText, onConfirm, modifier) {
        Text(
            text = body,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp,
            lineHeight = 24.8.sp,
            color = MaapTheme.colors.textEmphasis,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * The dialog card: 20dp from the screen sides, 28dp corners, 56dp icon circle, 22 bold centered title, [content],
 * then cancel + confirm (52dp) side by side, 4dp extra above them.
 * Defaults (7.8, 12.4): 24 / 20 / 20 padding, 14dp gaps. Restore (12.3): 22 / 18 / 18 padding, 12dp gaps.
 * Delete all (12.4): red icon circle and confirm button ([iconTint], [iconBackground], [confirmColor]),
 * confirm disabled until the word is typed.
 */
@Composable
fun DialogCard(
    icon: Painter,
    title: String,
    cancelText: String,
    onCancel: () -> Unit,
    confirmText: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmEnabled: Boolean = true,
    confirmColor: Color = MaterialTheme.colorScheme.primary,
    onConfirmColor: Color = MaterialTheme.colorScheme.onPrimary,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    iconBackground: Color = MaterialTheme.colorScheme.primaryContainer,
    iconSize: Dp = 28.dp,
    contentPadding: PaddingValues = PaddingValues(
        start = Dimens.space20, end = Dimens.space20, top = Dimens.space24, bottom = Dimens.space20,
    ),
    gap: Dp = Dimens.space14,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = Dimens.space20)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, DialogShape)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        IconTile(
            icon = icon,
            tint = iconTint,
            background = iconBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            size = 56.dp,
            iconSize = iconSize,
        )
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth().semantics { heading() },
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        content()
        Row(Modifier.padding(top = Dimens.space4), horizontalArrangement = Arrangement.spacedBy(Dimens.space10)) {
            NeutralPillButton(text = cancelText, onClick = onCancel, modifier = Modifier.weight(1f))
            PrimaryButton(
                text = confirmText,
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                enabled = confirmEnabled,
                containerColor = confirmColor,
                contentColor = onConfirmColor,
                height = Dimens.dialogButtonHeight,
                fontSize = 17.sp,
            )
        }
    }
}

private val DialogShape = RoundedCornerShape(28.dp)

/**
 * Preview only: a dialog card drawn over its base screen with the scrim (Dialog opens in a separate window,
 * which @Preview does not render). [top] = the card's top edge in the 360×800 design.
 */
@Composable
internal fun DialogOverlayPreview(
    base: @Composable BoxScope.() -> Unit,
    top: Dp,
    dialog: @Composable () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        base()
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim.copy(alpha = Dimens.scrimAlpha)))
        Box(Modifier.padding(top = top)) { dialog() }
    }
}
