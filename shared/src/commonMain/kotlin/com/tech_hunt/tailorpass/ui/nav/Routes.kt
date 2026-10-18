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

/** Size chart tab (11.1). */
@Serializable data object SizeChartRoute

/** Settings tab (12.1; 12.2–12.4 are overlays on it). */
@Serializable data object SettingsRoute

/** Privacy policy (12.5), from Settings and About. */
@Serializable data object PrivacyPolicyRoute

/** About the app (12.6). */
@Serializable data object AboutRoute

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

/** Verify a new measurement (8.1–8.5), after the photos or the tape summary. [method] = MeasureMethod.name. */
@Serializable data class VerifyNewRoute(val garment: String, val method: String)

/** Tape-measure preparation (7.1). */
@Serializable data class ManualPrepRoute(val garment: String)

/** Tape measurement list (7.2). */
@Serializable data class ManualListRoute(val garment: String)

/** One tape measurement (7.3 / 7.4 / 7.10; 7.5 is a sheet on it). The current step lives in the flow state. */
@Serializable data class ManualEntryRoute(val garment: String)

/** Tape summary (7.7). */
@Serializable data class ManualSummaryRoute(val garment: String)

/** Genji size suggestion. */
@Serializable data object GenjiResultRoute

/** Result of a saved measurement. */
@Serializable data class ResultRoute(val savedId: Long)

/** Result of a new measurement after Verify. */
@Serializable data class ResultNewRoute(val garment: String)

/** Verify/edit a saved measurement (8.1–8.5 with the saved values). */
@Serializable data class VerifyRoute(val savedId: Long)
