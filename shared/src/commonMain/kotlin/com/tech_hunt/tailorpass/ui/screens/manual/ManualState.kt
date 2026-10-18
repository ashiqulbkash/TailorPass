package com.tech_hunt.tailorpass.ui.screens.manual

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.GarmentMeasurements
import com.tech_hunt.tailorpass.model.MeasurementKey
import com.tech_hunt.tailorpass.model.MeasurementSection
import com.tech_hunt.tailorpass.model.NeckDepthPreset
import com.tech_hunt.tailorpass.ui.diagram.TapeDiagram
import com.tech_hunt.tailorpass.ui.screens.prep.MeasureTarget
import com.tech_hunt.tailorpass.util.STEP_INCHES
import com.tech_hunt.tailorpass.util.inchesToInput
import com.tech_hunt.tailorpass.util.parseInches
import com.tech_hunt.tailorpass.util.sanitizeInchInput

/** Unit chosen on 7.1. Entry screens (7.3–7.10) are designed in inches only. */
enum class MeasureUnit { Inch, Cm }

/** Tape instructions for one measurement: how-to text, "avoid mistakes" tips, diagram. Keys only. */
data class TapeGuide(
    val howtoKey: String? = null,
    val tipKeys: List<String> = emptyList(),
    val diagram: TapeDiagram? = null,
)

object ManualPlan {
    /**
     * Tape order per garment (top of the body to bottom). Salwar kameez has no separate tape order in
     * Measurements.kt; its list order matches 7.10 (front neck depth = 8/14). Shirt/Pants/Pajama lists are
     * not defined yet (plan.md §3), so they have no steps.
     */
    fun sections(garment: Garment): List<MeasurementSection> = when (garment) {
        Garment.Panjabi -> GarmentMeasurements.panjabiManualOrder
        Garment.SalwarKameez -> GarmentMeasurements.salwarKameez
        Garment.Shirt, Garment.Pants, Garment.Pajama, Garment.TShirt -> emptyList()
    }

    /** Only chest, belly, sleeve and neck depth have instructions/diagrams in the design resources. */
    fun guide(key: MeasurementKey): TapeGuide = when (key) {
        MeasurementKey.Chest -> TapeGuide("howto_chest", diagram = TapeDiagram.Chest)
        MeasurementKey.WaistBelly -> TapeGuide("howto_waist", diagram = TapeDiagram.Waist)
        MeasurementKey.Sleeve ->
            TapeGuide("howto_sleeve", listOf("tip_sleeve_bend", "tip_sleeve_start"), TapeDiagram.Sleeve)
        MeasurementKey.NeckDepthFront, MeasurementKey.NeckDepthBack ->
            TapeGuide("neck_depth_question", diagram = TapeDiagram.NeckDepth)
        MeasurementKey.Length -> TapeGuide("length_question")
        else -> TapeGuide()
    }

    /** Picture cards of a preference measurement (7.10). Length cards (7.6) come in Step 6. */
    fun presets(key: MeasurementKey): List<NeckDepthPreset> = when (key) {
        MeasurementKey.NeckDepthFront, MeasurementKey.NeckDepthBack -> NeckDepthPreset.entries
        else -> emptyList()
    }
}

enum class StepStatus { Done, Current, Pending }

/**
 * Tape-measure flow (7.1–7.5, 7.10). [values] are inches by step index (salwar kameez has waist and hip in both
 * sections). [input] holds Latin digits as typed ("24.5"); the screen shows "২৪½". Design phase: one state for the
 * whole flow; a ViewModel replaces it in Step 10.
 */
