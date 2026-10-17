package com.tech_hunt.tailorpass.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** স্পেসিং ও আকার — ডিজাইনের মাপ (৩৬০dp প্রস্থের ফোন ধরে)। */
object Dimens {
    // স্পেসিং
    val space4 = 4.dp
    val space6 = 6.dp
    val space8 = 8.dp
    val space10 = 10.dp
    val space12 = 12.dp
    val space14 = 14.dp
    val space16 = 16.dp
    val space20 = 20.dp
    val space24 = 24.dp

    val screenPadding = 16.dp          // দুই পাশের মার্জিন
    val minTouchTarget = 48.dp         // সব চাপা যায় এমন জিনিস ≥৪৮dp

    // বার
    val appBarHeight = 64.dp
    val bottomNavHeight = 80.dp
    val navIndicatorWidth = 64.dp
    val navIndicatorHeight = 32.dp

    // বোতাম
    val buttonHeight = 56.dp           // প্রধান পুরো-প্রস্থ বোতাম
    val buttonHeightCompact = 46.dp    // ফলাফল স্ক্রিনের দুই শেয়ার বোতাম
    val dialogButtonHeight = 52.dp
    val segmentedHeight = 44.dp        // ইঞ্চি/সেমি টগল

    // কার্ড ও তালিকা
    val garmentCardHeight = 80.dp
    val listRowMinHeight = 52.dp       // মাপ যাচাই সারি
    val savedRowMinHeight = 64.dp
    val iconTile = 48.dp               // পোশাকের আইকন টাইল
    val iconTileSmall = 40.dp
    val iconSize = 24.dp
    val garmentIconSize = 30.dp

    // ইনপুট
    val textFieldHeight = 56.dp
    val numberFieldHeight = 72.dp      // বড় সংখ্যার ঘর
    val stepperButtonWidth = 56.dp     // − / + বোতাম

    // অগ্রগতি
    val stepBarHeight = 6.dp
    val progressBarHeight = 12.dp

    // শিট/ডায়ালগ
    val sheetHandleWidth = 36.dp
    val sheetHandleHeight = 4.dp
    val dialogSideMargin = 20.dp
    val scrimAlpha = 0.5f
}

/** কোণের গোলাই। বোতাম ও চিপ পুরো গোল (pill)। */
val MaapShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),   // চেকবক্স
    small = RoundedCornerShape(10.dp),        // ব্যাজ
    medium = RoundedCornerShape(14.dp),       // ইনপুট, ছোট কার্ড, ব্যানার
    large = RoundedCornerShape(16.dp),        // কার্ড
    extraLarge = RoundedCornerShape(28.dp),   // ডায়ালগ, বটম শিট (উপরের দুই কোণ)
)

val PillShape = RoundedCornerShape(percent = 50)
val SheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
val PdfCardShape = RoundedCornerShape(10.dp)
