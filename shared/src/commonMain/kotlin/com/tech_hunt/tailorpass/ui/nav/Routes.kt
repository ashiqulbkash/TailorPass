package com.tech_hunt.tailorpass.ui.nav

import com.tech_hunt.tailorpass.ui.screens.capture.CameraShot
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

/** Choose method (3.1). [garment] = Garment.name in all measuring-flow routes; the profile is Home's current one. */
@Serializable data class MethodRoute(val garment: String)

/** Camera permission explanation (3.2). */
@Serializable data class CameraPermissionRoute(val garment: String)

/** Preparation checklist (4.1 / 4.2 by gender/garment). */
@Serializable data class ChecklistRoute(val garment: String)

/** Pose guide (4.3 / 4.4 by gender/garment). */
@Serializable data class PoseGuideRoute(val garment: String)

/** Camera (5.1–5.4). [shot] = CameraShot.name. */
@Serializable data class CameraRoute(val garment: String, val shot: String = CameraShot.Front.name)

/** Both photos accepted, measuring in progress (6.1). */
@Serializable data class PhotoCheckRoute(val garment: String)

/** A photo was rejected (6.2). [failedShot] = CameraShot.name of the photo to retake. */
@Serializable data class PhotoProblemRoute(val garment: String, val failedShot: String)

/** The AI could not measure (6.3). */
@Serializable data class AiFailedRoute(val garment: String)

/** Verify a new measurement (Step 7 placeholder). */
@Serializable data class VerifyNewRoute(val garment: String)

/** Tape-measure preparation (Step 5 placeholder). */
@Serializable data class ManualPrepRoute(val garment: String)

/** Genji size suggestion (Step 8 placeholder). */
@Serializable data object GenjiResultRoute

/** Result of a saved measurement (Step 8 placeholder). */
@Serializable data class ResultRoute(val savedId: Long)

/** Verify/edit a saved measurement (Step 7 placeholder). */
@Serializable data class VerifyRoute(val savedId: Long)
