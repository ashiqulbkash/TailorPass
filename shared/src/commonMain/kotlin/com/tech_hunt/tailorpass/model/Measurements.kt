package com.tech_hunt.tailorpass.model

/**
 * Platform-independent measurement definitions (shared/commonMain).
 *
 * No display text here, only keys:
 *  - composeResources/values/strings.xml has a <string> with the same name → str(key) in the UI
 *  - the same Compose UI runs on Android and iOS
 *
 * Plausible range (minInches..maxInches): for the "Is this measurement right?" warning (inches, adults).
 * ⚠ These are rough starting values — adjust them from real data.
 */
enum class MeasurementKey(
    val labelKey: String,
    val isPreference: Boolean = false,
    val needsHelper: Boolean = false,
    val minInches: Float,
    val maxInches: Float,
) {
    Neck("m_neck", minInches = 12f, maxInches = 20f),
    Shoulder("m_shoulder", needsHelper = true, minInches = 12f, maxInches = 22f),
    Chest("m_chest", minInches = 30f, maxInches = 50f),
    Bust("m_bust", minInches = 28f, maxInches = 50f),
    WaistBelly("m_waist_belly", minInches = 24f, maxInches = 50f),   // Panjabi "belly"
    Waist("m_waist", minInches = 22f, maxInches = 48f),              // Kameez/salwar "waist"
    Hip("m_hip", minInches = 30f, maxInches = 54f),
    Armhole("m_armhole", minInches = 12f, maxInches = 24f),
    Sleeve("m_sleeve", needsHelper = true, minInches = 14f, maxInches = 30f),
    Cuff("m_cuff", isPreference = true, minInches = 6f, maxInches = 14f),
    Length("m_length", isPreference = true, minInches = 26f, maxInches = 50f),
    NeckDepthFront("m_neck_front", isPreference = true, minInches = 3f, maxInches = 12f),
    NeckDepthBack("m_neck_back", isPreference = true, minInches = 2f, maxInches = 10f),
    SalwarLength("m_salwar_length", minInches = 32f, maxInches = 44f),
    Bottom("m_bottom", isPreference = true, minInches = 8f, maxInches = 18f);

    fun isPlausible(inches: Float): Boolean = inches in minInches..maxInches

    /** Everything except preference measurements can be measured by the AI from photos. */
    val aiMeasurable: Boolean get() = !isPreference
}

enum class Accuracy { High, Medium, Low }

/** Which badge a list row shows — the 6 kinds in the design. key = string resource name. */
enum class SourceBadge(val labelKey: String) {
    AccuracyHigh("accuracy_high"),
    AccuracyMedium("accuracy_mid"),
    AccuracyLow("accuracy_low"),
    YourChoice("badge_your_choice"),
    TapeMeasured("badge_tape_measured"),
    Missing("badge_missing_tailor"),
}

enum class MeasureMethod { Ai, Tape }

enum class Garment(
    val labelKey: String,
    val iconName: String,         // Drawable name in composeResources
    val forWomen: Boolean = false,
    val sizeOnly: Boolean = false,
) {
    Shirt("garment_shirt", "ic_garment_shirt"),
    Pants("garment_pants", "ic_garment_pants"),
    Panjabi("garment_panjabi", "ic_garment_panjabi"),
    Pajama("garment_pajama", "ic_garment_pajama"),
    SalwarKameez("garment_salwar_kameez", "ic_garment_salwar_kameez", forWomen = true),
    TShirt("garment_tshirt", "ic_garment_tshirt", sizeOnly = true),   // Genji: size suggestion only
}

/** A garment's measurements, in one or more sections (e.g. kameez + salwar). titleKey null = no heading. */
data class MeasurementSection(val titleKey: String?, val keys: List<MeasurementKey>)

object GarmentMeasurements {
    /** Order on result/verify/PDF (diagram numbers follow this order). */
    val panjabi = listOf(
        MeasurementSection(null, listOf(
            MeasurementKey.Length, MeasurementKey.Chest, MeasurementKey.WaistBelly, MeasurementKey.Hip,
            MeasurementKey.Shoulder, MeasurementKey.Sleeve, MeasurementKey.Cuff, MeasurementKey.Neck,
        ))
    )

    /** Tape measuring order: top of the body to bottom, preference measurements last. */
    val panjabiManualOrder = listOf(
        MeasurementSection("manual_body_measures", listOf(
            MeasurementKey.Neck, MeasurementKey.Shoulder, MeasurementKey.Chest,
            MeasurementKey.WaistBelly, MeasurementKey.Hip, MeasurementKey.Sleeve,
        )),
        MeasurementSection("manual_pref_measures", listOf(MeasurementKey.Cuff, MeasurementKey.Length)),
    )

    val salwarKameez = listOf(
        MeasurementSection("garment_kameez", listOf(
            MeasurementKey.Bust, MeasurementKey.Waist, MeasurementKey.Hip, MeasurementKey.Shoulder,
            MeasurementKey.Armhole, MeasurementKey.Sleeve, MeasurementKey.Cuff,
            MeasurementKey.NeckDepthFront, MeasurementKey.NeckDepthBack, MeasurementKey.Length,
        )),
        MeasurementSection("garment_salwar", listOf(
            MeasurementKey.Waist, MeasurementKey.Hip, MeasurementKey.SalwarLength, MeasurementKey.Bottom,
        )),
    )
}

/** Length preset cards (7.6). Real values will be computed from height; these are samples for 5'7". */
enum class LengthPreset(val labelKey: String, val sampleInches: Float) {
    AboveKnee("length_above_knee", 38f),
    AtKnee("length_at_knee", 42f),
    BelowKnee("length_below_knee", 45f),
}

/** Neck depth preset cards (7.10). */
enum class NeckDepthPreset(val labelKey: String, val sampleInches: Float) {
    Low("depth_low", 5f),
    Medium("depth_mid", 7f),
    High("depth_high", 9f),
}

enum class Fit(val labelKey: String) { Slim("fit_slim"), Regular("fit_regular"), Loose("fit_loose") }
enum class Gender(val labelKey: String) { Male("gender_male"), Female("gender_female") }

/** Sample size chart (men's shirt, inches). ⚠ Replace with the real chart. */
data class SizeRow(
    val size: String,
    val chestMin: Float, val chestMax: Float,
    val waistMin: Float, val waistMax: Float,
    val hipMin: Float, val hipMax: Float,
)

val SampleMenShirtChart = listOf(
    SizeRow("XS", 34f, 36f, 28f, 30f, 34f, 36f),
    SizeRow("S", 36f, 38f, 30f, 32f, 36f, 38f),
    SizeRow("M", 38f, 40f, 32f, 34f, 38f, 40f),
    SizeRow("L", 40f, 42f, 34f, 36f, 40f, 42f),
    SizeRow("XL", 42f, 44f, 36f, 38f, 42f, 44f),
    SizeRow("XXL", 44f, 46f, 38f, 40f, 44f, 46f),
    SizeRow("XXXL", 46f, 48f, 40f, 42f, 46f, 48f),
)

/** Chart size from the chest measurement (a value on a boundary gets the larger size). */
fun suggestSize(chestInches: Float, chart: List<SizeRow> = SampleMenShirtChart): String? =
    chart.firstOrNull { chestInches >= it.chestMin && chestInches < it.chestMax }?.size
        ?: chart.lastOrNull { chestInches >= it.chestMax }?.size
