package com.tech_hunt.tailorpass.ui.util

import kotlin.test.Test
import kotlin.test.assertEquals

class AppLanguageTest {

    @Test
    fun appLanguageOf_englishTags() {
        assertEquals(LANGUAGE_EN, appLanguageOf("en"))
        assertEquals(LANGUAGE_EN, appLanguageOf("en-US"))
        assertEquals(LANGUAGE_EN, appLanguageOf("EN-gb"))
    }

    @Test
    fun appLanguageOf_everythingElseIsBangla() {
        assertEquals(LANGUAGE_BN, appLanguageOf("bn"))
        assertEquals(LANGUAGE_BN, appLanguageOf("bn-BD"))
        assertEquals(LANGUAGE_BN, appLanguageOf("de-DE"))
        assertEquals(LANGUAGE_BN, appLanguageOf(""))
    }
}
