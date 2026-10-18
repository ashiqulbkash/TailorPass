package com.tech_hunt.tailorpass.ui.screens.sizechart

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.model.SampleMenShirtChart
import com.tech_hunt.tailorpass.model.SizeRow
import com.tech_hunt.tailorpass.model.suggestSize
import com.tech_hunt.tailorpass.ui.screens.manual.MeasureUnit

/**
 * Size chart tab (11.1): [gender] tab, [garment] chip and the chart, the current profile's row marked "আপনার".
 * [chestInches]: the profile's chest (null = nothing measured yet, no row marked). [unit]: the app's display unit.
 * ⚠ Only the sample men's chart exists (Measurements.kt), so every men's chip shows it; the women's charts are
 * missing (plan.md §3) — the women's tab says so instead of inventing one. Design phase: a ViewModel replaces this
 * in Step 10.
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
    val chart: List<SizeRow>? get() = chartFor(gender, garment)

    /** The profile's size in the shown chart; only on the profile's own gender tab. */
    val yourSize: String?
        get() {
            val rows = chart ?: return null
            val chest = chestInches ?: return null
            return if (gender == profileGender) suggestSize(chest, rows) else null
        }

    companion object {
        /** The design's sample chest (Rahat, 9.1 / 11.1). */
        const val SAMPLE_CHEST = 40f

        /** Garment chips per tab: men as in the design (11.1); women's chart garments. */
        fun garmentsFor(gender: Gender): List<Garment> = when (gender) {
            Gender.Male -> listOf(Garment.Shirt, Garment.Panjabi, Garment.TShirt, Garment.Pants)
            Gender.Female -> listOf(Garment.SalwarKameez)
        }

        /** The chart for a tab and chip; null = not provided yet (women's charts, plan.md §3). */
        fun chartFor(gender: Gender, garment: Garment): List<SizeRow>? = when (gender) {
            Gender.Male -> if (garment in garmentsFor(gender)) SampleMenShirtChart else null
            Gender.Female -> null
        }

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
