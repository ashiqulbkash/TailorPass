package com.tech_hunt.tailorpass.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_help
import com.tech_hunt.tailorpass.resources.garment_for_men
import com.tech_hunt.tailorpass.resources.garment_for_women
import com.tech_hunt.tailorpass.resources.garment_size_suggestion
import com.tech_hunt.tailorpass.resources.home_empty
import com.tech_hunt.tailorpass.resources.home_new_measure
import com.tech_hunt.tailorpass.resources.home_profile_chip_cd
import com.tech_hunt.tailorpass.resources.home_saved
import com.tech_hunt.tailorpass.resources.home_saved_count
import com.tech_hunt.tailorpass.resources.home_saved_size
import com.tech_hunt.tailorpass.resources.home_saved_subtitle
import com.tech_hunt.tailorpass.resources.home_saved_value
import com.tech_hunt.tailorpass.resources.home_sent_to_tailor
import com.tech_hunt.tailorpass.resources.home_share_cd
import com.tech_hunt.tailorpass.resources.home_whose
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.resources.ic_edit
import com.tech_hunt.tailorpass.resources.ic_help
import com.tech_hunt.tailorpass.resources.ic_plus
import com.tech_hunt.tailorpass.resources.ic_refresh
import com.tech_hunt.tailorpass.resources.ic_share
import com.tech_hunt.tailorpass.resources.ic_trash
import com.tech_hunt.tailorpass.resources.illus_empty_saved
import com.tech_hunt.tailorpass.resources.profile_edit_cd
import com.tech_hunt.tailorpass.resources.profile_measure_count
import com.tech_hunt.tailorpass.resources.profile_new
import com.tech_hunt.tailorpass.resources.profile_no_measure
import com.tech_hunt.tailorpass.resources.profile_sheet_title
import com.tech_hunt.tailorpass.resources.saved_menu_delete
import com.tech_hunt.tailorpass.resources.saved_menu_edit
import com.tech_hunt.tailorpass.resources.saved_menu_remeasure
import com.tech_hunt.tailorpass.resources.saved_menu_view_share
import com.tech_hunt.tailorpass.ui.components.Avatar
import com.tech_hunt.tailorpass.ui.components.BottomNav
import com.tech_hunt.tailorpass.ui.components.BottomSheetContent
import com.tech_hunt.tailorpass.ui.components.BottomTab
import com.tech_hunt.tailorpass.ui.components.GarmentCard
import com.tech_hunt.tailorpass.ui.components.GarmentTile
import com.tech_hunt.tailorpass.ui.components.MaapBottomSheet
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapIconButton
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.OutlinedPillButton
import com.tech_hunt.tailorpass.ui.components.ProfileChip
import com.tech_hunt.tailorpass.ui.components.SavedRow
import com.tech_hunt.tailorpass.ui.components.SheetOverlayPreview
import com.tech_hunt.tailorpass.ui.components.selectableCard
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.drawable
import com.tech_hunt.tailorpass.ui.util.formatDate
import com.tech_hunt.tailorpass.ui.util.localDigits
import com.tech_hunt.tailorpass.ui.util.str
import com.tech_hunt.tailorpass.ui.util.useBanglaDigits
import com.tech_hunt.tailorpass.util.formatInches
import com.tech_hunt.tailorpass.util.initialOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val ProfileSheetPadding = PaddingValues(start = Dimens.space16, end = Dimens.space16, top = Dimens.space10, bottom = Dimens.space24)
private val SavedMenuPadding = PaddingValues(start = Dimens.space8, end = Dimens.space8, top = Dimens.space10, bottom = Dimens.space20)

/**
 * Home (2.1–2.3) with its two sheets (2.4 profiles, 2.5 saved-measurement menu).
 * 2.3 is the same screen when the current profile has nothing saved.
 */
