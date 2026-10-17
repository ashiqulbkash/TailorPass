package com.tech_hunt.tailorpass.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.hind_siliguri_bold
import com.tech_hunt.tailorpass.resources.hind_siliguri_medium
import com.tech_hunt.tailorpass.resources.hind_siliguri_regular
import com.tech_hunt.tailorpass.resources.hind_siliguri_semibold
import org.jetbrains.compose.resources.Font

/** Hind Siliguri from composeResources/font/ (see FONTS.md). Font() is composable in CMP, so this is too. */
@Composable
fun hindSiliguri(): FontFamily = FontFamily(
    Font(Res.font.hind_siliguri_regular, FontWeight.Normal),
    Font(Res.font.hind_siliguri_medium, FontWeight.Medium),
    Font(Res.font.hind_siliguri_semibold, FontWeight.SemiBold),
    Font(Res.font.hind_siliguri_bold, FontWeight.Bold),
)

/**
 * Type scale. Rule: body text at least 16sp; measurement numbers large and bold.
 * lineHeight is a bit larger (≈1.3–1.5×) to fit Bangla conjuncts.
 */
@Composable
fun maapTypography(): Typography {
    val f = hindSiliguri()
    return Typography(
        displayLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 88.sp, lineHeight = 92.sp),   // Genji size "L"
        displayMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 54.sp, lineHeight = 58.sp),  // Suggested size
        headlineLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 34.sp),  // Onboarding/language title
        headlineMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp), // Measurement name (manual entry)
        headlineSmall = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 29.sp),  // Dialog/sheet title
        titleLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 21.sp, lineHeight = 26.sp),     // App bar title
        titleMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 24.sp),    // Section header
        titleSmall = TextStyle(fontFamily = f, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 22.sp), // List row name
        bodyLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Normal, fontSize = 17.sp, lineHeight = 26.sp),
        bodyMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),   // Body text (minimum)
        bodySmall = TextStyle(fontFamily = f, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),    // Dates, secondary text
        labelLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 22.sp),     // Primary button
        labelMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 18.sp),// Tab label
        labelSmall = TextStyle(fontFamily = f, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 17.sp), // Badge
    )
}

/** Separate styles for measurement numbers. */
@Immutable
data class MaapNumberStyles(
    val inputValue: TextStyle,   // Number input field (40)
    val listValue: TextStyle,    // Verify list (40")
    val compactValue: TextStyle, // Result table
)

@Composable
fun maapNumberStyles(): MaapNumberStyles {
    val f = hindSiliguri()
    return MaapNumberStyles(
        inputValue = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 44.sp, lineHeight = 48.sp),
        listValue = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 28.sp),
        compactValue = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 22.sp),
    )
}
