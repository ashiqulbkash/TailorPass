package com.tech_hunt.tailorpass.model

/**
 * নম্বরসহ শরীরের ডায়াগ্রামের ডেটা (প্ল্যাটফর্ম-নিরপেক্ষ)।
 * ছবি দুই স্তরে: outline (diagramOutline রঙে) + marks (primary রঙে)।
 * নম্বরের বৃত্ত প্রতিটি প্ল্যাটফর্ম নিজে আঁকে, নিচের স্থানাঙ্কে (viewport ইউনিটে)।
 *
 * Android: R.drawable.<outlineName>,   iOS: Image(outlineName) — একই নাম।
 */
data class Badge(val number: Int, val x: Float, val y: Float)

data class DiagramSpec(
    val outlineName: String,
    val marksName: String,
    val viewportWidth: Float,
    val viewportHeight: Float,
    /** দেখানোর অংশ (viewport ইউনিটে)। পুরো দেখাতে 0..viewportHeight। */
    val cropTop: Float,
    val cropBottom: Float,
    val badges: List<Badge>,
)

object Diagrams {
    /** পাঞ্জাবি: ১ ঝুল, ২ বুক, ৩ পেট, ৪ হিপ, ৫ পুট, ৬ হাতা, ৭ হাতার মুখ, ৮ গলা */
    val panjabi = DiagramSpec(
        outlineName = "body_male_upper", marksName = "diagram_panjabi_marks",
        viewportWidth = 200f, viewportHeight = 250f, cropTop = 0f, cropBottom = 250f,
        badges = listOf(
            Badge(1, 188f, 150f), Badge(2, 100f, 112f), Badge(3, 100f, 158f), Badge(4, 100f, 204f),
            Badge(5, 125f, 80f), Badge(6, 20f, 140f), Badge(7, 12f, 240f), Badge(8, 132f, 44f),
        ),
    )

    /** কামিজ: ১ বুক, ২ কোমর, ৩ হিপ, ৪ পুট, ৫ আর্মহোল, ৬ হাতা, ৭ হাতার মুখ, ৮ গলা সামনে, ৯ গলা পেছনে, ১০ ঝুল */
    val kameez = DiagramSpec(
        outlineName = "body_female_front", marksName = "diagram_kameez_marks",
        viewportWidth = 200f, viewportHeight = 520f, cropTop = 60f, cropBottom = 360f,
        badges = listOf(
            Badge(1, 100f, 170f), Badge(2, 100f, 232f), Badge(3, 100f, 300f), Badge(4, 72f, 100f),
            Badge(5, 160f, 118f), Badge(6, 32f, 196f), Badge(7, 40f, 322f), Badge(8, 100f, 136f),
            Badge(9, 130f, 80f), Badge(10, 180f, 230f),
        ),
    )

    /** সালওয়ার: ১ কোমর, ২ হিপ, ৩ মোট লম্বা, ৪ মোহরি */
    val salwar = DiagramSpec(
        outlineName = "body_female_front", marksName = "diagram_salwar_marks",
        viewportWidth = 200f, viewportHeight = 520f, cropTop = 210f, cropBottom = 510f,
        badges = listOf(Badge(1, 100f, 232f), Badge(2, 100f, 300f), Badge(3, 170f, 370f), Badge(4, 46f, 478f)),
    )
}