@Composable
fun HomeScreen(state: HomeState, onTabSelect: (BottomTab) -> Unit, onAction: (HomeAction) -> Unit) {
    val saved = state.currentSaved
    MaapScaffold(
        topBar = { HomeHeader(state.currentProfile, onAction) },
        bottomNav = { BottomNav(selected = BottomTab.Home, onSelect = onTabSelect) },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space4, bottom = Dimens.space16),
    ) {
        SectionTitle(stringResource(Res.string.home_new_measure), Modifier.padding(top = Dimens.space4, bottom = Dimens.space12))
        GarmentGrid(onAction)
        Row(Modifier.fillMaxWidth().padding(top = Dimens.space24, bottom = Dimens.space10)) {
            SectionTitle(stringResource(Res.string.home_saved), Modifier.weight(1f).alignByBaseline())
            if (saved.isNotEmpty()) {
                Text(
                    text = stringResource(Res.string.home_saved_count, localDigits(saved.size.toString())),
                    modifier = Modifier.alignByBaseline(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (saved.isEmpty()) {
            EmptySaved()
        } else {
            SavedList(saved, onAction)
        }
    }

    when (state.overlay) {
        HomeOverlay.ProfileSheet -> MaapBottomSheet(
            onDismiss = { onAction(HomeAction.DismissOverlay) },
            contentPadding = ProfileSheetPadding,
            verticalArrangement = Arrangement.spacedBy(Dimens.space12),
        ) { ProfileSheetBody(state, onAction) }
        is HomeOverlay.SavedMenu -> state.menuItem?.let { item ->
            MaapBottomSheet(onDismiss = { onAction(HomeAction.DismissOverlay) }, contentPadding = SavedMenuPadding) {
                SavedMenuBody(item, state.currentProfile.name, onAction)
            }
        }
        null -> Unit
    }
}

@Composable
private fun HomeHeader(profile: ProfileSummary, onAction: (HomeAction) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Dimens.space16, end = Dimens.space16, top = Dimens.space16, bottom = Dimens.space8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
    ) {
        ProfileChip(
            name = profile.name,
            initial = initialOf(profile.name),
            label = stringResource(Res.string.home_whose),
            contentDescription = stringResource(Res.string.home_profile_chip_cd, profile.name),
            onClick = { onAction(HomeAction.OpenProfileSheet) },
        )
        Spacer(Modifier.weight(1f))
        MaapIconButton(
            icon = painterResource(Res.drawable.ic_help),
            contentDescription = stringResource(Res.string.action_help),
            onClick = { onAction(HomeAction.Help) },
            iconSize = 26.dp,
        )
    }
}

@Composable
private fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

/** The 6 garments in 2 columns, in Garment order (shirt, pants, panjabi, pajama, salwar kameez, genji). */
@Composable
private fun GarmentGrid(onAction: (HomeAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.space10)) {
        Garment.entries.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space10)) {
                row.forEach { garment ->
                    GarmentCard(
                        icon = painterResource(drawable(garment.iconName)),
                        title = str(garment.labelKey),
                        subtitle = stringResource(
                            when {
                                garment.sizeOnly -> Res.string.garment_size_suggestion
                                garment.forWomen -> Res.string.garment_for_women
                                else -> Res.string.garment_for_men
                            },
                        ),
                        onClick = { onAction(HomeAction.StartMeasure(garment)) },
                        modifier = Modifier.weight(1f),
                        titleFontSize = if (garment == Garment.SalwarKameez) 16.sp else 17.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedList(saved: List<SavedMeasurement>, onAction: (HomeAction) -> Unit) {
    MaapCard(
        modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.large),
        contentPadding = PaddingValues(0.dp),
    ) {
        saved.forEachIndexed { index, item ->
            val title = str(item.garment.labelKey)
            val detail = detailText(item.detail)
            val date = formatDate(item.date)
            SavedRow(
                title = title,
                // "date · detail", or "date ·" followed by the green sent-to-tailor status
                subtitle = stringResource(Res.string.home_saved_subtitle, date, detail ?: "").trimEnd(),
                status = if (detail == null) stringResource(Res.string.home_sent_to_tailor) else null,
                onClick = { onAction(HomeAction.OpenSaved(item.id)) },
                onLongClick = { onAction(HomeAction.OpenSavedMenu(item.id)) },
                onShare = { onAction(HomeAction.ShareSaved(item.id)) },
                shareContentDescription = stringResource(Res.string.home_share_cd, title),
                showDivider = index < saved.lastIndex,
            )
        }
    }
}

/** Text after the date; null for SentToTailor (shown as the green status instead). */
@Composable
private fun detailText(detail: SavedDetail): String? = when (detail) {
    is SavedDetail.Size -> stringResource(Res.string.home_saved_size, detail.size)
    is SavedDetail.Value -> stringResource(
        Res.string.home_saved_value,
        str(detail.labelKey),
        formatInches(detail.inches, useBangla = useBanglaDigits()),
    )
    SavedDetail.SentToTailor -> null
}

/**
 * 2.3: dashed box with the hanger-and-tape illustration and a hint to pick a garment.
 * 262dp = its height in the 360×800 design; the page scrolls when a phone has less room.
 */
@Composable
private fun EmptySaved() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 262.dp)
            .dashedBorder(MaapTheme.colors.dashedBorder, 1.5.dp, 16.dp)
            .padding(horizontal = Dimens.space20, vertical = Dimens.space16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.space14, Alignment.CenterVertically),
    ) {
        Image(
            painter = painterResource(Res.drawable.illus_empty_saved),
            contentDescription = null,
            modifier = Modifier.size(width = 140.dp, height = 96.dp),
        )
        Text(
            text = stringResource(Res.string.home_empty),
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 25.5.sp,
            color = MaapTheme.colors.textEmphasis,
            textAlign = TextAlign.Center,
        )
    }
}

