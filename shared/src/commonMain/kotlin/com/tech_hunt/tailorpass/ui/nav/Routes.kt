package com.tech_hunt.tailorpass.ui.nav

import kotlinx.serialization.Serializable

/** Type-safe routes (navigation.json names). Overlays (sheets/dialogs) are not routes. */
@Serializable data object LanguageRoute

/** Onboarding1–3 are pages of one pager, so they share one route. */
@Serializable data object OnboardingRoute

@Serializable data object ProfileCreateRoute

/** Home — placeholder until Step 2. */
@Serializable data object MainRoute
