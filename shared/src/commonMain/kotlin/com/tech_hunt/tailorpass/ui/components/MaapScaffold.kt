package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tech_hunt.tailorpass.ui.theme.Dimens

/**
 * Screen frame: topBar at the top, scrolling content in the middle, fixed button(s) at the bottom (bottomBar).
 * Safe areas (notch, home indicator, keyboard) are handled with WindowInsets.safeDrawing.
 * Pass scrollable = false when the content is a LazyColumn.
 */
@Composable
fun MaapScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: (@Composable ColumnScope.() -> Unit)? = null,
    scrollable: Boolean = true,
    background: Color = MaterialTheme.colorScheme.background,
    contentPadding: PaddingValues = PaddingValues(
        start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space12, bottom = Dimens.space16,
    ),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .fillMaxSize()
            .background(background)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        topBar()
        Box(Modifier.weight(1f).fillMaxWidth()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier)
                    .padding(contentPadding),
                content = content,
            )
        }
        if (bottomBar != null) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = Dimens.screenPadding, end = Dimens.screenPadding, bottom = Dimens.space24),
                content = bottomBar,
            )
        }
    }
}
