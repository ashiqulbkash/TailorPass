package com.tech_hunt.tailorpass.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_cancel
import com.tech_hunt.tailorpass.resources.backup_done
import com.tech_hunt.tailorpass.resources.data_counts
import com.tech_hunt.tailorpass.resources.delete_action
import com.tech_hunt.tailorpass.resources.delete_body
import com.tech_hunt.tailorpass.resources.delete_confirm_label
import com.tech_hunt.tailorpass.resources.delete_confirm_word
import com.tech_hunt.tailorpass.resources.delete_title
import com.tech_hunt.tailorpass.resources.ic_backup
import com.tech_hunt.tailorpass.resources.ic_info
import com.tech_hunt.tailorpass.resources.ic_language
import com.tech_hunt.tailorpass.resources.ic_restore
import com.tech_hunt.tailorpass.resources.ic_shield
import com.tech_hunt.tailorpass.resources.ic_tape
import com.tech_hunt.tailorpass.resources.ic_trash
import com.tech_hunt.tailorpass.resources.ic_warning
import com.tech_hunt.tailorpass.resources.language_bangla
import com.tech_hunt.tailorpass.resources.language_english
import com.tech_hunt.tailorpass.resources.restore_action
import com.tech_hunt.tailorpass.resources.restore_contents
import com.tech_hunt.tailorpass.resources.restore_merge
import com.tech_hunt.tailorpass.resources.restore_merge_sub
import com.tech_hunt.tailorpass.resources.restore_replace
import com.tech_hunt.tailorpass.resources.restore_replace_sub
import com.tech_hunt.tailorpass.resources.restore_replace_warning
import com.tech_hunt.tailorpass.resources.restore_title
import com.tech_hunt.tailorpass.resources.settings_about
import com.tech_hunt.tailorpass.resources.settings_backup
import com.tech_hunt.tailorpass.resources.settings_backup_last
import com.tech_hunt.tailorpass.resources.settings_data
import com.tech_hunt.tailorpass.resources.settings_delete_all
import com.tech_hunt.tailorpass.resources.settings_delete_all_sub
import com.tech_hunt.tailorpass.resources.settings_general
import com.tech_hunt.tailorpass.resources.settings_language
import com.tech_hunt.tailorpass.resources.settings_other
import com.tech_hunt.tailorpass.resources.settings_privacy
import com.tech_hunt.tailorpass.resources.settings_restore
import com.tech_hunt.tailorpass.resources.settings_restore_sub
import com.tech_hunt.tailorpass.resources.settings_title
import com.tech_hunt.tailorpass.resources.settings_unit
import com.tech_hunt.tailorpass.resources.settings_version
import com.tech_hunt.tailorpass.resources.unit_cm
import com.tech_hunt.tailorpass.resources.unit_inch
import com.tech_hunt.tailorpass.ui.components.BottomNav
import com.tech_hunt.tailorpass.ui.components.BottomTab
import com.tech_hunt.tailorpass.ui.components.DialogCard
import com.tech_hunt.tailorpass.ui.components.DialogOverlayPreview
import com.tech_hunt.tailorpass.ui.components.MaapDialog
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.MaapTextField
import com.tech_hunt.tailorpass.ui.components.SettingsGroup
import com.tech_hunt.tailorpass.ui.components.SettingsRow
import com.tech_hunt.tailorpass.ui.components.SuccessSnackbar
import com.tech_hunt.tailorpass.ui.components.TabTitleBar
import com.tech_hunt.tailorpass.ui.components.WarningNote
import com.tech_hunt.tailorpass.ui.components.selectableCard
import com.tech_hunt.tailorpass.ui.screens.manual.MeasureUnit
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.LANGUAGE_BN
import com.tech_hunt.tailorpass.ui.util.emphasize
import com.tech_hunt.tailorpass.ui.util.formatDate
import com.tech_hunt.tailorpass.ui.util.localDigits
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/** How long the backup snackbar (12.2) stays before it closes by itself. */
private const val SNACKBAR_MILLIS = 4000L

