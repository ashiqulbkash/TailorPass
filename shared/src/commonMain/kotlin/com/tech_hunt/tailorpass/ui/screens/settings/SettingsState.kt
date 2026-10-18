package com.tech_hunt.tailorpass.ui.screens.settings

import com.tech_hunt.tailorpass.ui.screens.manual.MeasureUnit
import com.tech_hunt.tailorpass.ui.util.CalendarDate
import com.tech_hunt.tailorpass.ui.util.LANGUAGE_BN
import com.tech_hunt.tailorpass.ui.util.LANGUAGE_EN

/** Restore choice (12.3): add the file's data to the phone's, or keep only the file's. */
enum class RestoreMode { Merge, Replace }

/** What a backup file holds, as the restore dialog shows it. The file name is data, not a resource. */
data class BackupFile(val fileName: String, val profiles: Int, val measurements: Int)

/** Snackbar and dialogs over Settings (overlays, not routes). */
sealed interface SettingsOverlay {
    /** 12.2 "ব্যাকআপ সেভ হয়েছে". */
    data object BackupDone : SettingsOverlay

    /** 12.3: the picked [file] and the chosen [mode]. */
    data class Restore(val file: BackupFile, val mode: RestoreMode = RestoreMode.Merge) : SettingsOverlay

    /** 12.4: what has been typed into the confirm field. */
    data class DeleteAll(val typed: String = "") : SettingsOverlay
}

/**
 * Settings tab (12.1–12.4). [language]: LANGUAGE_BN / LANGUAGE_EN; [unit]: the app's display unit;
 * [lastBackup]: null = never backed up; the counts are what "delete all" removes.
 * Design phase: backup and restore only show their overlays (files come with FileKit in Step 10).
 */
data class SettingsState(
    val language: String,
    val unit: MeasureUnit,
    val lastBackup: CalendarDate?,
    val profileCount: Int,
    val measurementCount: Int,
    val overlay: SettingsOverlay? = null,
) {
    companion object {
        /** Shown on "অ্যাপ সম্পর্কে" (12.1, 12.6). Step 10 may read it from the build. */
        const val APP_VERSION = "1.0"

        /** "হালনাগাদ: ১৬ অক্টোবর ২০২৬" on the privacy policy (12.5). */
        val PolicyUpdated = CalendarDate(2026, 10, 16)

        /** Design sample of a picked backup file (12.3): 3 profiles, 5 measurements. */
        val SampleBackupFile = BackupFile("tailorpass-backup-2026-10-10.json", 3, 5)

        /** 12.1: Bangla, inches, last backup 10 Oct 2026, 3 profiles and 5 measurements. */
        val Sample = SettingsState(LANGUAGE_BN, MeasureUnit.Inch, CalendarDate(2026, 10, 10), 3, 5)
    }
}

sealed interface SettingsAction {
    data object ToggleLanguage : SettingsAction
    data object ToggleUnit : SettingsAction
    data object Backup : SettingsAction
    data object Restore : SettingsAction
    data class SetRestoreMode(val mode: RestoreMode) : SettingsAction
    data object ConfirmRestore : SettingsAction
    data object OpenPrivacy : SettingsAction
    data object OpenAbout : SettingsAction
    data object DeleteAll : SettingsAction
    data class SetDeleteText(val text: String) : SettingsAction
    data object ConfirmDelete : SettingsAction
    data object DismissOverlay : SettingsAction
}

/** The confirm word typed (12.4): surrounding spaces and letter case do not matter ("delete" = "DELETE"). */
fun isDeleteConfirmed(typed: String, word: String): Boolean = typed.trim().equals(word.trim(), ignoreCase = true)

/**
 * Pure Settings reducer. The caller applies the language and unit, opens the policy / about pages and deletes the
 * data on ConfirmDelete; actions for an overlay that is not open are ignored.
 */
fun SettingsState.reduce(action: SettingsAction): SettingsState = when (action) {
    SettingsAction.ToggleLanguage -> copy(language = if (language == LANGUAGE_BN) LANGUAGE_EN else LANGUAGE_BN, overlay = null)
    SettingsAction.ToggleUnit -> copy(unit = if (unit == MeasureUnit.Inch) MeasureUnit.Cm else MeasureUnit.Inch, overlay = null)
    SettingsAction.Backup -> copy(overlay = SettingsOverlay.BackupDone)
    SettingsAction.Restore -> copy(overlay = SettingsOverlay.Restore(SettingsState.SampleBackupFile))
    is SettingsAction.SetRestoreMode -> (overlay as? SettingsOverlay.Restore)?.let { copy(overlay = it.copy(mode = action.mode)) } ?: this
    SettingsAction.DeleteAll -> copy(overlay = SettingsOverlay.DeleteAll())
    is SettingsAction.SetDeleteText -> (overlay as? SettingsOverlay.DeleteAll)?.let { copy(overlay = it.copy(typed = action.text)) } ?: this
    SettingsAction.ConfirmRestore -> if (overlay is SettingsOverlay.Restore) copy(overlay = null) else this
    SettingsAction.ConfirmDelete -> if (overlay is SettingsOverlay.DeleteAll) copy(overlay = null, profileCount = 0, measurementCount = 0) else this
    SettingsAction.OpenPrivacy, SettingsAction.OpenAbout, SettingsAction.DismissOverlay -> copy(overlay = null)
}
