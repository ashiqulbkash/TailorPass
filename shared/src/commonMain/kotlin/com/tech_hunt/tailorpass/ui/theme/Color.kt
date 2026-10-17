package com.tech_hunt.tailorpass.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * App colors — matched exactly to the design.
 * The primary color is dark blue; dark green is kept as an alternative (BrandVariant.Green).
 */
enum class BrandVariant { Blue, Green }

// ---------- Brand colors ----------
private object Brand {
    // Light
    val Blue = Color(0xFF1B3A6B)
    val BlueContainer = Color(0xFFE6ECF5)
    val Green = Color(0xFF1D5B3C)
    val GreenContainer = Color(0xFFE2EFE6)
    // Dark (lighter variants readable on dark backgrounds)
    val BlueDark = Color(0xFFA9C4F0)
    val BlueDarkOn = Color(0xFF0E2747)
    val BlueDarkContainer = Color(0xFF263B5E)
    val GreenDark = Color(0xFF9DD3B0)
    val GreenDarkOn = Color(0xFF0B2E1A)
    val GreenDarkContainer = Color(0xFF1F4630)
}

// ---------- Neutral colors ----------
private object Neutral {
    val Background = Color(0xFFF5F6F8)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFEEF0F3)
    val Text = Color(0xFF191C20)
    val TextSecondary = Color(0xFF48505C)
    val TextTertiary = Color(0xFF5B6370)
    val Outline = Color(0xFF8A929E)
    val OutlineVariant = Color(0xFFD5DAE1)
    val Divider = Color(0xFFE6E9EE)

    val DarkBackground = Color(0xFF111316)
    val DarkSurface = Color(0xFF1C1F24)
    val DarkSurfaceVariant = Color(0xFF2C3037)
    val DarkText = Color(0xFFE4E6EA)
    val DarkTextSecondary = Color(0xFFA9AFB8)
    val DarkOutline = Color(0xFF5B6370)
    val DarkOutlineVariant = Color(0xFF353A42)
}

fun maapLightColorScheme(brand: BrandVariant = BrandVariant.Blue): ColorScheme {
    val primary = if (brand == BrandVariant.Blue) Brand.Blue else Brand.Green
    val container = if (brand == BrandVariant.Blue) Brand.BlueContainer else Brand.GreenContainer
    return lightColorScheme(
        primary = primary,
        onPrimary = Color.White,
        primaryContainer = container,
        onPrimaryContainer = primary,
        secondary = Color(0xFF33415C),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE8ECF2),
        onSecondaryContainer = Color(0xFF33415C),
        background = Neutral.Background,
        onBackground = Neutral.Text,
        surface = Neutral.Surface,
        onSurface = Neutral.Text,
        surfaceVariant = Neutral.SurfaceVariant,
        onSurfaceVariant = Neutral.TextSecondary,
        surfaceContainer = Neutral.Surface,
        surfaceContainerHigh = Neutral.Surface,
        outline = Neutral.Outline,
        outlineVariant = Neutral.OutlineVariant,
        error = Color(0xFFB3261E),
        onError = Color.White,
        errorContainer = Color(0xFFFCE8E6),
        onErrorContainer = Color(0xFF8C1D18),
        scrim = Color(0xFF0D1117),
    )
}

fun maapDarkColorScheme(brand: BrandVariant = BrandVariant.Blue): ColorScheme {
    val blue = brand == BrandVariant.Blue
    return darkColorScheme(
        primary = if (blue) Brand.BlueDark else Brand.GreenDark,
        onPrimary = if (blue) Brand.BlueDarkOn else Brand.GreenDarkOn,
        primaryContainer = if (blue) Brand.BlueDarkContainer else Brand.GreenDarkContainer,
        onPrimaryContainer = if (blue) Color(0xFFD6E3FA) else Color(0xFFCDEBD7),
        secondary = Color(0xFFC3CBD6),
        onSecondary = Color(0xFF1C1F24),
        secondaryContainer = Color(0xFF2C333D),
        onSecondaryContainer = Color(0xFFC9D1DC),
        background = Neutral.DarkBackground,
        onBackground = Neutral.DarkText,
        surface = Neutral.DarkSurface,
        onSurface = Neutral.DarkText,
        surfaceVariant = Neutral.DarkSurfaceVariant,
        onSurfaceVariant = Neutral.DarkTextSecondary,
        surfaceContainer = Neutral.DarkSurface,
        surfaceContainerHigh = Neutral.DarkSurface,
        outline = Neutral.DarkOutline,
        outlineVariant = Neutral.DarkOutlineVariant,
        error = Color(0xFFF2B8B5),
        onError = Color(0xFF601410),
        errorContainer = Color(0xFF8C1D18),
        onErrorContainer = Color(0xFFF9DEDC),
        scrim = Color(0xFF000000),
    )
}

