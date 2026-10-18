package com.tech_hunt.tailorpass.ui.screens.sizechart

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.model.MenChart
import com.tech_hunt.tailorpass.model.TShirtChart
import com.tech_hunt.tailorpass.model.WomenChart
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SizeChartStateTest {
    private val sample = SizeChartState.Sample

    @Test
    fun sampleMatchesTheDesign() {
        assertEquals(Gender.Male, sample.gender)
        assertEquals(listOf(Garment.Shirt, Garment.Panjabi, Garment.TShirt, Garment.Pants), sample.garments)
        assertEquals(Garment.Shirt, sample.garment)
        assertEquals(listOf("size_col_chest", "size_col_waist", "size_col_hip"), sample.chart?.columnKeys)
        assertEquals(MenChart.map { it.size }, sample.chart?.rows?.map { it.size })
        assertEquals(34f..36f, sample.chart?.rows?.first()?.cells?.first())
        assertEquals("L", sample.yourSize)
    }

    @Test
    fun noMeasurementMarksNoRow() {
        assertNull(sample.copy(chestInches = null).yourSize)
    }

    @Test
    fun chestOnABoundaryGetsTheLargerSize() {
        assertEquals("XL", sample.copy(chestInches = 42f).yourSize)
        assertEquals("L", sample.copy(chestInches = 41.5f).yourSize)
    }

    @Test
    fun genjiChipShowsChestShoulderAndLength() {
        val genji = sample.reduce(SizeChartAction.SelectGarment(Garment.TShirt))
        val chart = genji.chart!!
        assertEquals(listOf("size_col_chest", "size_col_shoulder", "size_col_length"), chart.columnKeys)
        assertEquals(TShirtChart.map { it.size }, chart.rows.map { it.size })
        assertEquals(listOf(40f..42f, 18f..18f, 28f..28f), chart.rows.single { it.size == "L" }.cells)
        assertEquals("L", genji.yourSize)
    }

    @Test
    fun womensTabShowsTheWomensChartByBust() {
        val farhana = SizeChartState("ফারহানা", Gender.Female, chestInches = SizeChartState.SAMPLE_BUST)
        assertEquals(listOf(Garment.SalwarKameez), farhana.garments)
        assertEquals(WomenChart.map { it.size }, farhana.chart?.rows?.map { it.size })
        assertEquals("M", farhana.yourSize)
    }

    @Test
    fun otherGendersTabDoesNotMarkTheProfilesRow() {
        val farhana = SizeChartState("ফারহানা", Gender.Female, chestInches = 36f)
        val menTab = farhana.reduce(SizeChartAction.SetGender(Gender.Male))
        assertEquals(Garment.Shirt, menTab.garment)
        assertNull(menTab.yourSize)
        assertNull(sample.reduce(SizeChartAction.SetGender(Gender.Female)).yourSize)
    }

    @Test
    fun selectingAChipKeepsTheTab() {
        val panjabi = sample.reduce(SizeChartAction.SelectGarment(Garment.Panjabi))
        assertEquals(Garment.Panjabi, panjabi.garment)
        assertEquals(Gender.Male, panjabi.gender)
        assertEquals("L", panjabi.yourSize)
    }

    @Test
    fun chipOfTheOtherTabAndSameTabAreIgnored() {
        assertEquals(sample, sample.reduce(SizeChartAction.SelectGarment(Garment.SalwarKameez)))
        val panjabi = sample.reduce(SizeChartAction.SelectGarment(Garment.Panjabi))
        assertEquals(panjabi, panjabi.reduce(SizeChartAction.SetGender(Gender.Male)))
        assertNull(SizeChartState.chartFor(Gender.Female, Garment.Shirt))
    }

    @Test
    fun chartCentimetersAreWhole() {
        assertEquals(86, inchesToWholeCm(34f))
        assertEquals(102, inchesToWholeCm(40f))
        assertEquals(122, inchesToWholeCm(48f))
    }
}
