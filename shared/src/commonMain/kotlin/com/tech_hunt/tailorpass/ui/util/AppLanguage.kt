package com.tech_hunt.tailorpass.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.tech_hunt.tailorpass.util.toBanglaDigits

/** App languages: Bangla (default) or English. BCP-47 tags. */
const val LANGUAGE_BN = "bn"
const val LANGUAGE_EN = "en"

/** Any language tag → one of the two app languages (Bangla unless it is English). */
fun appLanguageOf(tag: String): String =
    if (tag.substringBefore('-').lowercase() == LANGUAGE_EN) LANGUAGE_EN else LANGUAGE_BN

/**
 * Current app language, provided in App(). Changing it recomposes the UI immediately.
 * For numbers: formatInches(v, useBangla = useBanglaDigits()) or localDigits(text).
 */
val LocalAppLocale = staticCompositionLocalOf { LANGUAGE_BN }

/** True when numbers are shown with Bengali digits (app language Bangla). */
@Composable
@ReadOnlyComposable
fun useBanglaDigits(): Boolean = LocalAppLocale.current == LANGUAGE_BN

/** Latin digits in [text] → Bengali digits when the app language is Bangla; unchanged in English. */
@Composable
@ReadOnlyComposable
fun localDigits(text: String): String = if (useBanglaDigits()) text.toBanglaDigits() else text

/** Changes the platform app language (Android: AppCompatDelegate; iOS: Step 11). */
expect fun setAppLanguage(tag: String)