private fun Modifier.dashedBorder(color: Color, width: Dp, radius: Dp): Modifier = drawBehind {
    val stroke = width.toPx()
    drawRoundRect(
        color = color,
        topLeft = Offset(stroke / 2, stroke / 2),
        size = Size(size.width - stroke, size.height - stroke),
        cornerRadius = CornerRadius(radius.toPx()),
        style = Stroke(width = stroke, pathEffect = PathEffect.dashPathEffect(floatArrayOf(5.dp.toPx(), 4.dp.toPx()))),
    )
}

// ---------- 2.4 Profile sheet ----------

@Composable
private fun ColumnScope.ProfileSheetBody(state: HomeState, onAction: (HomeAction) -> Unit) {
    Text(
        text = stringResource(Res.string.profile_sheet_title),
        modifier = Modifier.padding(start = Dimens.space4, end = Dimens.space4, top = Dimens.space4),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface,
    )
    Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
        state.profiles.forEach { profile ->
            ProfileOption(
                profile = profile,
                count = state.savedCount(profile.id),
                selected = profile.id == state.currentProfileId,
                onSelect = { onAction(HomeAction.SelectProfile(profile.id)) },
                onEdit = { onAction(HomeAction.EditProfile(profile.id)) },
            )
        }
    }
    OutlinedPillButton(
        text = stringResource(Res.string.profile_new),
        onClick = { onAction(HomeAction.NewProfile) },
        modifier = Modifier.fillMaxWidth(),
        icon = painterResource(Res.drawable.ic_plus),
        fontSize = 18.sp,
        iconSize = 22.dp,
    )
}

