package com.tech_hunt.tailorpass.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.tech_hunt.tailorpass.R

/** Hind Siliguri — androidApp/src/main/res/font/ থেকে (FONTS.md দেখুন)। */
private val HindSiliguri = FontFamily(
    Font(R.font.hind_siliguri_regular, FontWeight.Normal),
    Font(R.font.hind_siliguri_medium, FontWeight.Medium),
    Font(R.font.hind_siliguri_semibold, FontWeight.SemiBold),
    Font(R.font.hind_siliguri_bold, FontWeight.Bold),
)

fun hindSiliguri(): FontFamily = HindSiliguri

/**
 * টাইপ স্কেল। নিয়ম: মূল লেখা কমপক্ষে ১৬sp, মাপের সংখ্যা বড় ও মোটা।
 * বাংলা যুক্তাক্ষরের জন্য lineHeight একটু বেশি রাখা হয়েছে (≈১.৩–১.৫×)।
 */
@Composable
fun maapTypography(): Typography {
    val f = hindSiliguri()
    return Typography(
        displayLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 88.sp, lineHeight = 92.sp),   // গেঞ্জির সাইজ "L"
        displayMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 54.sp, lineHeight = 58.sp),  // সাজেস্ট করা সাইজ
        headlineLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 34.sp),  // অনবোর্ডিং/ভাষা শিরোনাম
        headlineMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp), // মাপের নাম (ম্যানুয়াল এন্ট্রি)
        headlineSmall = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 29.sp),  // ডায়ালগ/শিট শিরোনাম
        titleLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 21.sp, lineHeight = 26.sp),     // অ্যাপ বার শিরোনাম
        titleMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 24.sp),    // সেকশন হেডার
        titleSmall = TextStyle(fontFamily = f, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 22.sp), // তালিকার সারির নাম
        bodyLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Normal, fontSize = 17.sp, lineHeight = 26.sp),
        bodyMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),   // মূল লেখা (ন্যূনতম)
        bodySmall = TextStyle(fontFamily = f, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),    // তারিখ, সাব-লেখা
        labelLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 22.sp),     // প্রধান বোতাম
        labelMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 18.sp),// ট্যাব লেবেল
        labelSmall = TextStyle(fontFamily = f, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 17.sp), // ব্যাজ
    )
}

/** মাপের সংখ্যার জন্য আলাদা স্টাইল। */
@Immutable
data class MaapNumberStyles(
    val inputValue: TextStyle,   // সংখ্যার ঘর (৪০)
    val listValue: TextStyle,    // যাচাই তালিকা (৪০")
    val compactValue: TextStyle, // ফলাফল টেবিল
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
