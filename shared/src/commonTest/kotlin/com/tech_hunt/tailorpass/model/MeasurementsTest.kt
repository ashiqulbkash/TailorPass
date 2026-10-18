package com.tech_hunt.tailorpass.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MeasurementsTest {

    @Test
    fun suggestSize_designSample() {
        // Panjabi sample (plan.md): chest 40 → L
        assertEquals("L", suggestSize(40f))
    }

    @Test
    fun suggestSize_boundaryGoesToLargerSize() {
        assertEquals("M", suggestSize(38f))
        assertEquals("M", suggestSize(39.5f))
        assertEquals("XXXL", suggestSize(50f))
        assertNull(suggestSize(30f))
    }

    @Test
    fun isPlausible_includesLimits() {
        assertTrue(MeasurementKey.Chest.isPlausible(30f))
        assertTrue(MeasurementKey.Chest.isPlausible(50f))
        assertFalse(MeasurementKey.Chest.isPlausible(29.5f))
        assertFalse(MeasurementKey.Chest.isPlausible(50.5f))
    }

    @Test
    fun garmentLists_matchOverview() {
        assertEquals(8, GarmentMeasurements.panjabi.single().keys.size)
        assertEquals(listOf(10, 4), GarmentMeasurements.salwarKameez.map { it.keys.size })
        assertEquals(
            GarmentMeasurements.panjabi.single().keys.toSet(),
            GarmentMeasurements.panjabiManualOrder.flatMap { it.keys }.toSet(),
        )
    }

    @Test
    fun preferenceMeasurements_areNotAiMeasurable() {
        MeasurementKey.entries.forEach { assertEquals(!it.isPreference, it.aiMeasurable, it.name) }
    }

    @Test
    fun newGarmentLists_matchOverview() {
        // Overview §4: shirt 9, pants 8, pajama 5; tape order covers the same keys.
        val lists = listOf(
            GarmentMeasurements.shirt to GarmentMeasurements.shirtManualOrder,
            GarmentMeasurements.pants to GarmentMeasurements.pantsManualOrder,
            GarmentMeasurements.pajama to GarmentMeasurements.pajamaManualOrder,
        )
        assertEquals(listOf(9, 8, 5), lists.map { it.first.single().keys.size })
        lists.forEach { (order, manual) ->
            assertEquals(order.single().keys.toSet(), manual.flatMap { it.keys }.toSet())
            assertEquals(manual.flatMap { it.keys }.size, manual.flatMap { it.keys }.toSet().size)
        }
        // Preferences (*) per Overview §4: shirt cuff + length, pants and pajama bottom opening.
        assertEquals(
            listOf(MeasurementKey.Cuff, MeasurementKey.ShirtLength),
            GarmentMeasurements.shirt.single().keys.filter { it.isPreference },
        )
        assertEquals(listOf(MeasurementKey.Bottom), GarmentMeasurements.pants.single().keys.filter { it.isPreference })
        assertEquals(listOf(MeasurementKey.Bottom), GarmentMeasurements.pajama.single().keys.filter { it.isPreference })
    }

    @Test
    fun charts_areOrderedWithoutOverlaps() {
        listOf(MenChart, WomenChart, TShirtChart).forEach { chart ->
            chart.zipWithNext().forEach { (a, b) -> assertTrue(a.chestMax <= b.chestMin, "${a.size} / ${b.size}") }
            chart.forEach { assertTrue(it.chestMin < it.chestMax, it.size) }
        }
        assertEquals(listOf("XS", "S", "M", "L", "XL", "XXL", "XXXL"), WomenChart.map { it.size })
        assertEquals(listOf("S", "M", "L", "XL", "XXL"), TShirtChart.map { it.size })
    }

    @Test
    fun womenSize_fromBust() {
        assertEquals("M", suggestSize(36f, WomenChart))      // Farhana (8.4) → M, as 9.3 shows
        assertEquals("L", suggestSize(37.5f, WomenChart))
        assertEquals("M", suggestSize(37.2f, WomenChart))    // Between M and L: the smaller, like the men's chart gaps
        assertNull(suggestSize(30f, WomenChart))
    }

    @Test
    fun tShirtSize_fromChestAndShoulder() {
        assertEquals("L", suggestTShirtSize(40f, 18f))       // Rahat (9.4): chest 40, shoulder 18 → L
        assertEquals("XL", suggestTShirtSize(40f, 18.5f))    // Broader shoulder than L's → one size up
        assertEquals("XXL", suggestTShirtSize(41f, 22f))     // Wider than every shirt → the largest
        assertEquals("S", suggestTShirtSize(36f, 15f))
        assertNull(suggestTShirtSize(34f, 16f))              // Smaller than the chart
    }

    @Test
    fun menChart_designSampleAndSources() {
        // Chest and waist as in the 11.1 design (ASOS); hip from the James Perse chart (± 1").
        val l = MenChart.single { it.size == "L" }
        assertEquals(listOf(40f, 42f, 34f, 36f, 39f, 41f), listOf(l.chestMin, l.chestMax, l.waistMin, l.waistMax, l.hipMin, l.hipMax))
        MenChart.zipWithNext().forEach { (a, b) ->
            assertEquals(a.waistMax, b.waistMin, a.size)
            assertEquals(a.hipMax, b.hipMin, a.size)
        }
    }

    @Test
    fun trouserSize_isTheWaistNumber() {
        assertEquals("34", suggestTrouserSize(34f))
        assertEquals("34", suggestTrouserSize(33f))           // odd waist → next even size
        assertEquals("34", suggestTrouserSize(32.5f))
        assertEquals("26", suggestTrouserSize(24f))
        assertEquals("44", suggestTrouserSize(44f))
        assertNull(suggestTrouserSize(23.5f))
        assertNull(suggestTrouserSize(44.5f))
    }

    @Test
    fun pajamaSize_fromWaist() {
        assertEquals("L", suggestSizeByWaist(34f))
        assertEquals("M", suggestSizeByWaist(33.5f))
        assertEquals("XXXL", suggestSizeByWaist(45f))
        assertNull(suggestSizeByWaist(26f))
    }

    @Test
    fun newRanges_coverAnsurMedians() {
        // ANSUR II men's medians (plan.md §3) must not trigger the "unusual value" warning.
        mapOf(
            MeasurementKey.Biceps to 14.1f, MeasurementKey.Thigh to 24.6f, MeasurementKey.Knee to 16.1f,
            MeasurementKey.Crotch to 24.4f, MeasurementKey.Inseam to 32.3f, MeasurementKey.FullLength to 41.5f,
        ).forEach { (key, median) -> assertTrue(key.isPlausible(median), key.name) }
    }
}
