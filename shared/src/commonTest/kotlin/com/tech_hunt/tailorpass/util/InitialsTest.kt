package com.tech_hunt.tailorpass.util

import kotlin.test.Test
import kotlin.test.assertEquals

class InitialsTest {

    @Test
    fun banglaKeepsVowelSign() {
        assertEquals("রা", initialOf("রাহাত"))
        assertEquals("ফা", initialOf("ফারহানা"))
    }

    @Test
    fun independentVowelStandsAlone() {
        assertEquals("আ", initialOf("আব্বু"))
    }

    @Test
    fun conjunctStaysWhole() {
        assertEquals("ক্ষ", initialOf("ক্ষমা"))
        assertEquals("স্নে", initialOf("স্নেহা"))
    }

    @Test
    fun englishIsUppercased() {
        assertEquals("R", initialOf("rahat"))
        assertEquals("F", initialOf("  Farhana "))
    }

    @Test
    fun blankNameGivesEmpty() {
        assertEquals("", initialOf(""))
        assertEquals("", initialOf("   "))
    }

    @Test
    fun trailingViramaIsKept() {
        assertEquals("ক্", initialOf("ক্"))
    }
}
