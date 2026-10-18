package com.tech_hunt.tailorpass.ui.screens.manual

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.model.LengthPreset
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
        assertEquals(PresetOption.Neck(NeckDepthPreset.Medium), s.selectedPreset)
        assertEquals(TapeDiagram.NeckDepth, s.guide.diagram)
    }

    @Test
    fun shirtPantsPajamaFollowTheirTapeOrder() {
        val shirt = ManualState(MeasureTarget(Garment.Shirt, Gender.Male, "x"))
        assertEquals(9, shirt.count)
        assertEquals(MeasurementKey.Neck, shirt.currentKey)
        assertEquals(listOf(MeasurementKey.Cuff, MeasurementKey.ShirtLength), shirt.steps.takeLast(2))
        assertEquals(8, ManualState(MeasureTarget(Garment.Pants, Gender.Male, "x")).count)
        assertEquals(5, ManualState(MeasureTarget(Garment.Pajama, Gender.Male, "x")).count)
        assertTrue(ManualPlan.presets(MeasurementKey.ShirtLength).isEmpty())       // number entry, no knee-length cards
    }

    @Test
    fun genjiHasNoSteps() {
        val s = ManualState(MeasureTarget(Garment.TShirt, Gender.Male, "x"))
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
        val s = ManualState.SampleNeckDepth.reduce(ManualEntryAction.PickPreset(PresetOption.Neck(NeckDepthPreset.High)))
        assertEquals("9", s.input)
        assertEquals(PresetOption.Neck(NeckDepthPreset.High), s.selectedPreset)
    }

    @Test
    fun tipsToggle() {
        val s = ManualState.SampleSleeve
        assertTrue(s.tipsOpen)
        assertFalse(s.reduce(ManualEntryAction.ToggleTips).tipsOpen)
    }

    @Test
    fun lengthIsTheLastPanjabiStepWithThreePictureCards() {
        val s = ManualState.SampleLength
        assertEquals(MeasurementKey.Length, s.currentKey)
        assertTrue(s.isLast)
        assertEquals(LengthPreset.entries.map { PresetOption.Length(it) }, s.presets)
        assertEquals(listOf(38f, 42f, 45f), s.presets.map { it.inches })   // "৩৮" ৪২" ৪৫"" (5'7")
        assertEquals(PresetOption.Length(LengthPreset.AtKnee), s.selectedPreset)
        assertEquals("length_tip_old_garment", s.guide.noteKey)
        assertEquals("38", s.reduce(ManualEntryAction.PickPreset(PresetOption.Length(LengthPreset.AboveKnee))).input)
        assertFalse(ManualState.SampleChest.isLast)
    }

    @Test
    fun preferenceStepsStartAtTheSuggestedPreset() {
        // Reached in tape order with no value: the middle card is preselected but not saved yet.
        val cuffDone = ManualState(MeasureTarget.SampleRahat, current = 6, input = "10")
        val length = cuffDone.reduce(ManualEntryAction.Next)
        assertEquals(7, length.current)
        assertEquals("42", length.input)
        assertEquals(PresetOption.Length(LengthPreset.AtKnee), length.selectedPreset)
        assertFalse(7 in length.values)
        // Skipping keeps it missing.
        assertTrue(7 in length.copy(reviewing = true).reduce(ManualEntryAction.Later).missing)
        // Neck depth (7.10) from the list: medium, 7".
        val neck = ManualState(MeasureTarget.SampleFarhana).goTo(7)
        assertEquals("7", neck.input)
        assertEquals(PresetOption.Neck(NeckDepthPreset.Medium), neck.selectedPreset)
        // A saved value wins over the suggestion; body measurements stay empty.
        assertEquals("45", ManualState(MeasureTarget.SampleRahat, values = mapOf(7 to 45f)).goTo(7).input)
        assertEquals("", ManualState(MeasureTarget.SampleRahat).goTo(4).input)
    }

    @Test
    fun finishOnTheLastStepGoesToTheSummary() {
        val done = ManualState.SampleLength.reduce(ManualEntryAction.Next)
        assertTrue(done.finished)
        assertEquals(42f, done.values[7])
        assertEquals(ManualState.SampleSummary.values, done.values)
    }

    @Test
    fun backAsksBeforeLeavingOnlyWhenSomethingWasMeasured() {
        val asked = ManualState.SampleChest.reduce(ManualEntryAction.Back)
        assertTrue(asked.exitOpen)
        assertFalse(asked.reduce(ManualEntryAction.KeepGoing).exitOpen)
        val stopped = asked.reduce(ManualEntryAction.StopForLater)
        assertFalse(stopped.exitOpen)
        assertEquals(asked.values, stopped.values)   // kept for later
        // Nothing measured yet: no dialog, the caller goes back to the list.
        val fresh = ManualState(MeasureTarget.SampleRahat)
        assertFalse(fresh.reduce(ManualEntryAction.Back).exitOpen)
    }

    @Test
    fun exitSampleMatchesTheDialogText() {
        val s = ManualState.SampleExit
        assertEquals(4, s.doneCount)                  // "যে ৪টি মাপ নিয়েছেন"
        assertEquals(MeasurementKey.Chest, s.currentKey)
        assertTrue(s.exitOpen)
        assertFalse(s.copy(exitOpen = false).goTo(5).exitOpen)
    }

    @Test
    fun summaryListsMissingMeasurements() {
        val s = ManualState.SampleSummary
        assertEquals(listOf(4), s.missing)            // hip: "১টি মাপ বাদ আছে"
        assertEquals(MeasurementKey.Hip, s.steps[4])
        assertEquals(listOf(SummaryGroup(null, (0..7).toList())), s.summaryGroups)
        assertTrue(ManualState.SampleList.missing.containsAll(listOf(2, 3, 4, 5, 6, 7)))
    }

    @Test
    fun salwarKameezSummaryKeepsKameezAndSalwar() {
        val groups = ManualState.SampleNeckDepth.summaryGroups
        assertEquals(listOf("garment_kameez", "garment_salwar"), groups.map { it.titleKey })
        assertEquals((0..9).toList(), groups[0].indices)
        assertEquals((10..13).toList(), groups[1].indices)
    }

    @Test
    fun editFromTheSummaryReturnsThereWhenSavedOrSkipped() {
        val editing = ManualState.SampleSummary.reduce(ManualSummaryAction.Edit(4))
        assertEquals(4, editing.current)
        assertTrue(editing.reviewing)
        assertFalse(editing.finished)
        assertEquals("", editing.input)
        val saved = editing.copy(input = "41").reduce(ManualEntryAction.Next)
        assertTrue(saved.finished)
        assertEquals(4, saved.current)                // not moved on to sleeve
        assertTrue(saved.missing.isEmpty())
        val skipped = editing.reduce(ManualEntryAction.Later)
        assertTrue(skipped.finished)
        assertEquals(listOf(4), skipped.missing)
        // Unknown index: unchanged.
        assertEquals(ManualState.SampleSummary, ManualState.SampleSummary.reduce(ManualSummaryAction.Edit(99)))
    }

    @Test
    fun goingToAStepFromTheListEndsReviewMode() {
        val editing = ManualState.SampleSummary.reduce(ManualSummaryAction.Edit(0))
        val fromList = editing.goTo(1)
        assertFalse(fromList.reviewing)
        assertEquals(2, fromList.copy(input = "18").reduce(ManualEntryAction.Next).current)
    }
}
