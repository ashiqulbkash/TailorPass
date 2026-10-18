package com.tech_hunt.tailorpass.model

/**
 * Data for the numbered body diagrams (platform-independent).
 * The image has two layers: outline (diagramOutline color) + marks (primary color).
 * The numbered circles are drawn by the UI at the coordinates below (in viewport units).
 *
 * Names are drawable names in composeResources (resolved with drawable(name)).
 */
data class Badge(val number: Int, val x: Float, val y: Float)

data class DiagramSpec(
    val outlineName: String,
    val marksName: String,
    val viewportWidth: Float,
    val viewportHeight: Float,
    /** Visible part (in viewport units). Use 0..viewportHeight to show everything. */
    val cropTop: Float,
    val cropBottom: Float,
    val badges: List<Badge>,
)

object Diagrams {
    /** Panjabi: 1 length, 2 chest, 3 belly, 4 hip, 5 shoulder, 6 sleeve, 7 cuff, 8 neck */
    val panjabi = DiagramSpec(
        outlineName = "body_male_upper", marksName = "diagram_panjabi_marks",
        viewportWidth = 200f, viewportHeight = 250f, cropTop = 0f, cropBottom = 250f,
        badges = listOf(
            Badge(1, 188f, 150f), Badge(2, 100f, 112f), Badge(3, 100f, 158f), Badge(4, 100f, 204f),
            Badge(5, 125f, 80f), Badge(6, 20f, 140f), Badge(7, 12f, 240f), Badge(8, 132f, 44f),
        ),
    )

    /** Kameez: 1 bust, 2 waist, 3 hip, 4 shoulder, 5 armhole, 6 sleeve, 7 cuff, 8 front neck depth, 9 back neck depth, 10 length */
    val kameez = DiagramSpec(
        outlineName = "body_female_front", marksName = "diagram_kameez_marks",
        viewportWidth = 200f, viewportHeight = 520f, cropTop = 60f, cropBottom = 360f,
        badges = listOf(
            Badge(1, 100f, 170f), Badge(2, 100f, 232f), Badge(3, 100f, 300f), Badge(4, 72f, 100f),
            Badge(5, 160f, 118f), Badge(6, 32f, 196f), Badge(7, 40f, 322f), Badge(8, 100f, 136f),
            Badge(9, 130f, 80f), Badge(10, 180f, 230f),
        ),
    )

    /** Salwar: 1 waist, 2 hip, 3 full length, 4 bottom opening */
    val salwar = DiagramSpec(
        outlineName = "body_female_front", marksName = "diagram_salwar_marks",
        viewportWidth = 200f, viewportHeight = 520f, cropTop = 210f, cropBottom = 510f,
        badges = listOf(Badge(1, 100f, 232f), Badge(2, 100f, 300f), Badge(3, 170f, 370f), Badge(4, 46f, 478f)),
    )

    /** Shirt: 1 neck, 2 chest, 3 waist, 4 hip, 5 shoulder, 6 sleeve, 7 biceps, 8 cuff, 9 length (panjabi marks, shorter length) */
    val shirt = DiagramSpec(
        outlineName = "body_male_upper", marksName = "diagram_shirt_marks",
        viewportWidth = 200f, viewportHeight = 250f, cropTop = 0f, cropBottom = 250f,
        badges = listOf(
            Badge(1, 132f, 44f), Badge(2, 100f, 112f), Badge(3, 100f, 158f), Badge(4, 100f, 204f),
            Badge(5, 125f, 80f), Badge(6, 20f, 140f), Badge(7, 50f, 118f), Badge(8, 12f, 240f), Badge(9, 188f, 150f),
        ),
    )

    /** Pants: 1 waist, 2 hip, 3 thigh, 4 knee, 5 crotch (হাই, drawn as its front depth), 6 inseam, 7 full length, 8 bottom opening */
    val pants = DiagramSpec(
        outlineName = "body_male_front", marksName = "diagram_pants_marks",
        viewportWidth = 200f, viewportHeight = 520f, cropTop = 200f, cropBottom = 510f,
        badges = listOf(
            Badge(1, 100f, 226f), Badge(2, 100f, 270f), Badge(3, 40f, 322f), Badge(4, 42f, 402f),
            Badge(5, 158f, 263f), Badge(6, 120f, 400f), Badge(7, 186f, 440f), Badge(8, 46f, 476f),
        ),
    )

    /** Pajama: 1 waist, 2 hip, 3 thigh, 4 full length, 5 bottom opening */
    val pajama = DiagramSpec(
        outlineName = "body_male_front", marksName = "diagram_pajama_marks",
        viewportWidth = 200f, viewportHeight = 520f, cropTop = 200f, cropBottom = 510f,
        badges = listOf(Badge(1, 100f, 226f), Badge(2, 100f, 270f), Badge(3, 40f, 322f), Badge(4, 170f, 370f), Badge(5, 46f, 476f)),
    )
}