/**
 * 12.1 Settings tab: General (language, unit), Data (backup, restore), Other (privacy policy, about), and the red
 * "delete all data" card. Overlays: backup snackbar (12.2), restore dialog (12.3), delete-all dialog (12.4).
 */
@Composable
fun SettingsScreen(state: SettingsState, onTabSelect: (BottomTab) -> Unit, onAction: (SettingsAction) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        SettingsContent(state, onTabSelect, onAction)
        if (state.overlay == SettingsOverlay.BackupDone) {
            LaunchedEffect(Unit) {
                delay(SNACKBAR_MILLIS)
                onAction(SettingsAction.DismissOverlay)
            }
            BackupSnackbar(onAction, Modifier.align(Alignment.BottomCenter))
        }
    }
    when (val overlay = state.overlay) {
        is SettingsOverlay.Restore -> MaapDialog({ onAction(SettingsAction.DismissOverlay) }) { RestoreDialogCard(overlay, onAction) }
        is SettingsOverlay.DeleteAll -> MaapDialog({ onAction(SettingsAction.DismissOverlay) }) {
            DeleteDialogCard(overlay, state.profileCount, state.measurementCount, onAction)
        }
        SettingsOverlay.BackupDone, null -> Unit
    }
}

@Composable
private fun SettingsContent(state: SettingsState, onTabSelect: (BottomTab) -> Unit, onAction: (SettingsAction) -> Unit) {
    MaapScaffold(
        topBar = { TabTitleBar(stringResource(Res.string.settings_title)) },
        bottomNav = { BottomNav(selected = BottomTab.Settings, onSelect = onTabSelect) },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, bottom = Dimens.space12),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space6)) {
            GroupHeader(stringResource(Res.string.settings_general), top = Dimens.space4)
            SettingsGroup {
                SettingsRow(
                    icon = painterResource(Res.drawable.ic_language),
                    title = stringResource(Res.string.settings_language),
                    value = stringResource(
                        if (state.language == LANGUAGE_BN) Res.string.language_bangla else Res.string.language_english,
                    ),
                    onClick = { onAction(SettingsAction.ToggleLanguage) },
                    divider = true,
                )
                SettingsRow(
                    icon = painterResource(Res.drawable.ic_tape),
                    title = stringResource(Res.string.settings_unit),
                    value = stringResource(if (state.unit == MeasureUnit.Inch) Res.string.unit_inch else Res.string.unit_cm),
                    onClick = { onAction(SettingsAction.ToggleUnit) },
                )
            }
            GroupHeader(stringResource(Res.string.settings_data), top = Dimens.space10)
            SettingsGroup {
                SettingsRow(
                    icon = painterResource(Res.drawable.ic_backup),
                    title = stringResource(Res.string.settings_backup),
                    subtitle = state.lastBackup?.let { stringResource(Res.string.settings_backup_last, formatDate(it)) },
                    minHeight = 66.dp,
                    onClick = { onAction(SettingsAction.Backup) },
                    divider = true,
                )
                SettingsRow(
                    icon = painterResource(Res.drawable.ic_restore),
                    title = stringResource(Res.string.settings_restore),
                    subtitle = stringResource(Res.string.settings_restore_sub),
                    onClick = { onAction(SettingsAction.Restore) },
                )
            }
            GroupHeader(stringResource(Res.string.settings_other), top = Dimens.space10)
            SettingsGroup {
                SettingsRow(
                    icon = painterResource(Res.drawable.ic_shield),
                    title = stringResource(Res.string.settings_privacy),
                    onClick = { onAction(SettingsAction.OpenPrivacy) },
                    divider = true,
                )
                SettingsRow(
                    icon = painterResource(Res.drawable.ic_info),
                    title = stringResource(Res.string.settings_about),
                    value = stringResource(Res.string.settings_version, localDigits(SettingsState.APP_VERSION)),
                    onClick = { onAction(SettingsAction.OpenAbout) },
                )
            }
            val shape = MaterialTheme.shapes.large
            SettingsRow(
                icon = painterResource(Res.drawable.ic_trash),
                title = stringResource(Res.string.settings_delete_all),
                subtitle = stringResource(Res.string.settings_delete_all_sub),
                onClick = { onAction(SettingsAction.DeleteAll) },
                modifier = Modifier
                    .padding(top = Dimens.space14)
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaapTheme.colors.errorBorder, shape),
                chevron = false,
                endPadding = Dimens.space16,
                iconTint = MaterialTheme.colorScheme.error,
                titleColor = MaterialTheme.colorScheme.error,
                titleWeight = FontWeight.Bold,
                subtitleColor = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
    }
}