/** One profile (radio): avatar · name + "gender · N measurements" · ✓ when selected · edit button. */
@Composable
private fun ProfileOption(
    profile: ProfileSummary,
    count: Int,
    selected: Boolean,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
) {
    val gender = str(profile.gender.labelKey)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectableCard(selected, onSelect, MaterialTheme.shapes.large, MaterialTheme.colorScheme.outlineVariant)
            .padding(start = Dimens.space10, end = Dimens.space6, top = Dimens.space6, bottom = Dimens.space6),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space6),
    ) {
        Row(
            modifier = Modifier.weight(1f).heightIn(min = 52.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
        ) {
            Avatar(
                initial = initialOf(profile.name),
                background = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaapTheme.colors.textEmphasis,
                size = 44.dp,
            )
            Column(Modifier.weight(1f)) {
                Text(
                    text = profile.name,
                    style = MaterialTheme.typography.titleMedium,
                    lineHeight = 23.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                )
                Text(
                    text = if (count > 0) {
                        stringResource(Res.string.profile_measure_count, gender, localDigits(count.toString()))
                    } else {
                        stringResource(Res.string.profile_no_measure, gender)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
            if (selected) {
                Icon(
                    painter = painterResource(Res.drawable.ic_check),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
        MaapIconButton(
            icon = painterResource(Res.drawable.ic_edit),
            contentDescription = stringResource(Res.string.profile_edit_cd, profile.name),
            onClick = onEdit,
            iconSize = 22.dp,
        )
    }
}

// ---------- 2.5 Saved measurement menu ----------

@Composable
private fun ColumnScope.SavedMenuBody(item: SavedMeasurement, profileName: String, onAction: (HomeAction) -> Unit) {
    val divider = MaapTheme.colors.divider
    val subtitle = stringResource(
        Res.string.home_saved_subtitle,
        stringResource(Res.string.home_saved_subtitle, profileName, formatDate(item.date)),
        detailText(item.detail) ?: stringResource(Res.string.home_sent_to_tailor),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Dimens.space6)
            .drawBehind {
                val y = size.height - 0.5.dp.toPx()
                drawLine(divider, Offset(0f, y), Offset(size.width, y), 1.dp.toPx())
            }
            .padding(horizontal = Dimens.space12, vertical = Dimens.space14),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space12),
    ) {
        GarmentTile(painterResource(drawable(item.garment.iconName)))
        Column {
            Text(
                text = str(item.garment.labelKey),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 19.sp,
                lineHeight = 25.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
    MenuItem(painterResource(Res.drawable.ic_share), stringResource(Res.string.saved_menu_view_share)) {
        onAction(HomeAction.OpenSaved(item.id))
    }
    MenuItem(painterResource(Res.drawable.ic_edit), stringResource(Res.string.saved_menu_edit)) {
        onAction(HomeAction.EditSaved(item.id))
    }
    MenuItem(painterResource(Res.drawable.ic_refresh), stringResource(Res.string.saved_menu_remeasure)) {
        onAction(HomeAction.RemeasureSaved(item.id))
    }
    Spacer(
        Modifier
            .padding(horizontal = Dimens.space12, vertical = Dimens.space6)
            .fillMaxWidth()
            .height(1.dp)
            .drawBehind { drawRect(divider) },
    )
    MenuItem(
        icon = painterResource(Res.drawable.ic_trash),
        text = stringResource(Res.string.saved_menu_delete),
        color = MaterialTheme.colorScheme.error,
        iconTint = MaterialTheme.colorScheme.error,
        fontWeight = FontWeight.SemiBold,
    ) { onAction(HomeAction.DeleteSaved(item.id)) }
}

/** Sheet menu row: 56dp min, 24dp icon, 17 medium text, 16dp gap. */
@Composable
private fun MenuItem(
    icon: Painter,
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    fontWeight: FontWeight = FontWeight.Medium,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = Dimens.buttonHeight)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.space12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.space16),
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(Dimens.iconSize))
        Text(text = text, style = MaterialTheme.typography.bodyLarge, fontWeight = fontWeight, color = color)
    }
}

// ---------- Previews ----------

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun HomeScreenPreview() {
    MaapTheme(darkTheme = false) { HomeScreen(HomeState.Sample, {}) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun HomeScreenDarkPreview() {
    MaapTheme(darkTheme = true) { HomeScreen(HomeState.Sample, {}) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun HomeEmptyPreview() {
    MaapTheme(darkTheme = false) { HomeScreen(HomeState.SampleEmpty, {}) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ProfileSheetPreview() {
    MaapTheme(darkTheme = false) {
        SheetOverlayPreview(base = { HomeScreen(HomeState.Sample, {}) {} }) {
            BottomSheetContent(ProfileSheetPadding, Arrangement.spacedBy(Dimens.space12)) {
                ProfileSheetBody(HomeState.Sample) {}
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun SavedMenuSheetPreview() {
    val state = HomeState.Sample
    MaapTheme(darkTheme = false) {
        SheetOverlayPreview(base = { HomeScreen(state, {}) {} }) {
            BottomSheetContent(SavedMenuPadding) { SavedMenuBody(state.saved.first(), state.currentProfile.name) {} }
        }
    }
}
