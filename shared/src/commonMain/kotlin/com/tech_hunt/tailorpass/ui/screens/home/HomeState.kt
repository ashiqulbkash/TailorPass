package com.tech_hunt.tailorpass.ui.screens.home

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.ui.util.CalendarDate

/**
 * A profile as Home and the profile sheet show it. The name is user data, not a resource key.
 * [heightInches]: the measuring flow needs it (7.6); null when the design gives none (Abbu).
 */
data class ProfileSummary(val id: Long, val name: String, val gender: Gender, val heightInches: Int? = null)

/** What a saved row shows after the date: the suggested size, "sent to tailor", or one measurement. */
sealed interface SavedDetail {
    data class Size(val size: String) : SavedDetail
    data object SentToTailor : SavedDetail
    /** [labelKey] = short string key ("size_col_waist" → কোমর), value in inches. */
    data class Value(val labelKey: String, val inches: Float) : SavedDetail
}

data class SavedMeasurement(
    val id: Long,
    val profileId: Long,
    val garment: Garment,
    val date: CalendarDate,
    val detail: SavedDetail,
)

/** An unfinished tape measurement (7.9): [done] of [total] measurements taken. One per profile and garment. */
data class IncompleteMeasurement(val profileId: Long, val garment: Garment, val done: Int, val total: Int)

/** Sheets shown over Home (overlays, not routes). */
sealed interface HomeOverlay {
    data object ProfileSheet : HomeOverlay
    data class SavedMenu(val savedId: Long) : HomeOverlay
}

/**
 * Home (2.1–2.5, 7.9). [saved] holds every profile's measurements, newest first; [incomplete] every profile's
 * unfinished tape measurements, newest first. The screen shows the current profile's: unfinished ones on top.
 */
data class HomeState(
    val profiles: List<ProfileSummary>,
    val currentProfileId: Long,
    val saved: List<SavedMeasurement>,
    val incomplete: List<IncompleteMeasurement> = emptyList(),
    val overlay: HomeOverlay? = null,
) {
    val currentProfile: ProfileSummary get() = profiles.first { it.id == currentProfileId }
    val currentSaved: List<SavedMeasurement> get() = saved.filter { it.profileId == currentProfileId }
    val currentIncomplete: List<IncompleteMeasurement> get() = incomplete.filter { it.profileId == currentProfileId }

    /** "সংরক্ষিত মাপ ৪টি" counts the unfinished ones too (7.9: 3 saved + 1). */
    val listedCount: Int get() = currentSaved.size + currentIncomplete.size
    val menuItem: SavedMeasurement?
        get() = (overlay as? HomeOverlay.SavedMenu)?.let { menu -> saved.firstOrNull { it.id == menu.savedId } }

    fun savedCount(profileId: Long): Int = saved.count { it.profileId == profileId }

    companion object {
        const val RAHAT = 1L
        const val FARHANA = 2L
        const val ABBU = 3L

        /** Design sample: Rahat (3 saved), Farhana (2), Abbu (none). */
        val Sample = HomeState(
            profiles = listOf(
                ProfileSummary(RAHAT, "রাহাত", Gender.Male, heightInches = 67),
                ProfileSummary(FARHANA, "ফারহানা", Gender.Female, heightInches = 63),
                ProfileSummary(ABBU, "আব্বু", Gender.Male),
            ),
            currentProfileId = RAHAT,
            saved = listOf(
                SavedMeasurement(1, RAHAT, Garment.Panjabi, CalendarDate(2026, 10, 16), SavedDetail.Size("L")),
                SavedMeasurement(2, RAHAT, Garment.Shirt, CalendarDate(2026, 9, 28), SavedDetail.SentToTailor),
                SavedMeasurement(3, RAHAT, Garment.Pants, CalendarDate(2026, 8, 15), SavedDetail.Value("size_col_waist", 34f)),
                SavedMeasurement(4, FARHANA, Garment.SalwarKameez, CalendarDate(2026, 10, 2), SavedDetail.Size("M")),
                SavedMeasurement(5, FARHANA, Garment.SalwarKameez, CalendarDate(2026, 6, 20), SavedDetail.SentToTailor),
            ),
        )

        /** 2.3: Abbu, nothing saved yet. */
        val SampleEmpty = Sample.copy(currentProfileId = ABBU)

        /** 7.9: Rahat left a panjabi tape measurement at 4/8. */
        val SampleIncomplete = Sample.copy(incomplete = listOf(IncompleteMeasurement(RAHAT, Garment.Panjabi, 4, 8)))
    }
}

sealed interface HomeAction {
    data object OpenProfileSheet : HomeAction
    data class SelectProfile(val profileId: Long) : HomeAction
    data class EditProfile(val profileId: Long) : HomeAction
    data object NewProfile : HomeAction
    data object Help : HomeAction
    data class StartMeasure(val garment: Garment) : HomeAction
    data class ContinueIncomplete(val garment: Garment) : HomeAction
    data class OpenSaved(val savedId: Long) : HomeAction
    data class ShareSaved(val savedId: Long) : HomeAction
    data class OpenSavedMenu(val savedId: Long) : HomeAction
    data class EditSaved(val savedId: Long) : HomeAction
    data class RemeasureSaved(val savedId: Long) : HomeAction
    data class DeleteSaved(val savedId: Long) : HomeAction
    data object DismissOverlay : HomeAction
}

/**
 * Pure Home reducer. Navigation actions (new/edit profile, start, continue, open, share, edit, remeasure, help)
 * are handled by the caller; they only close an open sheet.
 */
fun HomeState.reduce(action: HomeAction): HomeState = when (action) {
    HomeAction.OpenProfileSheet -> copy(overlay = HomeOverlay.ProfileSheet)
    is HomeAction.OpenSavedMenu -> copy(overlay = HomeOverlay.SavedMenu(action.savedId))
    HomeAction.DismissOverlay -> copy(overlay = null)
    is HomeAction.SelectProfile ->
        if (profiles.any { it.id == action.profileId }) copy(currentProfileId = action.profileId, overlay = null) else this
    is HomeAction.DeleteSaved -> copy(saved = saved.filterNot { it.id == action.savedId }, overlay = null)
    is HomeAction.EditProfile, HomeAction.NewProfile, HomeAction.Help, is HomeAction.StartMeasure,
    is HomeAction.ContinueIncomplete, is HomeAction.OpenSaved, is HomeAction.ShareSaved, is HomeAction.EditSaved, is HomeAction.RemeasureSaved,
    -> copy(overlay = null)
}

/** Adds [item] on top, replacing the profile's earlier unfinished measurement of the same garment (7.8 "পরে করব"). */
fun HomeState.withIncomplete(item: IncompleteMeasurement): HomeState =
    copy(incomplete = listOf(item) + incomplete.filterNot { it.profileId == item.profileId && it.garment == item.garment })

/** Removes the profile's unfinished measurement of [garment] once it is finished. */
fun HomeState.withoutIncomplete(profileId: Long, garment: Garment): HomeState =
    copy(incomplete = incomplete.filterNot { it.profileId == profileId && it.garment == garment })
