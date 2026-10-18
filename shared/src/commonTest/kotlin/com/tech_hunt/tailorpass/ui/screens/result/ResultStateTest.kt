package com.tech_hunt.tailorpass.ui.screens.result

import com.tech_hunt.tailorpass.model.Diagrams
import com.tech_hunt.tailorpass.model.Fit
import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.MeasurementKey
import com.tech_hunt.tailorpass.model.SourceBadge
import com.tech_hunt.tailorpass.ui.screens.manual.ManualState
import com.tech_hunt.tailorpass.ui.screens.manual.MeasureUnit
import com.tech_hunt.tailorpass.ui.screens.prep.MeasureTarget
import com.tech_hunt.tailorpass.ui.screens.verify.VerifyAction
import com.tech_hunt.tailorpass.ui.screens.verify.VerifyState
import com.tech_hunt.tailorpass.ui.screens.verify.reduce
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ResultStateTest {

    @Test
    fun panjabiSampleMatchesTheDesign() {
        val s = ResultState.SamplePanjabi
        assertFalse(s.sectioned)
        assertEquals(Diagrams.panjabi, s.currentSection?.diagram)
        assertEquals(listOf(42f, 40f, 36f, 41f, 18f, 24.5f, 10f, 15.5f), s.rows.map { it.inches })
        assertEquals("L", s.size)                                      // chest 40" → L (9.1, 10.1)
        assertEquals(listOf(MeasurementKey.Neck), s.checkKeys)         // 10.1: "* ফিতায় মিলিয়ে নিন" under neck
        assertFalse(s.hasChoices)                                      // 10.1 stars nothing
        assertEquals("result_title_panjabi", s.titleKey)
        assertEquals("garment_panjabi", s.readymadeKey)
    }

    @Test
    fun salwarKameezSampleHasTwoPartsWithTheirDiagrams() {
        val s = ResultState.SampleSalwarKameez
        assertTrue(s.sectioned)
        assertEquals(listOf(10, 4), s.sections.map { it.rows.size })
        assertEquals(listOf(Diagrams.kameez, Diagrams.salwar), s.sections.map { it.diagram })
        assertEquals("M", s.size)                                      // bust 36 on the women's chart
        assertTrue(s.hasChoices)                                       // cuff, neck depths, length, bottom starred
        assertEquals(listOf(MeasurementKey.Armhole), s.checkKeys)      // 10.2 footer: "আর্মহোল ফিতায় মিলিয়ে নিন"
        assertEquals("result_title_salwar_kameez", s.titleKey)
        assertEquals("garment_kameez", s.readymadeKey)                 // "রেডিমেড কামিজ কিনতে"
    }

    @Test
    fun fromVerifyKeepsEditsAndTheTrimmedNote() {
        val verify = VerifyState.SamplePanjabi
            .reduce(VerifyAction.Edit(1))
            .reduce(VerifyAction.EditInputChanged("42"))
            .reduce(VerifyAction.SaveEdit)
            .reduce(VerifyAction.NoteChanged("  বোতাম কাপড়ের রঙে  "))
        val s = ResultState.fromVerify(verify, ResultState.SampleDate, Fit.Slim)
        assertEquals(42f, s.rows[1].inches)
        assertEquals(SourceBadge.TapeMeasured, s.rows[1].badge)
        assertEquals("XL", s.size)                                     // chest 42" is on the L/XL boundary → XL
        assertEquals("বোতাম কাপড়ের রঙে", s.note)
        assertEquals(Fit.Slim, s.fit)
    }

    @Test
    fun skippedChestGivesNoSize() {
        val s = ResultState.fromVerify(
            VerifyState.fromTape(ManualState(MeasureTarget.SampleRahat)),
            ResultState.SampleDate,
            Fit.Regular,
        )
        assertTrue(s.rows.all { it.inches == null && it.badge == SourceBadge.Missing })
        assertNull(s.size)
        assertTrue(s.checkKeys.isEmpty())
    }

    @Test
    fun repeatedKeysAreCheckedOnce() {
        val low = ResultState.SampleSalwarKameez.let { s ->
            s.copy(sections = s.sections.map { sec -> sec.copy(rows = sec.rows.map { it.copy(badge = SourceBadge.AccuracyLow) }) })
        }
        assertEquals(low.rows.map { it.key }.distinct(), low.checkKeys)
    }

    @Test
    fun shirtPantsPajamaShowTheirListsDiagramsAndSizes() {
        mapOf(Garment.Shirt to 9, Garment.Pants to 8, Garment.Pajama to 5).forEach { (g, count) ->
            val s = ResultState.sample(MeasureTarget.SampleRahat.copy(garment = g), ResultState.SampleDate)
            assertEquals(count, s.rows.size, g.name)
            assertEquals(1, s.sections.size, g.name)
            // One badge per row, numbered in list order.
            assertEquals((1..count).toList(), ResultPlan.diagram(g, 0)?.badges?.map { it.number }, g.name)
            assertNull(ResultPlan.diagram(g, 1))
        }
        fun size(g: Garment) = ResultState.sample(MeasureTarget.SampleRahat.copy(garment = g), ResultState.SampleDate).size
        assertEquals("L", size(Garment.Shirt))                          // chest 40 on the men's chart
        assertEquals("34", size(Garment.Pants))                         // waist 34 → trouser size 34
        assertEquals("L", size(Garment.Pajama))                         // waist 34 on the men's chart
    }

    @Test
    fun reducerSwitchesUnitAndTabs() {
        val s = ResultState.SampleSalwarKameez
        assertEquals(MeasureUnit.Cm, s.reduce(ResultAction.SetUnit(MeasureUnit.Cm)).unit)
        assertEquals(1, s.reduce(ResultAction.SelectTab(1)).tab)
        assertEquals(0, s.reduce(ResultAction.SelectTab(2)).tab)       // out of range is ignored
        assertEquals(0, ResultState.SamplePanjabi.reduce(ResultAction.SelectTab(1)).tab)
        assertEquals(s, s.reduce(ResultAction.SharePdf))
        assertEquals(s, s.reduce(ResultAction.ShareImage))
        assertEquals(s, s.reduce(ResultAction.Back))
    }

    @Test
    fun genjiSampleIsLBetweenMAndXl() {
        val g = GenjiState.Sample
        assertEquals("L", g.size)
        assertEquals(listOf("M", "L", "XL"), g.compared.map { it.size })
        assertEquals("M", g.tighter?.size)
        assertEquals("XL", g.looser?.size)
    }

    @Test
    fun genjiAtTheChartEndsHasOneNeighbour() {
        val smallest = GenjiState.Sample.copy(chestInches = 36f, shoulderInches = 16.5f)
        assertEquals("S", smallest.size)
        assertEquals(listOf("S", "M"), smallest.compared.map { it.size })
        assertNull(smallest.tighter)
        val largest = GenjiState.Sample.copy(chestInches = 50f, shoulderInches = 19f)
        assertEquals("XXL", largest.size)
        assertEquals(listOf("XL", "XXL"), largest.compared.map { it.size })
        assertNull(largest.looser)
        val tooSmall = GenjiState.Sample.copy(chestInches = 30f)
        assertNull(tooSmall.size)
        assertTrue(tooSmall.compared.isEmpty())
        assertNull(tooSmall.tighter)
        assertNull(tooSmall.looser)
    }

    @Test
    fun genjiBroadShoulderMovesUpASize() {
        assertEquals("XL", GenjiState.Sample.copy(shoulderInches = 18.5f).size)    // chest says L, shoulder needs XL
    }
}
