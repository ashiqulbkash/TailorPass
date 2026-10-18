package com.tech_hunt.tailorpass.ui.screens.settings

import com.tech_hunt.tailorpass.ui.screens.manual.MeasureUnit
import com.tech_hunt.tailorpass.ui.util.LANGUAGE_BN
import com.tech_hunt.tailorpass.ui.util.LANGUAGE_EN
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SettingsStateTest {
    private val sample = SettingsState.Sample

    @Test
    fun sampleMatchesTheDesign() {
        assertEquals(LANGUAGE_BN, sample.language)
        assertEquals(MeasureUnit.Inch, sample.unit)
        assertEquals(3, sample.profileCount)
        assertEquals(5, sample.measurementCount)
        assertNull(sample.overlay)
    }

    @Test
    fun languageAndUnitToggleBackAndForth() {
        val en = sample.reduce(SettingsAction.ToggleLanguage)
        assertEquals(LANGUAGE_EN, en.language)
        assertEquals(LANGUAGE_BN, en.reduce(SettingsAction.ToggleLanguage).language)
        val cm = sample.reduce(SettingsAction.ToggleUnit)
        assertEquals(MeasureUnit.Cm, cm.unit)
        assertEquals(MeasureUnit.Inch, cm.reduce(SettingsAction.ToggleUnit).unit)
    }

    @Test
    fun backupShowsTheSnackbarUntilDismissed() {
        val done = sample.reduce(SettingsAction.Backup)
        assertEquals(SettingsOverlay.BackupDone, done.overlay)
        assertNull(done.reduce(SettingsAction.DismissOverlay).overlay)
    }

    @Test
    fun restoreStartsWithMergeAndCanSwitchToReplace() {
        val open = sample.reduce(SettingsAction.Restore)
        val restore = open.overlay as SettingsOverlay.Restore
        assertEquals(RestoreMode.Merge, restore.mode)
        assertEquals(SettingsState.SampleBackupFile, restore.file)
        val replace = open.reduce(SettingsAction.SetRestoreMode(RestoreMode.Replace))
        assertEquals(RestoreMode.Replace, (replace.overlay as SettingsOverlay.Restore).mode)
        assertNull(replace.reduce(SettingsAction.ConfirmRestore).overlay)
    }

    @Test
    fun overlayActionsWithoutTheirOverlayAreIgnored() {
        assertEquals(sample, sample.reduce(SettingsAction.SetRestoreMode(RestoreMode.Replace)))
        assertEquals(sample, sample.reduce(SettingsAction.SetDeleteText("মুছুন")))
        assertEquals(sample, sample.reduce(SettingsAction.ConfirmRestore))
        assertEquals(sample, sample.reduce(SettingsAction.ConfirmDelete))
    }

    @Test
    fun deleteTracksTheTypedTextAndClearsTheCounts() {
        val typed = sample.reduce(SettingsAction.DeleteAll).reduce(SettingsAction.SetDeleteText("মুছু"))
        assertEquals(SettingsOverlay.DeleteAll("মুছু"), typed.overlay)
        val deleted = typed.reduce(SettingsAction.ConfirmDelete)
        assertNull(deleted.overlay)
        assertEquals(0, deleted.profileCount)
        assertEquals(0, deleted.measurementCount)
    }

    @Test
    fun deleteNeedsTheExactWord() {
        assertTrue(isDeleteConfirmed("মুছুন", "মুছুন"))
        assertTrue(isDeleteConfirmed("  মুছুন ", "মুছুন"))
        assertTrue(isDeleteConfirmed("delete", "DELETE"))
        assertFalse(isDeleteConfirmed("মুছু", "মুছুন"))
        assertFalse(isDeleteConfirmed("", "মুছুন"))
        assertFalse(isDeleteConfirmed("মুছুন মুছুন", "মুছুন"))
    }

    @Test
    fun navigationActionsCloseAnOverlay() {
        val open = sample.reduce(SettingsAction.DeleteAll)
        assertNull(open.reduce(SettingsAction.OpenPrivacy).overlay)
        assertNull(open.reduce(SettingsAction.OpenAbout).overlay)
    }
}
