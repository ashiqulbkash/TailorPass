package com.tech_hunt.tailorpass.util

import kotlin.test.Test
import kotlin.test.assertEquals

class PossessiveTest {
    @Test
    fun banglaConsonantEndingTakesEr() {
        assertEquals("রাহাতের", possessiveOf("রাহাত", bangla = true))
    }

    @Test
    fun banglaVowelEndingTakesR() {
        assertEquals("ফারহানার", possessiveOf("ফারহানা", bangla = true))
        assertEquals("আব্বুর", possessiveOf("আব্বু", bangla = true))
        assertEquals("রিয়ার", possessiveOf("রিয়া", bangla = true))
    }

    @Test
    fun latinNameInBanglaUsesHyphenEr() {
        assertEquals("Rahat-এর", possessiveOf("Rahat", bangla = true))
    }

    @Test
    fun englishTakesApostropheS() {
        assertEquals("Rahat's", possessiveOf(" Rahat ", bangla = false))
        assertEquals("রাহাত's", possessiveOf("রাহাত", bangla = false))
    }

    @Test
    fun blankNameIsEmpty() {
        assertEquals("", possessiveOf("  ", bangla = true))
        assertEquals("", possessiveOf("", bangla = false))
    }
}
