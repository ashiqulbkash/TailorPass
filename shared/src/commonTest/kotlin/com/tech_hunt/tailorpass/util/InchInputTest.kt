package com.tech_hunt.tailorpass.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class InchInputTest {

    @Test
    fun parsesLatinBengaliAndHalf() {
        assertEquals(24.5f, parseInches("24.5"))
        assertEquals(24.5f, parseInches("২৪½"))
        assertEquals(24.5f, parseInches("২৪.৫"))
        assertEquals(40f, parseInches("40"))
        assertEquals(40f, parseInches("40."))
    }

    @Test
    fun rejectsEmptyZeroAndNonNumbers() {
        assertNull(parseInches(""))
        assertNull(parseInches("."))
        assertNull(parseInches("0"))
        assertNull(parseInches("abc"))
    }

    @Test
    fun sanitizeConvertsToLatinAndLimitsLength() {
        assertEquals("24.5", sanitizeInchInput("২৪½"))
        assertEquals("24.5", sanitizeInchInput("24,5"))
        assertEquals("24.", sanitizeInchInput("২৪."))
        assertEquals("", sanitizeInchInput(""))
        assertEquals("120", sanitizeInchInput("120"))
        assertNull(sanitizeInchInput("1200"))      // more than 3 whole digits
        assertNull(sanitizeInchInput("24.55"))     // more than one decimal
        assertNull(sanitizeInchInput("২৪½5"))      // a digit typed after ½
        assertNull(sanitizeInchInput("2.4.5"))
        assertNull(sanitizeInchInput("24a"))
    }

    @Test
    fun inputTextFromInches() {
        assertEquals("24.5", inchesToInput(24.5f))
        assertEquals("40", inchesToInput(40f))
        assertEquals("15.5", inchesToInput(15.5f))
    }

    @Test
    fun displayShowsHalfAndLocalDigits() {
        assertEquals("২৪½", displayInchInput("24.5"))
        assertEquals("24½", displayInchInput("24.5", useBangla = false))
        assertEquals("৪০", displayInchInput("40"))
        assertEquals("২৪.", displayInchInput("24."))
        assertEquals("", displayInchInput(""))
        // What is shown parses back to the same value.
        assertEquals(24.5f, parseInches(displayInchInput("24.5")))
    }
}
