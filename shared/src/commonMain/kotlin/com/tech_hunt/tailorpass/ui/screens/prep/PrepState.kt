package com.tech_hunt.tailorpass.ui.screens.prep

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.Gender

/**
 * Who and what is being measured (3.1–4.4). The name is user data, not a resource key.
 * female: female checklist/pose (4.2, 4.4) — by profile gender, or a women's garment.
 */
data class MeasureTarget(val garment: Garment, val gender: Gender, val profileName: String) {
    val female: Boolean get() = gender == Gender.Female || garment.forWomen

    companion object {
        /** Design samples: Rahat's panjabi (3.1, 4.1, 4.3), Farhana's salwar kameez (4.2, 4.4). */
        val SampleRahat = MeasureTarget(Garment.Panjabi, Gender.Male, "রাহাত")
        val SampleFarhana = MeasureTarget(Garment.SalwarKameez, Gender.Female, "ফারহানা")
    }
}

/** One checklist line: drawable name + title/subtitle string keys. */
data class PrepItem(val iconName: String, val titleKey: String, val subtitleKey: String)

/** Garment-specific tip card (4.1/4.2). */
data class PrepTip(val iconName: String, val titleKey: String, val bodyKey: String)

object PrepGuide {
    private val common = listOf(
        PrepItem("ic_sun", "check_light_title", "check_light_sub"),
        PrepItem("ic_wall", "check_bg_title", "check_bg_sub"),
        PrepItem("ic_phone", "check_phone_title", "check_phone_sub"),
        PrepItem("ic_person_stand", "check_stand_title", "check_stand_sub"),
    )
    private val maleChecklist = listOf(PrepItem("ic_garment_tshirt", "check_tight_title", "check_tight_sub")) + common
    private val femaleChecklist =
        listOf(PrepItem("ic_garment_salwar_kameez", "check_tight_female_title", "check_tight_female_sub")) + common

    fun checklist(female: Boolean): List<PrepItem> = if (female) femaleChecklist else maleChecklist

    /** Only panjabi and salwar kameez have a tip in the design; other garments show none. */
    fun tip(garment: Garment): PrepTip? = when (garment) {
        Garment.Panjabi -> PrepTip("ic_garment_panjabi", "tip_panjabi_title", "tip_panjabi_body")
        Garment.SalwarKameez -> PrepTip("ic_garment_salwar_kameez", "tip_sk_title", "tip_sk_body")
        Garment.Shirt, Garment.Pants, Garment.Pajama, Garment.TShirt -> null
    }

    /** Pose guide drawables (4.3/4.4): front, side, distance diagram. */
    data class PoseDrawables(val front: String, val side: String, val distance: String)

    fun poses(female: Boolean): PoseDrawables =
        if (female) PoseDrawables("pose_female_front", "pose_female_side", "pose_distance_female")
        else PoseDrawables("pose_male_front", "pose_male_side", "pose_distance_male")

    /** Pose tips under the diagram. The female design replaces the voice tip with the hair tip. */
    fun poseTips(female: Boolean): List<PoseTip> = listOf(
        PoseTip("ic_person_stand", "pose_tip_arms"),
        if (female) PoseTip("ic_hair", "pose_tip_hair") else PoseTip("ic_voice", "pose_tip_voice"),
    )
}

/** One pose tip line: drawable name + string key. */
data class PoseTip(val iconName: String, val textKey: String)

// ---------- 3.1 Method ----------

sealed interface MethodAction {
    data object Back : MethodAction
    data object ChooseAi : MethodAction
    data object ChooseTape : MethodAction
}

// ---------- 3.2 Camera permission ----------

sealed interface CameraPermissionAction {
    data object Back : CameraPermissionAction
    data object Allow : CameraPermissionAction
    data object UseTape : CameraPermissionAction
}

// ---------- 4.1 / 4.2 Checklist ----------

/** [checked] = indexes of ticked items in [items]. Continue is enabled only when every item is ticked. */
data class ChecklistState(val target: MeasureTarget, val checked: Set<Int> = emptySet()) {
    val items: List<PrepItem> get() = PrepGuide.checklist(target.female)
    val tip: PrepTip? get() = PrepGuide.tip(target.garment)
    val checkedCount: Int get() = checked.count { it in items.indices }
    val canContinue: Boolean get() = checkedCount == items.size

    companion object {
        /** 4.1: three of five ticked. */
        val Sample = ChecklistState(MeasureTarget.SampleRahat, checked = setOf(0, 1, 2))

        /** 4.2: two of five ticked. */
        val SampleFemale = ChecklistState(MeasureTarget.SampleFarhana, checked = setOf(0, 1))
    }
}

sealed interface ChecklistAction {
    data class Toggle(val index: Int) : ChecklistAction
    data object Continue : ChecklistAction
    data object Back : ChecklistAction
}

/** Pure reducer. Continue/Back are handled by the caller (navigation). */
fun ChecklistState.reduce(action: ChecklistAction): ChecklistState = when (action) {
    is ChecklistAction.Toggle -> when {
        action.index !in items.indices -> this
        action.index in checked -> copy(checked = checked - action.index)
        else -> copy(checked = checked + action.index)
    }
    ChecklistAction.Continue, ChecklistAction.Back -> this
}

// ---------- 4.3 / 4.4 Pose guide ----------

sealed interface PoseGuideAction {
    data object Back : PoseGuideAction
    data object OpenCamera : PoseGuideAction
}
