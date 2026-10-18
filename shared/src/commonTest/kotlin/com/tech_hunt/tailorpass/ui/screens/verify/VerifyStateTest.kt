package com.tech_hunt.tailorpass.ui.screens.verify

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.GarmentMeasurements
import com.tech_hunt.tailorpass.model.MeasureMethod
import com.tech_hunt.tailorpass.model.MeasurementKey
import com.tech_hunt.tailorpass.model.SourceBadge
import com.tech_hunt.tailorpass.ui.screens.manual.ManualState
import com.tech_hunt.tailorpass.ui.screens.prep.MeasureTarget
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class VerifyStateTest {

    @Test
    fun panjabiSampleMatchesTheDesign() {
        val s = VerifyState.SamplePanjabi
        assertEquals(GarmentMeasurements.panjabi.single().keys, s.rows.map { it.key })
        assertEquals(listOf(42f, 40f, 36f, 41f, 18f, 24.5f, 10f, 15.5f), s.rows.map { it.inches })
        assertEquals(SourceBadge.AccuracyLow, s.rows.last().badge)   // neck: "▲ নিম্ন"
        assertFalse(s.sectioned)
        assertFalse(s.showPreferenceHint)
    }

    @Test
    fun salwarKameezHasTwoSectionsAndThePreferenceHint() {
        val s = VerifyState.SampleSalwarKameez
        assertEquals(listOf(10, 4), s.sections.map { it.rows.size })
        assertEquals(14, s.count)
        assertTrue(s.sectioned)
        assertTrue(s.showPreferenceHint)
        assertEquals(listOf("ic_garment_salwar_kameez", "ic_garment_pajama"), s.sections.map { it.iconName })
        assertEquals(10, s.sectionStart(1))
        // Preference values are never AI values.
        assertTrue(s.rows.filter { it.badge == SourceBadge.YourChoice }.all { it.key.isPreference && it.aiInches == null })
    }

    @Test
    fun tapeSessionMapsToVerifyOrderWithTheSkippedHipMissing() {
        val s = VerifyState.SampleManual
        assertEquals(MeasureMethod.Tape, s.method)
        assertEquals(GarmentMeasurements.panjabi.single().keys, s.rows.map { it.key })
        assertEquals(listOf(42f, 40f, 36f, null, 18f, 24.5f, 10f, 15.5f), s.rows.map { it.inches })
        assertEquals(
            listOf(
                SourceBadge.YourChoice, SourceBadge.TapeMeasured, SourceBadge.TapeMeasured, SourceBadge.Missing,
                SourceBadge.TapeMeasured, SourceBadge.TapeMeasured, SourceBadge.YourChoice, SourceBadge.TapeMeasured,
            ),
            s.rows.map { it.badge },
        )
        assertTrue(s.rows.all { it.aiInches == null })
        assertFalse(s.showPreferenceHint)   // 8.5 shows the normal banner
    }

    @Test
    fun tapeSalwarKameezKeepsBothWaistAndHipValues() {
        val manual = ManualState(MeasureTarget.SampleFarhana).let { m ->
            // Kameez waist/hip = steps 1/2, salwar waist/hip = steps 10/11.
            m.copy(values = mapOf(1 to 30f, 2 to 38f, 10 to 31f, 11 to 39f))
        }
        val s = VerifyState.fromTape(manual)
        val kameez = s.sections[0].rows
        val salwar = s.sections[1].rows
        assertEquals(30f, kameez.first { it.key == MeasurementKey.Waist }.inches)
        assertEquals(38f, kameez.first { it.key == MeasurementKey.Hip }.inches)
        assertEquals(31f, salwar.first { it.key == MeasurementKey.Waist }.inches)
        assertEquals(39f, salwar.first { it.key == MeasurementKey.Hip }.inches)
        assertEquals(SourceBadge.Missing, kameez.first { it.key == MeasurementKey.Bust }.badge)
    }

    @Test
    fun shirtSampleUsesKnownValuesAndLeavesTheRestToTheTailor() {
        val target = MeasureTarget.SampleRahat.copy(garment = Garment.Shirt)
        val s = VerifyState.sampleAi(target)
        assertEquals(target, s.target)
        assertEquals(GarmentMeasurements.shirt.single().keys, s.rows.map { it.key })
        assertEquals(40f, s.rows.single { it.key == MeasurementKey.Chest }.inches)
        val biceps = s.rows.single { it.key == MeasurementKey.Biceps }
        assertNull(biceps.inches)                                        // no design value: not invented
        assertEquals(SourceBadge.Missing, biceps.badge)
        val pants = VerifyState.sampleAi(target.copy(garment = Garment.Pants))
        assertEquals(34f, pants.rows.single { it.key == MeasurementKey.Waist }.inches)    // 2.1 "কোমর ৩৪""
        assertEquals(0, VerifyState.sampleAi(target.copy(garment = Garment.TShirt)).count)  // genji is size-only
    }

    @Test
    fun editSheetOpensWithTheRowValueAndShowsTheChange() {
        val opened = VerifyState.SamplePanjabi.reduce(VerifyAction.Edit(2))
        assertEquals(2, opened.editing)
        assertEquals("36", opened.editInput)
        assertFalse(opened.editChanged)
        val changed = opened.reduce(VerifyAction.EditIncrease)
        assertEquals("36.5", changed.editInput)
        assertTrue(changed.editChanged)   // "AI: ৩৬" · আপনি বদলেছেন"
        assertEquals(VerifyState.SampleEdit.editInput, changed.editInput)
    }

    @Test
    fun savingAChangedValueUpdatesOnlyThatRow() {
        val saved = VerifyState.SampleEdit.reduce(VerifyAction.SaveEdit)
        assertNull(saved.editing)
        val row = saved.rows[2]
        assertEquals(36.5f, row.inches)
        assertEquals(SourceBadge.TapeMeasured, row.badge)
        assertEquals(36f, row.aiInches)
        assertEquals(VerifyState.SamplePanjabi.rows.filterIndexed { i, _ -> i != 2 }, saved.rows.filterIndexed { i, _ -> i != 2 })
    }

    @Test
    fun savingAnUnchangedValueKeepsItsBadge() {
        val s = VerifyState.SamplePanjabi.reduce(VerifyAction.Edit(0)).reduce(VerifyAction.SaveEdit)
        assertEquals(VerifyState.SamplePanjabi.rows, s.rows)
    }

    @Test
    fun editedPreferenceStaysYourChoice() {
        val s = VerifyState.SamplePanjabi
            .reduce(VerifyAction.Edit(6))            // cuff 10"
            .reduce(VerifyAction.EditDecrease)
            .reduce(VerifyAction.SaveEdit)
        assertEquals(9.5f, s.rows[6].inches)
        assertEquals(SourceBadge.YourChoice, s.rows[6].badge)
    }

    @Test
    fun missingTapeValueCanBeFilledIn() {
        val opened = VerifyState.SampleManual.reduce(VerifyAction.Edit(3))   // hip, skipped
        assertEquals("", opened.editInput)
        assertFalse(opened.canSave)
        assertEquals(opened, opened.reduce(VerifyAction.SaveEdit))         // nothing to save yet
        val started = opened.reduce(VerifyAction.EditIncrease)
        assertEquals("30", started.editInput)                              // starts at the plausible minimum
        val saved = started.reduce(VerifyAction.EditInputChanged("৪১½")).reduce(VerifyAction.SaveEdit)
        assertEquals(41.5f, saved.rows[3].inches)
        assertEquals(SourceBadge.TapeMeasured, saved.rows[3].badge)
    }

    @Test
    fun editInRepeatedKeyUpdatesTheRightSection() {
        val s = VerifyState.SampleSalwarKameez
            .reduce(VerifyAction.Edit(10))           // salwar waist
            .reduce(VerifyAction.EditInputChanged("32"))
            .reduce(VerifyAction.SaveEdit)
        assertEquals(30f, s.sections[0].rows[1].inches)
        assertEquals(32f, s.sections[1].rows[0].inches)
    }

    @Test
    fun invalidInputAndOutOfRangeEditAreIgnored() {
        val opened = VerifyState.SamplePanjabi.reduce(VerifyAction.Edit(1))
        assertEquals(opened, opened.reduce(VerifyAction.EditInputChanged("4a")))
        assertEquals(VerifyState.SamplePanjabi, VerifyState.SamplePanjabi.reduce(VerifyAction.Edit(99)))
    }

    @Test
    fun dismissKeepsTheValueAndNoteIsStored() {
        val s = VerifyState.SampleEdit.reduce(VerifyAction.DismissEdit)
        assertNull(s.editingRow)
        assertEquals(36f, s.rows[2].inches)
        assertEquals("হাতা ঢিলা", s.reduce(VerifyAction.NoteChanged("হাতা ঢিলা")).note)
    }
}
