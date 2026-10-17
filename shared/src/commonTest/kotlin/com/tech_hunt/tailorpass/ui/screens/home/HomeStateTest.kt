package com.tech_hunt.tailorpass.ui.screens.home

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.ui.util.CalendarDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HomeStateTest {
    private val sample = HomeState.Sample

    @Test
    fun sampleMatchesTheDesign() {
        assertEquals("রাহাত", sample.currentProfile.name)
        assertEquals(listOf(Garment.Panjabi, Garment.Shirt, Garment.Pants), sample.currentSaved.map { it.garment })
        assertEquals(3, sample.savedCount(HomeState.RAHAT))
        assertEquals(2, sample.savedCount(HomeState.FARHANA))
        assertEquals(0, sample.savedCount(HomeState.ABBU))
        assertTrue(HomeState.SampleEmpty.currentSaved.isEmpty())
    }

    @Test
    fun profileSheetOpensAndSelectingAProfileClosesIt() {
        val open = sample.reduce(HomeAction.OpenProfileSheet)
        assertEquals(HomeOverlay.ProfileSheet, open.overlay)
        val abbu = open.reduce(HomeAction.SelectProfile(HomeState.ABBU))
        assertEquals(HomeState.ABBU, abbu.currentProfileId)
        assertNull(abbu.overlay)
        assertTrue(abbu.currentSaved.isEmpty())
    }

    @Test
    fun unknownProfileIsIgnored() {
        val open = sample.reduce(HomeAction.OpenProfileSheet)
        assertEquals(open, open.reduce(HomeAction.SelectProfile(99)))
    }

    @Test
    fun savedMenuShowsTheLongPressedItem() {
        val menu = sample.reduce(HomeAction.OpenSavedMenu(2))
        assertEquals(Garment.Shirt, menu.menuItem?.garment)
        assertNull(menu.reduce(HomeAction.DismissOverlay).menuItem)
    }

    @Test
    fun deleteRemovesOnlyThatItemAndClosesTheMenu() {
        val after = sample.reduce(HomeAction.OpenSavedMenu(1)).reduce(HomeAction.DeleteSaved(1))
        assertEquals(listOf(Garment.Shirt, Garment.Pants), after.currentSaved.map { it.garment })
        assertEquals(2, after.savedCount(HomeState.FARHANA))
        assertNull(after.overlay)
    }

    @Test
    fun navigationActionsOnlyCloseTheSheet() {
        val menu = sample.reduce(HomeAction.OpenSavedMenu(1))
        listOf(
            HomeAction.OpenSaved(1), HomeAction.EditSaved(1), HomeAction.RemeasureSaved(1), HomeAction.ShareSaved(1),
            HomeAction.StartMeasure(Garment.Panjabi), HomeAction.NewProfile, HomeAction.EditProfile(HomeState.RAHAT),
            HomeAction.Help,
        ).forEach { action ->
            assertEquals(sample, menu.reduce(action), action.toString())
        }
    }

    @Test
    fun calendarDateRejectsImpossibleValues() {
        assertFailsWith<IllegalArgumentException> { CalendarDate(2026, 13, 1) }
        assertFailsWith<IllegalArgumentException> { CalendarDate(2026, 0, 1) }
        assertFailsWith<IllegalArgumentException> { CalendarDate(2026, 1, 32) }
    }
}