/**
 * Colors not in Material's ColorScheme: success (green), warning (orange), accuracy badges,
 * camera screens, diagrams, etc. Available next to MaterialTheme via MaapTheme.colors.
 */
@Immutable
data class MaapExtendedColors(
    val textTertiary: Color,
    val divider: Color,
    // Success — "Saved", check marks, high accuracy
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    // Warning — orange banners, low accuracy, skipped measurements
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val warningBorder: Color,
    val onWarningContainer: Color,
    val warningBadge: Color,
    val onWarningBadge: Color,
    val warningRow: Color,
    // Medium accuracy / neutral badges ("Tape-measured", "Helper needed")
    val neutralBadge: Color,
    val onNeutralBadge: Color,
    // Diagram
    val diagramOutline: Color,
    // Camera screens (always dark)
    val cameraTop: Color,
    val cameraFloor: Color,
    val cameraPanel: Color,
    val cameraBanner: Color,
    // Disabled buttons
    val disabledContainer: Color,
    val onDisabled: Color,
    // Onboarding page dots (not the current page)
    val dotInactive: Color,
    // Bottom nav top border
    val navDivider: Color,
    // Darker body text (empty state, unselected avatar letter)
    val textEmphasis: Color,
    // Dashed border of the empty saved-list box
    val dashedBorder: Color,
)

val MaapLightExtended = MaapExtendedColors(
    textTertiary = Color(0xFF5B6370),
    divider = Color(0xFFE6E9EE),
    success = Color(0xFF15803D),
    onSuccess = Color.White,
    successContainer = Color(0xFFDCFCE7),
    onSuccessContainer = Color(0xFF166534),
    warning = Color(0xFFB8470F),
    onWarning = Color.White,
    warningContainer = Color(0xFFFFF1E5),
    warningBorder = Color(0xFFF0B27F),
    onWarningContainer = Color(0xFF7C2D12),
    warningBadge = Color(0xFFFFE4CC),
    onWarningBadge = Color(0xFF9A3412),
    warningRow = Color(0xFFFFF6EC),
    neutralBadge = Color(0xFFE8ECF2),
    onNeutralBadge = Color(0xFF33415C),
    diagramOutline = Color(0xFF9AA2AE),
    cameraTop = Color(0xFF2B3037),
    cameraFloor = Color(0xFF1E2125),
    cameraPanel = Color(0xFF111418),
    cameraBanner = Color(0xFFB8470F),
    disabledContainer = Color(0xFFDDE1E6),
    onDisabled = Color(0xFF6B7280),
    dotInactive = Color(0xFFB9C0CA),
    navDivider = Color(0xFFE1E4E9),
    textEmphasis = Color(0xFF33373D),
    dashedBorder = Color(0xFFB9C0CA),
)

val MaapDarkExtended = MaapExtendedColors(
    textTertiary = Color(0xFFA9AFB8),
    divider = Color(0xFF2C3037),
    success = Color(0xFF8FE3A9),
    onSuccess = Color(0xFF0B2E1A),
    successContainer = Color(0xFF173A25),
    onSuccessContainer = Color(0xFF8FE3A9),
    warning = Color(0xFFFFB77A),
    onWarning = Color(0xFF4A2A12),
    warningContainer = Color(0xFF4A2A12),
    warningBorder = Color(0xFFA85A22),
    onWarningContainer = Color(0xFFFFD9B8),
    warningBadge = Color(0xFF4A2A12),
    onWarningBadge = Color(0xFFFFB77A),
    warningRow = Color(0xFF2A1E15),
    neutralBadge = Color(0xFF2C333D),
    onNeutralBadge = Color(0xFFC9D1DC),
    diagramOutline = Color(0xFF6B7380),
    cameraTop = Color(0xFF22262B),
    cameraFloor = Color(0xFF17191C),
    cameraPanel = Color(0xFF1C1F24),
    cameraBanner = Color(0xFF4A2A12),
    disabledContainer = Color(0xFF2C3037),
    onDisabled = Color(0xFF8A919B),
    dotInactive = Color(0xFF5B6370),
    navDivider = Color(0xFF2C3037),
    textEmphasis = Color(0xFFCDD1D7),
    dashedBorder = Color(0xFF5B6370),
)
