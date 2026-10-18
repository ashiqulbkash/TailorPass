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
    Bottom("m_bottom", isPreference = true, minInches = 8f, maxInches = 18f),

    // Shirt, pants and pajama (Project Overview §4; how they are measured: plan.md §3 "Garment data sources").
    // Ranges contain the ANSUR II men's 5th–95th percentiles (US Army body data, inches) with room below for
    // smaller builds: biceps flexed 11.9–16.5, thigh 20.9–28.5, above-knee 14.1–18.2, crotch length 21.6–27.6,
    // crotch height 30.4–36.4, waist height 38.3–45.1.
    Biceps("m_biceps", minInches = 9f, maxInches = 20f),                                  // Fullest part of the upper arm
    ShirtLength("m_length", isPreference = true, minInches = 24f, maxInches = 36f),       // Shirt "ঝুল": number entry, no cards
    Thigh("m_thigh", minInches = 16f, maxInches = 34f),                                   // Fullest part, just below the crotch
    Knee("m_knee", minInches = 12f, maxInches = 22f),
    Crotch("m_crotch", minInches = 18f, maxInches = 32f),                                 // হাই: navel, between the legs, to the back waist
    Inseam("m_inseam", needsHelper = true, minInches = 24f, maxInches = 38f),             // Crotch down the inside leg to the hem
    FullLength("m_full_length", needsHelper = true, minInches = 32f, maxInches = 46f);   // Waist down the outside leg to the hem

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

    /** Shirt (Overview §4): 9 measurements; the shirt waist is the belly, as on the panjabi. */
    val shirt = listOf(
        MeasurementSection(null, listOf(
            MeasurementKey.Neck, MeasurementKey.Chest, MeasurementKey.WaistBelly, MeasurementKey.Hip,
            MeasurementKey.Shoulder, MeasurementKey.Sleeve, MeasurementKey.Biceps, MeasurementKey.Cuff,
            MeasurementKey.ShirtLength,
        ))
    )

    val shirtManualOrder = listOf(
        MeasurementSection("manual_body_measures", listOf(
            MeasurementKey.Neck, MeasurementKey.Shoulder, MeasurementKey.Chest, MeasurementKey.WaistBelly,
            MeasurementKey.Hip, MeasurementKey.Sleeve, MeasurementKey.Biceps,
        )),
        MeasurementSection("manual_pref_measures", listOf(MeasurementKey.Cuff, MeasurementKey.ShirtLength)),
    )

    /** Pants (Overview §4): 8 measurements. */
    val pants = listOf(
        MeasurementSection(null, listOf(
            MeasurementKey.Waist, MeasurementKey.Hip, MeasurementKey.Thigh, MeasurementKey.Knee,
            MeasurementKey.Crotch, MeasurementKey.Inseam, MeasurementKey.FullLength, MeasurementKey.Bottom,
        ))
    )

    val pantsManualOrder = listOf(
        MeasurementSection("manual_body_measures", listOf(
            MeasurementKey.Waist, MeasurementKey.Hip, MeasurementKey.Crotch, MeasurementKey.Thigh,
            MeasurementKey.Knee, MeasurementKey.Inseam, MeasurementKey.FullLength,
        )),
        MeasurementSection("manual_pref_measures", listOf(MeasurementKey.Bottom)),
    )

    /** Pajama (Overview §4): 5 measurements. */
    val pajama = listOf(
        MeasurementSection(null, listOf(
            MeasurementKey.Waist, MeasurementKey.Hip, MeasurementKey.Thigh, MeasurementKey.FullLength,
            MeasurementKey.Bottom,
        ))
    )

    val pajamaManualOrder = listOf(
        MeasurementSection("manual_body_measures", listOf(
            MeasurementKey.Waist, MeasurementKey.Hip, MeasurementKey.Thigh, MeasurementKey.FullLength,
        )),
        MeasurementSection("manual_pref_measures", listOf(MeasurementKey.Bottom)),
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

/** A chart row a size is suggested from: its chest (women: bust) range in inches, body measurements. */
interface ChestRange {
    val size: String
    val chestMin: Float
    val chestMax: Float
}

/** A body-measurement chart row (inches). Women's chart: chest = bust. */
data class SizeRow(
    override val size: String,
    override val chestMin: Float, override val chestMax: Float,
    val waistMin: Float, val waistMax: Float,
    val hipMin: Float, val hipMax: Float,
) : ChestRange

/**
 * Men's size chart (XS–XXXL), body measurements in inches. Chest: ASOS men's shirt chart; waist: ASOS men's chart
 * (the sample chart already used both); hip: James Perse men's body chart (low hip) ±1", XXXL continued by its 2"
 * step. Sources: plan.md §3 "Garment data sources". ⚠ Western brand charts — check with a tailor (Overview §7).
 */
val MenChart = listOf(
    SizeRow("XS", 34f, 36f, 28f, 30f, 33f, 35f),
    SizeRow("S", 36f, 38f, 30f, 32f, 35f, 37f),
    SizeRow("M", 38f, 40f, 32f, 34f, 37f, 39f),
    SizeRow("L", 40f, 42f, 34f, 36f, 39f, 41f),
    SizeRow("XL", 42f, 44f, 36f, 38f, 41f, 43f),
    SizeRow("XXL", 44f, 46f, 38f, 40f, 43f, 45f),
    SizeRow("XXXL", 46f, 48f, 40f, 42f, 45f, 47f),
)

/**
 * Women's size chart (XS–XXXL), body measurements in inches: bust (in the chest fields), natural waist, hip.
 * Source: Lord & Taylor women's size guide (plan.md §3 "Garment data sources"). ⚠ A western brand chart — check it
 * against Bangladeshi kameez sizing with a tailor (Overview §7).
 */
val WomenChart = listOf(
    SizeRow("XS", 31f, 32f, 23f, 24f, 34f, 35f),
    SizeRow("S", 33f, 35f, 25f, 27f, 36f, 37f),
    SizeRow("M", 35.5f, 37f, 27f, 29f, 38f, 39f),
    SizeRow("L", 37.5f, 40f, 29.5f, 32f, 40f, 42f),
    SizeRow("XL", 40.5f, 43f, 32.5f, 35f, 43f, 44.5f),
    SizeRow("XXL", 43.5f, 45f, 35.5f, 37f, 45f, 46.5f),
    SizeRow("XXXL", 45.5f, 47f, 37.5f, 39f, 47f, 48.5f),
)

/**
 * Genji / T-shirt row: body chest range for the suggestion, and the shirt's own shoulder and length (inches).
 */
data class TShirtSizeRow(
    override val size: String,
    override val chestMin: Float, override val chestMax: Float,
    val shoulder: Float,
    val length: Float,
) : ChestRange

/**
 * Genji / T-shirt chart (S–XXL). Shoulder and length: Urturms regular T-shirt chart (a South Asian brand); body
 * chest: the brand's chest less 0–2" ease, which gives the 9.4 design's ranges (M 38–40, L 40–42, XL 42–44).
 * Source details: plan.md §3 "Garment data sources".
 */
val TShirtChart = listOf(
    TShirtSizeRow("S", 36f, 38f, 17f, 27f),
    TShirtSizeRow("M", 38f, 40f, 17.5f, 27.5f),
    TShirtSizeRow("L", 40f, 42f, 18f, 28f),
    TShirtSizeRow("XL", 42f, 44f, 18.5f, 28.5f),
    TShirtSizeRow("XXL", 44f, 46f, 19f, 29f),
)

/** Chart size from the chest measurement (a value on a boundary gets the larger size). */
fun suggestSize(chestInches: Float, chart: List<ChestRange> = MenChart): String? =
    pickSize(chestInches, chart, { it.chestMin }, { it.chestMax })?.size

/** Chart size from the waist (pajama: drawstring, sized S–XXXL like tops); same rule as [suggestSize]. */
fun suggestSizeByWaist(waistInches: Float, chart: List<SizeRow> = MenChart): String? =
    pickSize(waistInches, chart, { it.waistMin }, { it.waistMax })?.size

/**
 * Trouser size = the waist number in inches (ASOS trousers: W28, W30 … — plan.md §3): the smallest even size that
 * fits the waist, 26–44. Null outside that range.
 */
fun suggestTrouserSize(waistInches: Float): String? {
    if (waistInches < TrouserWaistSizes.first() - 2) return null
    return TrouserWaistSizes.firstOrNull { it >= waistInches }?.toString()
}

val TrouserWaistSizes = (26..44 step 2).toList()

private fun <T> pickSize(value: Float, chart: List<T>, min: (T) -> Float, max: (T) -> Float): T? =
    chart.firstOrNull { value >= min(it) && value < max(it) } ?: chart.lastOrNull { value >= max(it) }

/**
 * Genji size from chest and shoulder (Overview §4): the chest's size, one size up while the shirt's shoulder is
 * narrower than the person's. Length is not used — a longer or shorter genji is a preference.
 */
fun suggestTShirtSize(chestInches: Float, shoulderInches: Float, chart: List<TShirtSizeRow> = TShirtChart): String? {
    val bySize = suggestSize(chestInches, chart) ?: return null
    val start = chart.indexOfFirst { it.size == bySize }
    return chart.drop(start).firstOrNull { it.shoulder >= shoulderInches }?.size ?: chart.last().size
}