/** "সাধারণ" / "ডেটা" / "অন্যান্য": 14 bold grey, 4dp in from the cards. */
@Composable
private fun GroupHeader(text: String, top: Dp) {
    Text(
        text = text,
        modifier = Modifier.padding(start = Dimens.space4, end = Dimens.space4, top = top).semantics { heading() },
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** 12.2: 12dp from the sides, 14dp above the bottom nav. */
@Composable
private fun BackupSnackbar(onAction: (SettingsAction) -> Unit, modifier: Modifier = Modifier) {
    SuccessSnackbar(
        text = stringResource(Res.string.backup_done),
        onClose = { onAction(SettingsAction.DismissOverlay) },
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
            .padding(start = Dimens.space12, end = Dimens.space12, bottom = Dimens.bottomNavHeight + Dimens.space14)
            .fillMaxWidth(),
    )
}

/** "৩টি প্রোফাইল ও ৫টি মাপ" in Bengali digits (Bangla) — the bold part of 12.3 / 12.4. */
@Composable
private fun counts(profiles: Int, measurements: Int): String =
    stringResource(Res.string.data_counts, localDigits(profiles.toString()), localDigits(measurements.toString()))

/**
 * 12.3 Restore: upload icon, file summary (counts bold, file name), merge / replace radio cards (replace adds a
 * warning), Cancel / Restore. 22 / 18 / 18 padding, 12dp gaps.
 */
@Composable
internal fun RestoreDialogCard(overlay: SettingsOverlay.Restore, onAction: (SettingsAction) -> Unit) {
    val file = overlay.file
    DialogCard(
        icon = painterResource(Res.drawable.ic_restore),
        title = stringResource(Res.string.restore_title),
        cancelText = stringResource(Res.string.action_cancel),
        onCancel = { onAction(SettingsAction.DismissOverlay) },
        confirmText = stringResource(Res.string.restore_action),
        onConfirm = { onAction(SettingsAction.ConfirmRestore) },
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 22.dp, bottom = 18.dp),
        gap = Dimens.space12,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                .padding(horizontal = Dimens.space12, vertical = Dimens.space10),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val counts = counts(file.profiles, file.measurements)
            val profiles = localDigits(file.profiles.toString())
            val measurements = localDigits(file.measurements.toString())
            Text(
                text = emphasize(stringResource(Res.string.restore_contents, profiles, measurements), counts),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 23.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = file.fileName,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
            RestoreOption(
                title = stringResource(Res.string.restore_merge),
                subtitle = stringResource(Res.string.restore_merge_sub),
                selected = overlay.mode == RestoreMode.Merge,
                onClick = { onAction(SettingsAction.SetRestoreMode(RestoreMode.Merge)) },
            )
            RestoreOption(
                title = stringResource(Res.string.restore_replace),
                subtitle = stringResource(Res.string.restore_replace_sub),
                selected = overlay.mode == RestoreMode.Replace,
                onClick = { onAction(SettingsAction.SetRestoreMode(RestoreMode.Replace)) },
            )
        }
        if (overlay.mode == RestoreMode.Replace) {
            WarningNote(stringResource(Res.string.restore_replace_warning), icon = painterResource(Res.drawable.ic_warning))
        }
    }
}

/** Radio card: min 64dp, 14dp corners, 22dp radio · 16 title (bold when chosen, else semibold) over a 13 grey line. */
@Composable
private fun RestoreOption(title: String, subtitle: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .selectableCard(selected, onClick, unselectedBorder = MaterialTheme.colorScheme.outlineVariant)
            .padding(horizontal = Dimens.space12, vertical = Dimens.space8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
    ) {
        RadioMark(selected)
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                lineHeight = 21.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** 22dp ring, 2dp border: primary with a 10dp dot when chosen, grey when not. */
@Composable
private fun RadioMark(selected: Boolean) {
    Box(
        Modifier
            .size(22.dp)
            .border(2.dp, if (selected) MaterialTheme.colorScheme.primary else MaapTheme.colors.checkboxBorder, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) Box(Modifier.size(10.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
    }
}

/**
 * 12.4 Delete all: red warning circle, red box with the counts in bold, type-to-confirm field (the confirm word
 * enables the red "সব মুছুন"), Cancel / Delete all.
 */
@Composable
internal fun DeleteDialogCard(
    overlay: SettingsOverlay.DeleteAll,
    profiles: Int,
    measurements: Int,
    onAction: (SettingsAction) -> Unit,
) {
    val word = stringResource(Res.string.delete_confirm_word)
    val confirmed = isDeleteConfirmed(overlay.typed, word)
    DialogCard(
        icon = painterResource(Res.drawable.ic_warning),
        title = stringResource(Res.string.delete_title),
        cancelText = stringResource(Res.string.action_cancel),
        onCancel = { onAction(SettingsAction.DismissOverlay) },
        confirmText = stringResource(Res.string.delete_action),
        onConfirm = { if (confirmed) onAction(SettingsAction.ConfirmDelete) },
        confirmEnabled = confirmed,
        confirmColor = MaterialTheme.colorScheme.error,
        onConfirmColor = MaterialTheme.colorScheme.onError,
        iconTint = MaterialTheme.colorScheme.error,
        iconBackground = MaterialTheme.colorScheme.errorContainer,
        iconSize = 30.dp,
    ) {
        val body = stringResource(
            Res.string.delete_body,
            localDigits(profiles.toString()),
            localDigits(measurements.toString()),
        )
        Text(
            text = emphasize(body, counts(profiles, measurements)),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(12.dp))
                .padding(horizontal = Dimens.space12, vertical = Dimens.space10),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center,
        )
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space6)) {
            val label = stringResource(Res.string.delete_confirm_label)
            Text(
                text = emphasize(label, "\"$word\""),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp,
                color = MaapTheme.colors.textEmphasis,
            )
            MaapTextField(
                value = overlay.typed,
                onValueChange = { onAction(SettingsAction.SetDeleteText(it)) },
                contentDescription = label,
                placeholder = word,
                height = Dimens.dialogButtonHeight,
                horizontalPadding = Dimens.space14,
                accentColor = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun SettingsPreview() {
    MaapTheme(darkTheme = false) { SettingsScreen(SettingsState.Sample, {}) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun BackupSnackbarPreview() {
    MaapTheme(darkTheme = false) { SettingsScreen(SettingsState.Sample.copy(overlay = SettingsOverlay.BackupDone), {}) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun RestoreDialogPreview() {
    MaapTheme(darkTheme = false) {
        DialogOverlayPreview(base = { SettingsContent(SettingsState.Sample, {}) {} }, top = 120.dp) {
            RestoreDialogCard(SettingsOverlay.Restore(SettingsState.SampleBackupFile)) {}
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun DeleteDialogPreview() {
    MaapTheme(darkTheme = false) {
        DialogOverlayPreview(base = { SettingsContent(SettingsState.Sample, {}) {} }, top = 150.dp) {
            DeleteDialogCard(SettingsOverlay.DeleteAll("মুছুন"), 3, 5) {}
        }
    }
}
