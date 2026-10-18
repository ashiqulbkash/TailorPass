package com.tech_hunt.tailorpass.ui.screens.sizechart

import com.tech_hunt.tailorpass.model.ChestRange
import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.model.MenChart
import com.tech_hunt.tailorpass.model.SizeRow
import com.tech_hunt.tailorpass.model.TShirtChart
import com.tech_hunt.tailorpass.model.WomenChart
import com.tech_hunt.tailorpass.model.suggestSize
import com.tech_hunt.tailorpass.ui.screens.manual.MeasureUnit

/** One chart row: the size and one range per column (min == max shows a single value). */
data class ChartRow(val size: String, val cells: List<ClosedFloatingPointRange<Float>>)

/**
 * A size chart as the tab shows it: column header keys (after "সাইজ"), rows, and the chart the profile's size is
 * suggested from (chest; bust for women).
 */
data class ChartTable(val columnKeys: List<String>, val rows: List<ChartRow>, val suggestFrom: List<ChestRange>)

/**
 * Size chart tab (11.1): [gender] tab, [garment] chip and the chart, the current profile's row marked "আপনার".
 * [chestInches]: the profile's chest — bust for a woman (null = nothing measured yet, no row marked).
 * [unit]: the app's display unit. Men's shirt / panjabi / pants show the sample men's chart (Measurements.kt), genji
 * the T-shirt chart (chest, shoulder, length), women the women's chart (bust, waist, hip).
 * Design phase: a ViewModel replaces this in Step 10.
 */
data class SizeChartState(
    val profileName: String,
    val profileGender: Gender,
    val chestInches: Float?,
    val gender: Gender = profileGender,
    val garment: Garment = garmentsFor(gender).first(),
    val unit: MeasureUnit = MeasureUnit.Inch,
) {
    val garments: List<Garment> get() = garmentsFor(gender)
    val chart: ChartTable? get() = chartFor(gender, garment)

    /** The profile's size in the shown chart; only on the profile's own gender tab. */
    val yourSize: String?
        get() {
            val table = chart ?: return null
            val chest = chestInches ?: return null
            return if (gender == profileGender) suggestSize(chest, table.suggestFrom) else null
        }

    companion object {
        /** The design's sample chest (Rahat, 9.1 / 11.1) and bust (Farhana, 8.4). */
        const val SAMPLE_CHEST = 40f
        const val SAMPLE_BUST = 36f

        /** Garment chips per tab: men as in the design (11.1); women's chart garments. */
        fun garmentsFor(gender: Gender): List<Garment> = when (gender) {
            Gender.Male -> listOf(Garment.Shirt, Garment.Panjabi, Garment.TShirt, Garment.Pants)
            Gender.Female -> listOf(Garment.SalwarKameez)
        }

        /** The chart for a tab and chip; null for a chip that is not on the tab. */
        fun chartFor(gender: Gender, garment: Garment): ChartTable? = when {
            garment !in garmentsFor(gender) -> null
            garment == Garment.TShirt -> ChartTable(
                listOf("size_col_chest", "size_col_shoulder", "size_col_length"),
                TShirtChart.map { ChartRow(it.size, listOf(it.chestMin..it.chestMax, it.shoulder..it.shoulder, it.length..it.length)) },
                TShirtChart,
            )
            gender == Gender.Female -> bodyChart(WomenChart)
            else -> bodyChart(MenChart)
        }

        private fun bodyChart(rows: List<SizeRow>) = ChartTable(
            listOf("size_col_chest", "size_col_waist", "size_col_hip"),
            rows.map { ChartRow(it.size, listOf(it.chestMin..it.chestMax, it.waistMin..it.waistMax, it.hipMin..it.hipMax)) },
            rows,
        )

        /** 11.1: Rahat, chest 40" → L. */
        val Sample = SizeChartState("রাহাত", Gender.Male, SAMPLE_CHEST)
    }
}

/** Chart value in whole centimeters (40" → 102). */
fun inchesToWholeCm(inches: Float): Int = kotlin.math.round(inches * 2.54f).toInt()

sealed interface SizeChartAction {
    data class SetGender(val gender: Gender) : SizeChartAction
    data class SelectGarment(val garment: Garment) : SizeChartAction
}

/** Switching the tab selects its first chip; a chip not on the current tab is ignored. */
fun SizeChartState.reduce(action: SizeChartAction): SizeChartState = when (action) {
    is SizeChartAction.SetGender ->
        if (action.gender == gender) this else copy(gender = action.gender, garment = SizeChartState.garmentsFor(action.gender).first())
    is SizeChartAction.SelectGarment -> if (action.garment in garments) copy(garment = action.garment) else this
}
