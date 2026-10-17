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
}
