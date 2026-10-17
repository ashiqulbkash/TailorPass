package com.tech_hunt.tailorpass.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Spacing and sizes from the design (360dp-wide phone frame). */
object Dimens {
    // Spacing
    val space4 = 4.dp
    val space6 = 6.dp
    val space8 = 8.dp
    val space10 = 10.dp
    val space12 = 12.dp
    val space14 = 14.dp
    val space16 = 16.dp
    val space20 = 20.dp
    val space24 = 24.dp

    val screenPadding = 16.dp          // Side margins
    val minTouchTarget = 48.dp         // Everything tappable is ≥48dp

    // Bars
    val appBarHeight = 64.dp
    val bottomNavHeight = 80.dp
    val navIndicatorWidth = 64.dp
    val navIndicatorHeight = 32.dp

    // Buttons
    val buttonHeight = 56.dp           // Primary full-width button
    val buttonHeightCompact = 46.dp    // The two share buttons on the result screen
    val dialogButtonHeight = 52.dp
    val segmentedHeight = 44.dp        // Inch/cm toggle

    // Cards and lists
    val garmentCardHeight = 80.dp
    val listRowMinHeight = 52.dp       // Verify measurement row
    val savedRowMinHeight = 64.dp
    val iconTile = 48.dp               // Garment icon tile
    val iconTileSmall = 40.dp
    val iconSize = 24.dp
    val garmentIconSize = 30.dp

    // Inputs
    val textFieldHeight = 56.dp
    val numberFieldHeight = 72.dp      // Large number field
    val stepperButtonWidth = 56.dp     // − / + buttons

    // Progress
    val stepBarHeight = 6.dp
    val progressBarHeight = 12.dp

    // Sheets/dialogs
    val sheetHandleWidth = 36.dp
    val sheetHandleHeight = 4.dp
    val dialogSideMargin = 20.dp
    val scrimAlpha = 0.5f
}

/** Corner radii. Buttons and chips are fully rounded (pill). */
val MaapShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),   // Checkbox
    small = RoundedCornerShape(10.dp),        // Badge
    medium = RoundedCornerShape(14.dp),       // Inputs, small cards, banners
    large = RoundedCornerShape(16.dp),        // Cards
    extraLarge = RoundedCornerShape(28.dp),   // Dialogs, bottom sheets (top two corners)
)

val PillShape = RoundedCornerShape(percent = 50)
val SheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
val PdfCardShape = RoundedCornerShape(10.dp)
