package com.tech_hunt.tailorpass

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.navigation.compose.rememberNavController
import com.tech_hunt.tailorpass.ui.nav.AppNavHost
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.LocalAppLocale
import com.tech_hunt.tailorpass.ui.util.appLanguageOf
import com.tech_hunt.tailorpass.ui.util.setAppLanguage

/** The app's only UI root — shown by Android (MainActivity) and iOS (MainViewController, Step 11). */
@Composable
fun App() {
    var language by remember { mutableStateOf(appLanguageOf(Locale.current.toLanguageTag())) }
    // Outside key(language), so the back stack survives a language change.
    val navController = rememberNavController()
    CompositionLocalProvider(LocalAppLocale provides language) {
        key(language) {
            MaapTheme {
                AppNavHost(
                    navController = navController,
                    onLanguageChosen = { tag ->
                        setAppLanguage(tag)
                        language = tag
                    },
                )
            }
        }
    }
}
