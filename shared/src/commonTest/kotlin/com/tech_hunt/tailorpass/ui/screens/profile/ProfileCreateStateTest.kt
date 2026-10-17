package com.tech_hunt.tailorpass.ui.screens.profile

import com.tech_hunt.tailorpass.model.Fit
import com.tech_hunt.tailorpass.model.Gender
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProfileCreateStateTest {

    @Test
    fun sampleIs5ft7About170cmAndCanSave() {
        val s = ProfileCreateState.Sample
        assertEquals(170, s.heightCm)
        assertTrue(s.canSave)
    }

    @Test
    fun banglaDigitsAreAccepted() {
        val s = ProfileCreateState()
            .reduce(ProfileCreateAction.FeetChanged("৫"))
            .reduce(ProfileCreateAction.InchesChanged("১১"))
        assertEquals("5", s.feet)
        assertEquals("11", s.inches)
        assertEquals(180, s.heightCm)
    }

    @Test
    fun nonDigitInputIsIgnored() {
        val s = ProfileCreateState.Sample
        assertEquals(s, s.reduce(ProfileCreateAction.FeetChanged("5a")), "letter")
        assertEquals(s, s.reduce(ProfileCreateAction.FeetChanged("-5")), "sign")
        assertEquals(s, s.reduce(ProfileCreateAction.InchesChanged("7.5")), "decimal point")
    }

    @Test
    fun typingIntoAFullFieldStartsAgainWithTheNewDigit() {
        val s = ProfileCreateState.Sample
        assertEquals("6", s.reduce(ProfileCreateAction.FeetChanged("56")).feet)
        assertEquals("1", s.reduce(ProfileCreateAction.InchesChanged("71")).inches, "71 is above 11")
        assertEquals("1", s.copy(inches = "11").reduce(ProfileCreateAction.InchesChanged("111")).inches, "too long")
        assertEquals("5", s.copy(feet = "6").reduce(ProfileCreateAction.FeetChanged("৬৫")).feet, "Bengali digits")
        val cm = s.copy(heightUnit = HeightUnit.Cm, cm = "170")
        assertEquals("4", cm.reduce(ProfileCreateAction.CmChanged("1704")).cm)
    }

    @Test
    fun inchesUpTo11AndClearingAreAccepted() {
        val s = ProfileCreateState.Sample
        assertEquals("11", s.copy(inches = "1").reduce(ProfileCreateAction.InchesChanged("11")).inches)
        assertEquals("0", s.reduce(ProfileCreateAction.InchesChanged("0")).inches)
        assertEquals("", s.reduce(ProfileCreateAction.FeetChanged("")).feet)
        assertNull(s.reduce(ProfileCreateAction.FeetChanged("")).heightCm)
    }

    @Test
    fun emptyInchesCountAsZero() {
        val s = ProfileCreateState(feet = "6")
        assertEquals(183, s.heightCm)
    }

    @Test
    fun missingOrZeroHeightCannotBeSaved() {
        assertNull(ProfileCreateState(name = "A").heightCm)
        assertFalse(ProfileCreateState(name = "A").canSave)
        assertNull(ProfileCreateState(name = "A", feet = "0", inches = "5").heightCm)
        assertNull(ProfileCreateState(name = "A", heightUnit = HeightUnit.Cm, cm = "0").heightCm)
    }

    @Test
    fun blankNameCannotBeSaved() {
        assertFalse(ProfileCreateState.Sample.reduce(ProfileCreateAction.NameChanged("  ")).canSave)
    }

    @Test
    fun switchingUnitsKeepsTheHeight() {
        val cm = ProfileCreateState.Sample.reduce(ProfileCreateAction.HeightUnitSelected(HeightUnit.Cm))
        assertEquals(HeightUnit.Cm, cm.heightUnit)
        assertEquals("170", cm.cm)
        val back = cm.reduce(ProfileCreateAction.HeightUnitSelected(HeightUnit.FeetInch))
        assertEquals("5", back.feet)
        assertEquals("7", back.inches)
    }

    @Test
    fun switchingUnitsWithNoHeightLeavesFieldsEmpty() {
        val s = ProfileCreateState().reduce(ProfileCreateAction.HeightUnitSelected(HeightUnit.Cm))
        assertEquals("", s.cm)
        val back = s.reduce(ProfileCreateAction.HeightUnitSelected(HeightUnit.FeetInch))
        assertEquals("", back.feet)
        assertEquals("", back.inches)
    }

    @Test
    fun selectionsAndNavigationActions() {
        val s = ProfileCreateState.Sample
            .reduce(ProfileCreateAction.GenderSelected(Gender.Female))
            .reduce(ProfileCreateAction.FitSelected(Fit.Loose))
        assertEquals(Gender.Female, s.gender)
        assertEquals(Fit.Loose, s.fit)
        assertEquals(s, s.reduce(ProfileCreateAction.Save))
        assertEquals(s, s.reduce(ProfileCreateAction.Back))
    }
}
