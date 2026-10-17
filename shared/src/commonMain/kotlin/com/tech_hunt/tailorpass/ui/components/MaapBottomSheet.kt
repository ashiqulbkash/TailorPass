package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.SheetShape

/**
 * Modal bottom sheet (overlay, not a route): surface, 28dp top corners, 36×4 handle, 50% scrim.
 * Opens fully expanded. Back press, scrim tap and dragging down call [onDismiss].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaapBottomSheet(
    onDismiss: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(start = Dimens.space16, end = Dimens.space16, top = Dimens.space10, bottom = Dimens.space24),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = SheetShape,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
        scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = Dimens.scrimAlpha),
        dragHandle = null,
    ) {
        BottomSheetContent(contentPadding, verticalArrangement, content)
    }
}

/** Sheet body: centered handle, then [content]; the handle is the first item of [verticalArrangement]. */
@Composable
fun BottomSheetContent(
    contentPadding: PaddingValues,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(contentPadding),
        verticalArrangement = verticalArrangement,
    ) {
        Box(
            Modifier
                .align(Alignment.CenterHorizontally)
                .size(Dimens.sheetHandleWidth, Dimens.sheetHandleHeight)
                .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(2.dp)),
        )
        content()
    }
}

/**
 * Preview only: a sheet drawn over its base screen with the scrim (ModalBottomSheet opens in a
 * separate window, which @Preview does not render).
 */
@Composable
internal fun SheetOverlayPreview(
    base: @Composable BoxScope.() -> Unit,
    sheet: @Composable ColumnScope.() -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        base()
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim.copy(alpha = Dimens.scrimAlpha)))
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, SheetShape),
            content = sheet,
        )
    }
}
