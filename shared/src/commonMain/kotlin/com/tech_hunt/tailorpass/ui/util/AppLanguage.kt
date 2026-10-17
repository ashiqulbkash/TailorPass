package com.tech_hunt.tailorpass.ui.util

import androidx.compose.runtime.staticCompositionLocalOf

/** App languages: Bangla (default) or English. BCP-47 tags. */
const val LANGUAGE_BN = "bn"
const val LANGUAGE_EN = "en"

/** Any language tag → one of the two app languages (Bangla unless it is English). */
fun appLanguageOf(tag: String): String =
    if (tag.substringBefore('-').lowercase() == LANGUAGE_EN) LANGUAGE_EN else LANGUAGE_BN

/**
 * Current app language, provided in App(). Changing it recomposes the UI immediately.
 * For numbers: formatInches(v, useBangla = LocalAppLocale.current == LANGUAGE_BN).
 */
val LocalAppLocale = staticCompositionLocalOf { LANGUAGE_BN }

/** Changes the platform app language (Android: AppCompatDelegate; iOS: Step 11). */
expect fun setAppLanguage(tag: String)
