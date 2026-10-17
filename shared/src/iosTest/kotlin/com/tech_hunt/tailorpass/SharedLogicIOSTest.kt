package com.tech_hunt.tailorpass

import com.tech_hunt.tailorpass.util.formatInches
import kotlin.test.Test
import kotlin.test.assertEquals

// Placeholder until Step 11 (iOS); runs the common formatter on iOS.
class SharedLogicIOSTest {

    @Test
    fun formatInches_onIos() {
        assertEquals("২৪½", formatInches(24.5f))
    }
}
