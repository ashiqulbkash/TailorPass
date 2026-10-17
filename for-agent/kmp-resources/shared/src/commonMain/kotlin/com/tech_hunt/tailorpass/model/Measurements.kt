package com.tech_hunt.tailorpass.model

/**
 * প্ল্যাটফর্ম-নিরপেক্ষ মাপের সংজ্ঞা (shared/commonMain)।
 *
 * লেখা এখানে রাখা হয়নি, শুধু key রাখা আছে:
 *  - Android: res/values/strings.xml-এ একই নামের <string> আছে → context.str(key)
 *  - iOS:     Localizable.xcstrings-এ একই key আছে → String(localized: …)
 *
 * plausibleRange: "মাপটা ঠিক আছে তো?" সতর্কতার জন্য (ইঞ্চি, প্রাপ্তবয়স্ক)।
 * ⚠ এগুলো শুরু করার মতো আনুমানিক মান — আসল ডেটা দেখে ঠিক করে নেবেন।
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
    WaistBelly("m_waist_belly", minInches = 24f, maxInches = 50f),   // পাঞ্জাবির "পেট"
    Waist("m_waist", minInches = 22f, maxInches = 48f),              // কামিজ/সালওয়ারের "কোমর"
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

    /** "সাহায্য দরকার?"/"পছন্দের মাপ" বাদে বাকিগুলো ছবি থেকে AI মাপতে পারে। */
    val aiMeasurable: Boolean get() = !isPreference
}

enum class Accuracy { High, Medium, Low }

/** তালিকার সারিতে কোন ব্যাজ দেখাবে — ডিজাইনের ৬ রকম। key = strings-এর নাম। */
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
    val iconName: String,         // Android: R.drawable.<iconName>, iOS: Image(iconName)
    val forWomen: Boolean = false,
    val sizeOnly: Boolean = false,
) {
    Shirt("garment_shirt", "ic_garment_shirt"),
    Pants("garment_pants", "ic_garment_pants"),
    Panjabi("garment_panjabi", "ic_garment_panjabi"),
    Pajama("garment_pajama", "ic_garment_pajama"),
    SalwarKameez("garment_salwar_kameez", "ic_garment_salwar_kameez", forWomen = true),
    TShirt("garment_tshirt", "ic_garment_tshirt", sizeOnly = true),   // গেঞ্জি: শুধু সাইজ সাজেশন
}

/** একটি পোশাকের মাপ, এক বা একাধিক ভাগে (যেমন কামিজ + সালওয়ার)। titleKey null = শিরোনাম নেই। */
data class MeasurementSection(val titleKey: String?, val keys: List<MeasurementKey>)

object GarmentMeasurements {
    /** ফলাফল/যাচাই/PDF-এর ক্রম (ডায়াগ্রামের নম্বর এই ক্রমে)। */
    val panjabi = listOf(
        MeasurementSection(null, listOf(
            MeasurementKey.Length, MeasurementKey.Chest, MeasurementKey.WaistBelly, MeasurementKey.Hip,
            MeasurementKey.Shoulder, MeasurementKey.Sleeve, MeasurementKey.Cuff, MeasurementKey.Neck,
        ))
    )

    /** ফিতা দিয়ে মাপার ক্রম: শরীরের উপর থেকে নিচে, পছন্দের মাপ শেষে। */
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

/** ঝুল বাছাই-কার্ড (৭.৬)। উচ্চতা অনুযায়ী মান অ্যাপে হিসাব হবে; এগুলো ৫'৭"-এর নমুনা। */
enum class LengthPreset(val labelKey: String, val sampleInches: Float) {
    AboveKnee("length_above_knee", 38f),
    AtKnee("length_at_knee", 42f),
    BelowKnee("length_below_knee", 45f),
}

/** গলার গভীরতা বাছাই-কার্ড (৭.১০)। */
enum class NeckDepthPreset(val labelKey: String, val sampleInches: Float) {
    Low("depth_low", 5f),
    Medium("depth_mid", 7f),
    High("depth_high", 9f),
}

enum class Fit(val labelKey: String) { Slim("fit_slim"), Regular("fit_regular"), Loose("fit_loose") }
enum class Gender(val labelKey: String) { Male("gender_male"), Female("gender_female") }

/** নমুনা সাইজ চার্ট (ছেলেদের শার্ট, ইঞ্চি)। ⚠ আসল চার্ট দিয়ে বদলাবেন। */
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

/** বুকের মাপ থেকে চার্টের সাইজ (সীমানায় পড়লে বড় সাইজ)। */
fun suggestSize(chestInches: Float, chart: List<SizeRow> = SampleMenShirtChart): String? =
    chart.firstOrNull { chestInches >= it.chestMin && chestInches < it.chestMax }?.size
        ?: chart.lastOrNull { chestInches >= it.chestMax }?.size
