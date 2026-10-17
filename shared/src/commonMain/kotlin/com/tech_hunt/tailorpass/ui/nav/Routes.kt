package com.tech_hunt.tailorpass.ui.nav

import kotlinx.serialization.Serializable

/** Type-safe routes (navigation.json names). Overlays (sheets/dialogs) are not routes. */
@Serializable data object LanguageRoute

/** Onboarding1–3 are pages of one pager, so they share one route. */
@Serializable data object OnboardingRoute

@Serializable data object ProfileCreateRoute

/** Home tab (2.1–2.3; ProfileSheet and SavedMenuSheet are overlays on it). */
@Serializable data object MainRoute

/** Bottom-nav tabs — placeholders until Step 9. */
@Serializable data object SizeChartRoute

@Serializable data object SettingsRoute

/** Choose method (Step 3 placeholder). [garment] = Garment.name. */
@Serializable data class MethodRoute(val garment: String)

/** Genji size suggestion (Step 8 placeholder). */
@Serializable data object GenjiResultRoute

/** Result of a saved measurement (Step 8 placeholder). */
@Serializable data class ResultRoute(val savedId: Long)

/** Verify/edit a saved measurement (Step 7 placeholder). */
@Serializable data class VerifyRoute(val savedId: Long)
