package com.tech_hunt.tailorpass.ui.screens.sizechart

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.model.SampleMenShirtChart
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
        assertEquals(SampleMenShirtChart, sample.chart)
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
    fun womensChartIsMissingAndMarksNothing() {
        val women = sample.reduce(SizeChartAction.SetGender(Gender.Female))
        assertEquals(listOf(Garment.SalwarKameez), women.garments)
        assertEquals(Garment.SalwarKameez, women.garment)
        assertNull(women.chart)
        assertNull(women.yourSize)
    }

    @Test
    fun otherGendersTabDoesNotMarkTheProfilesRow() {
        val farhana = SizeChartState("ফারহানা", Gender.Female, chestInches = 36f)
        assertEquals(Gender.Female, farhana.gender)
        val menTab = farhana.reduce(SizeChartAction.SetGender(Gender.Male))
        assertEquals(SampleMenShirtChart, menTab.chart)
        assertNull(menTab.yourSize)
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
    }

    @Test
    fun chartCentimetersAreWhole() {
        assertEquals(86, inchesToWholeCm(34f))
        assertEquals(102, inchesToWholeCm(40f))
        assertEquals(122, inchesToWholeCm(48f))
    }
}
