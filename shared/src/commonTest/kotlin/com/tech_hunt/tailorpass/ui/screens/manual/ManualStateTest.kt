package com.tech_hunt.tailorpass.ui.screens.manual

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.model.MeasurementKey
import com.tech_hunt.tailorpass.model.NeckDepthPreset
import com.tech_hunt.tailorpass.ui.diagram.TapeDiagram
import com.tech_hunt.tailorpass.ui.screens.prep.MeasureTarget
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ManualStateTest {

    @Test
    fun panjabiListMatchesTheDesign() {
        val s = ManualState.SampleList
        assertEquals(8, s.count)
        assertEquals(5, s.minutes)                                   // "প্রায় ৫ মিনিট"
        assertEquals(listOf(6, 2), s.sections.map { it.keys.size })  // body 6, preference 2
        assertEquals(6, s.sectionStart(1))
        assertEquals(
            listOf(StepStatus.Done, StepStatus.Done, StepStatus.Current) + List(5) { StepStatus.Pending },
            (0 until 8).map { s.status(it) },
        )
        assertEquals(MeasurementKey.Chest, s.currentKey)
    }

    @Test
    fun salwarKameezFrontNeckDepthIsEighthOfFourteen() {
        val s = ManualState.SampleNeckDepth
        assertEquals(14, s.count)
        assertEquals(MeasurementKey.NeckDepthFront, s.currentKey)
        assertEquals(NeckDepthPreset.Medium, s.selectedPreset)
        assertEquals(TapeDiagram.NeckDepth, s.guide.diagram)
    }

    @Test
    fun garmentsWithoutAListHaveNoSteps() {
        val s = ManualState(MeasureTarget(Garment.Shirt, Gender.Male, "x"))
        assertEquals(0, s.count)
        assertNull(s.currentKey)
        assertFalse(s.isOutlier)
    }

    @Test
    fun guidesOnlyWhereTheDesignHasThem() {
        assertEquals(listOf("tip_sleeve_bend", "tip_sleeve_start"), ManualPlan.guide(MeasurementKey.Sleeve).tipKeys)
        assertEquals("howto_chest", ManualPlan.guide(MeasurementKey.Chest).howtoKey)
        assertTrue(ManualPlan.guide(MeasurementKey.Chest).tipKeys.isEmpty())
        assertNull(ManualPlan.guide(MeasurementKey.Neck).diagram)
        assertTrue(ManualPlan.presets(MeasurementKey.Chest).isEmpty())
    }

    @Test
    fun stepperChangesByHalfInchAndNeverBelowHalf() {
        val s = ManualState.SampleChest
        assertEquals("40.5", s.reduce(ManualEntryAction.Increase).input)
        assertEquals("39.5", s.reduce(ManualEntryAction.Decrease).input)
        assertEquals("0.5", s.copy(input = "0.5").reduce(ManualEntryAction.Decrease).input)
        // Empty field: + starts at the low end of the usual range, − does nothing.
        assertEquals("30", s.copy(input = "").reduce(ManualEntryAction.Increase).input)
        assertEquals("", s.copy(input = "").reduce(ManualEntryAction.Decrease).input)
    }

    @Test
    fun typingIsSanitized() {
        val s = ManualState.SampleChest
        assertEquals("24.5", s.reduce(ManualEntryAction.InputChanged("২৪½")).input)
        assertEquals("40", s.reduce(ManualEntryAction.InputChanged("40x")).input)   // rejected, unchanged
    }

    @Test
    fun nextSavesAndMovesOn() {
        val next = ManualState.SampleChest.reduce(ManualEntryAction.Next)
        assertEquals(40f, next.values[2])
        assertEquals(3, next.current)
        assertEquals("", next.input)
        assertFalse(next.finished)
    }

    @Test
    fun nextIsIgnoredWithoutANumber() {
        val s = ManualState.SampleChest.copy(input = "")
        assertFalse(s.canGoNext)
        assertEquals(s, s.reduce(ManualEntryAction.Next))
    }

    @Test
    fun outlierOpensTheWarningAndKeepsTheStep() {
        val s = ManualState.SampleChest.copy(input = "14")
        assertTrue(s.isOutlier)
        val warned = s.reduce(ManualEntryAction.Next)
        assertTrue(warned.warningOpen)
        assertEquals(2, warned.current)
        assertNull(warned.values[2])
        // Range limits are plausible (30..50 for chest).
        assertFalse(s.copy(input = "30").isOutlier)
        assertFalse(s.copy(input = "50").isOutlier)
        assertTrue(s.copy(input = "50.5").isOutlier)
    }

    @Test
    fun remeasureClosesTheWarningConfirmSaves() {
        val warned = ManualState.SampleWarning
        val again = warned.reduce(ManualEntryAction.Remeasure)
        assertFalse(again.warningOpen)
        assertEquals("14", again.input)
        assertEquals(2, again.current)
        val confirmed = warned.reduce(ManualEntryAction.ConfirmValue)
        assertEquals(14f, confirmed.values[2])
        assertEquals(3, confirmed.current)
        assertFalse(confirmed.warningOpen)
    }

    @Test
    fun laterSkipsWithoutLosingAnEnteredValue() {
        val skipped = ManualState.SampleChest.reduce(ManualEntryAction.Later)
        assertTrue(2 in skipped.skipped)
        assertNull(skipped.values[2])
        assertEquals(3, skipped.current)
        // A done measurement opened again and left with "later" keeps its value.
        val reopened = ManualState.SampleList.goTo(0).reduce(ManualEntryAction.Later)
        assertEquals(15.5f, reopened.values[0])
        assertFalse(0 in reopened.skipped)
    }

    @Test
    fun goToLoadsTheSavedValue() {
        val s = ManualState.SampleList.goTo(0)
        assertEquals(0, s.current)
        assertEquals("15.5", s.input)
        assertEquals(s, s.goTo(99))   // out of range: unchanged
    }

    @Test
    fun lastStepFinishes() {
        val last = ManualState.SampleList.goTo(7).copy(input = "42")
        val done = last.reduce(ManualEntryAction.Next)
        assertTrue(done.finished)
        assertEquals(42f, done.values[7])
        assertEquals(7, done.current)
    }

    @Test
    fun presetSetsTheValue() {
        val s = ManualState.SampleNeckDepth.reduce(ManualEntryAction.PickPreset(NeckDepthPreset.High))
        assertEquals("9", s.input)
        assertEquals(NeckDepthPreset.High, s.selectedPreset)
    }

    @Test
    fun tipsToggle() {
        val s = ManualState.SampleSleeve
        assertTrue(s.tipsOpen)
        assertFalse(s.reduce(ManualEntryAction.ToggleTips).tipsOpen)
    }
}
