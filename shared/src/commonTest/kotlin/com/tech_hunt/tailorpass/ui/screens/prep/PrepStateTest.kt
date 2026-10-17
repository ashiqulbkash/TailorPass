package com.tech_hunt.tailorpass.ui.screens.prep

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.Gender
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PrepStateTest {

    @Test
    fun samplesMatchTheDesign() {
        assertEquals(3, ChecklistState.Sample.checkedCount)
        assertEquals(2, ChecklistState.SampleFemale.checkedCount)
        assertEquals(5, ChecklistState.Sample.items.size)
        assertEquals(5, ChecklistState.SampleFemale.items.size)
        assertFalse(ChecklistState.Sample.canContinue)
        assertFalse(MeasureTarget.SampleRahat.female)
        assertTrue(MeasureTarget.SampleFarhana.female)
    }

    @Test
    fun continueIsEnabledOnlyWhenEveryItemIsTicked() {
        var state = ChecklistState(MeasureTarget.SampleRahat)
        assertEquals(0, state.checkedCount)
        state.items.indices.forEach {
            assertFalse(state.canContinue)
            state = state.reduce(ChecklistAction.Toggle(it))
        }
        assertTrue(state.canContinue)
        state = state.reduce(ChecklistAction.Toggle(2))
        assertFalse(state.canContinue)
        assertEquals(4, state.checkedCount)
    }

    @Test
    fun toggleTwiceUntogglesAndInvalidIndexIsIgnored() {
        val start = ChecklistState.Sample
        assertEquals(start, start.reduce(ChecklistAction.Toggle(4)).reduce(ChecklistAction.Toggle(4)))
        assertEquals(start, start.reduce(ChecklistAction.Toggle(5)))
        assertEquals(start, start.reduce(ChecklistAction.Toggle(-1)))
        assertEquals(start, start.reduce(ChecklistAction.Continue))
        assertEquals(start, start.reduce(ChecklistAction.Back))
    }

    @Test
    fun femaleChecklistOnlyChangesTheClothingItem() {
        val male = PrepGuide.checklist(female = false)
        val female = PrepGuide.checklist(female = true)
        assertEquals("check_tight_title", male.first().titleKey)
        assertEquals("check_tight_female_title", female.first().titleKey)
        assertEquals(male.drop(1), female.drop(1))
    }

    @Test
    fun femaleVariantByGenderOrWomensGarment() {
        assertTrue(MeasureTarget(Garment.Pants, Gender.Female, "x").female)
        assertTrue(MeasureTarget(Garment.SalwarKameez, Gender.Male, "x").female)
        assertFalse(MeasureTarget(Garment.Shirt, Gender.Male, "x").female)
    }

    @Test
    fun tipsOnlyForPanjabiAndSalwarKameez() {
        assertEquals("tip_panjabi_title", PrepGuide.tip(Garment.Panjabi)?.titleKey)
        assertEquals("tip_sk_title", PrepGuide.tip(Garment.SalwarKameez)?.titleKey)
        listOf(Garment.Shirt, Garment.Pants, Garment.Pajama, Garment.TShirt).forEach { assertNull(PrepGuide.tip(it), it.name) }
    }

    @Test
    fun poseGuideFollowsTheVariant() {
        assertEquals("pose_male_front", PrepGuide.poses(female = false).front)
        assertEquals("pose_distance_female", PrepGuide.poses(female = true).distance)
        assertEquals(listOf("pose_tip_arms", "pose_tip_voice"), PrepGuide.poseTips(female = false).map { it.textKey })
        assertEquals(listOf("pose_tip_arms", "pose_tip_hair"), PrepGuide.poseTips(female = true).map { it.textKey })
    }
}
