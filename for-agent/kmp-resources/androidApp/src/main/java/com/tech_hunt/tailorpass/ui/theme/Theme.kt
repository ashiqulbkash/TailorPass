package com.tech_hunt.tailorpass.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalMaapColors = staticCompositionLocalOf { MaapLightExtended }
private val LocalMaapNumbers = staticCompositionLocalOf<MaapNumberStyles> {
    error("MaapTheme দিয়ে মোড়ানো হয়নি")
}

/**
 * ব্যবহার:
 * MaapTheme {
 *     Text("৪০\"", style = MaapTheme.numbers.listValue)
 *     Surface(color = MaapTheme.colors.successContainer) { … }
 * }
 */
@Composable
fun MaapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    brand: BrandVariant = BrandVariant.Blue,
    content: @Composable () -> Unit,
) {
    val scheme = if (darkTheme) maapDarkColorScheme(brand) else maapLightColorScheme(brand)
    val extended = if (darkTheme) MaapDarkExtended else MaapLightExtended
    CompositionLocalProvider(
        LocalMaapColors provides extended,
        LocalMaapNumbers provides maapNumberStyles(),
    ) {
        MaterialTheme(
            colorScheme = scheme,
            typography = maapTypography(),
            shapes = MaapShapes,
            content = content,
        )
    }
}

object MaapTheme {
    val colors: MaapExtendedColors
        @Composable @ReadOnlyComposable get() = LocalMaapColors.current
    val numbers: MaapNumberStyles
        @Composable @ReadOnlyComposable get() = LocalMaapNumbers.current
}