data class ManualState(
    val target: MeasureTarget,
    val unit: MeasureUnit = MeasureUnit.Inch,
    val values: Map<Int, Float> = emptyMap(),
    val skipped: Set<Int> = emptySet(),
    val current: Int = 0,
    val input: String = "",
    val tipsOpen: Boolean = false,
    val warningOpen: Boolean = false,
    /** Set when the last measurement was entered: the caller goes on to the summary. */
    val finished: Boolean = false,
) {
    val sections: List<MeasurementSection> get() = ManualPlan.sections(target.garment)
    val steps: List<MeasurementKey> get() = sections.flatMap { it.keys }
    val count: Int get() = steps.size
    val currentKey: MeasurementKey? get() = steps.getOrNull(current)

    /** Rough time for the list subtitle: about 40 seconds per measurement (8 → 5 minutes). */
    val minutes: Int get() = (count * 5 + 7) / 8

    fun status(index: Int): StepStatus = when {
        index == current -> StepStatus.Current
        index in values -> StepStatus.Done
        else -> StepStatus.Pending
    }

    /** Index of the first step of [section] in [steps]. */
    fun sectionStart(section: Int): Int = sections.take(section).sumOf { it.keys.size }

    val inputValue: Float? get() = parseInches(input)
    val canGoNext: Boolean get() = inputValue != null

    /** The entered value is outside the key's plausible range (7.5 warning). */
    val isOutlier: Boolean
        get() {
            val key = currentKey ?: return false
            val v = inputValue ?: return false
            return !key.isPlausible(v)
        }

    val guide: TapeGuide get() = currentKey?.let(ManualPlan::guide) ?: TapeGuide()
    val presets: List<NeckDepthPreset> get() = currentKey?.let(ManualPlan::presets).orEmpty()
    val selectedPreset: NeckDepthPreset? get() = inputValue?.let { v -> presets.firstOrNull { it.sampleInches == v } }

    companion object {
        /** 7.1: Rahat's panjabi, inches. */
        val SamplePrep = ManualState(MeasureTarget.SampleRahat)

        /** 7.2: neck and shoulder done, chest now. */
        val SampleList = ManualState(MeasureTarget.SampleRahat, values = mapOf(0 to 15.5f, 1 to 18f), current = 2)

        /** 7.3: chest, 40". */
        val SampleChest = SampleList.copy(input = "40")

        /** 7.4: sleeve 6/8, 24½", tips open. Earlier values from the panjabi sample (plan.md §6). */
        val SampleSleeve = ManualState(
            MeasureTarget.SampleRahat,
            values = mapOf(0 to 15.5f, 1 to 18f, 2 to 40f, 3 to 36f, 4 to 41f),
            current = 5,
            input = "24.5",
            tipsOpen = true,
        )

        /** 7.5: chest 14" → outlier warning. */
        val SampleWarning = SampleList.copy(input = "14", warningOpen = true)

        /** 7.10: Farhana's salwar kameez, front neck depth 8/14, medium (7"). */
        val SampleNeckDepth = ManualState(MeasureTarget.SampleFarhana, current = 7, input = "7")
    }
}

// ---------- 7.1 Preparation ----------

sealed interface ManualPrepAction {
    data object Back : ManualPrepAction
    data class SetUnit(val unit: MeasureUnit) : ManualPrepAction
    data object Start : ManualPrepAction
}

// ---------- 7.2 Measurement list ----------

sealed interface ManualListAction {
    data object Back : ManualListAction
    data class Open(val index: Int) : ManualListAction
    data object Continue : ManualListAction
}

// ---------- 7.3 / 7.4 / 7.5 / 7.10 Entry ----------

sealed interface ManualEntryAction {
    data object Back : ManualEntryAction
    data object OpenList : ManualEntryAction
    data class InputChanged(val text: String) : ManualEntryAction
    data object Decrease : ManualEntryAction
    data object Increase : ManualEntryAction
    data class PickPreset(val preset: NeckDepthPreset) : ManualEntryAction
    data object ToggleTips : ManualEntryAction
    data object Later : ManualEntryAction
    data object Next : ManualEntryAction
    data object Remeasure : ManualEntryAction
    data object ConfirmValue : ManualEntryAction
}

/** Opens step [index] with its saved value (if any) in the field. */
fun ManualState.goTo(index: Int): ManualState =
    if (index !in steps.indices) this
    else copy(
        current = index,
        input = values[index]?.let(::inchesToInput) ?: "",
        tipsOpen = false,
        warningOpen = false,
        finished = false,
    )

/** Pure reducer for the entry screens. Back/OpenList are handled by the caller (navigation). */
fun ManualState.reduce(action: ManualEntryAction): ManualState = when (action) {
    is ManualEntryAction.InputChanged -> sanitizeInchInput(action.text)?.let { copy(input = it) } ?: this
    ManualEntryAction.Decrease -> inputValue?.let { copy(input = inchesToInput((it - STEP_INCHES).coerceAtLeast(STEP_INCHES))) } ?: this
    ManualEntryAction.Increase -> {
        val start = inputValue ?: currentKey?.minInches?.minus(STEP_INCHES)
        start?.let { copy(input = inchesToInput(it + STEP_INCHES)) } ?: this
    }
    is ManualEntryAction.PickPreset -> copy(input = inchesToInput(action.preset.sampleInches))
    ManualEntryAction.ToggleTips -> copy(tipsOpen = !tipsOpen)
    // Skip for now: a value already entered is kept; the next measurement becomes current.
    ManualEntryAction.Later -> {
        val marked = if (current in values) this else copy(skipped = skipped + current)
        marked.goTo((current + 1).coerceAtMost(count - 1))
    }
    ManualEntryAction.Next -> inputValue?.let { if (isOutlier) copy(warningOpen = true) else commit(it) } ?: this
    ManualEntryAction.Remeasure -> copy(warningOpen = false)
    ManualEntryAction.ConfirmValue -> inputValue?.let { copy(warningOpen = false).commit(it) } ?: copy(warningOpen = false)
    ManualEntryAction.Back, ManualEntryAction.OpenList -> this
}

/** Saves [value] for the current step and moves on; after the last step the flow is [ManualState.finished]. */
private fun ManualState.commit(value: Float): ManualState {
    val saved = copy(values = values + (current to value), skipped = skipped - current)
    return if (current >= count - 1) saved.copy(finished = true) else saved.goTo(current + 1)
}
