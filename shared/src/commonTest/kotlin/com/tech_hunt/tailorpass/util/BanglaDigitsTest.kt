package com.tech_hunt.tailorpass.util

import kotlin.test.Test
import kotlin.test.assertEquals

class BanglaDigitsTest {

    @Test
    fun toBanglaDigits_convertsOnlyDigits() {
        assertEquals("০১২৩৪৫৬৭৮৯", "0123456789".toBanglaDigits())
        assertEquals("৫′৭″ · ১৭০ সেমি", "5′7″ · 170 সেমি".toBanglaDigits())
    }

    @Test
    fun toLatinDigits_isInverse() {
        assertEquals("0123456789", "০১২৩৪৫৬৭৮৯".toLatinDigits())
        assertEquals("24.5", "24.5".toBanglaDigits().toLatinDigits())
    }

    @Test
    fun formatInches_wholeAndHalf() {
        assertEquals("৪০", formatInches(40f))
        assertEquals("২৪½", formatInches(24.5f))
        assertEquals("40", formatInches(40f, useBangla = false))
        assertEquals("24½", formatInches(24.5f, useBangla = false))
    }

    @Test
    fun formatInches_otherFractionsUseOneDecimal() {
        assertEquals("24.2", formatInches(24.25f, useBangla = false))
        assertEquals("২৪.৭", formatInches(24.75f))
    }

    @Test
    fun inchesToCmText_oneDecimal() {
        assertEquals("101.6", inchesToCmText(40f, useBangla = false))
        assertEquals("৬২.২", inchesToCmText(24.5f))
    }

    @Test
    fun feetInchesToCm_rounds() {
        assertEquals(170, feetInchesToCm(5, 7))
        assertEquals(160, feetInchesToCm(5, 3))
        assertEquals(152, feetInchesToCm(5, 0))
    }
}
