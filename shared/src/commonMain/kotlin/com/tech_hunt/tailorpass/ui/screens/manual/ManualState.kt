package com.tech_hunt.tailorpass.ui.screens.manual

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.GarmentMeasurements
import com.tech_hunt.tailorpass.model.LengthPreset
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

/** Tape instructions for one measurement: how-to text, "avoid mistakes" tips, diagram, tip card (7.6). Keys only. */
data class TapeGuide(
    val howtoKey: String? = null,
    val tipKeys: List<String> = emptyList(),
    val diagram: TapeDiagram? = null,
    val noteKey: String? = null,
)

/** One picture card of a preference measurement: neck depth (7.10) or length (7.6). The screen draws the picture. */
sealed interface PresetOption {
    val labelKey: String
    val inches: Float

    data class Neck(val depth: NeckDepthPreset) : PresetOption {
        override val labelKey: String get() = depth.labelKey
        override val inches: Float get() = depth.sampleInches
    }

    data class Length(val length: LengthPreset) : PresetOption {
        override val labelKey: String get() = length.labelKey
        override val inches: Float get() = length.sampleInches
    }
}

/** A list on the summary (7.7): optional section title key and the step indices it shows. */
data class SummaryGroup(val titleKey: String?, val indices: List<Int>)

object ManualPlan {
    /**
     * Tape order per garment (top of the body to bottom). Salwar kameez has no separate tape order in
     * Measurements.kt; its list order matches 7.10 (front neck depth = 8/14). Genji is size-only (no steps).
     */
    fun sections(garment: Garment): List<MeasurementSection> = when (garment) {
        Garment.Panjabi -> GarmentMeasurements.panjabiManualOrder
        Garment.SalwarKameez -> GarmentMeasurements.salwarKameez
        Garment.Shirt -> GarmentMeasurements.shirtManualOrder
        Garment.Pants -> GarmentMeasurements.pantsManualOrder
        Garment.Pajama -> GarmentMeasurements.pajamaManualOrder
        Garment.TShirt -> emptyList()
    }

    /** Only chest, belly, sleeve and neck depth have instructions/diagrams in the design resources. */
    fun guide(key: MeasurementKey): TapeGuide = when (key) {
        MeasurementKey.Chest -> TapeGuide("howto_chest", diagram = TapeDiagram.Chest)
        MeasurementKey.WaistBelly -> TapeGuide("howto_waist", diagram = TapeDiagram.Waist)
        MeasurementKey.Sleeve ->
            TapeGuide("howto_sleeve", listOf("tip_sleeve_bend", "tip_sleeve_start"), TapeDiagram.Sleeve)
        MeasurementKey.NeckDepthFront, MeasurementKey.NeckDepthBack ->
            TapeGuide("neck_depth_question", diagram = TapeDiagram.NeckDepth)
        MeasurementKey.Length -> TapeGuide("length_question", noteKey = "length_tip_old_garment")
        MeasurementKey.ShirtLength -> TapeGuide(noteKey = "shirt_length_tip")
        else -> TapeGuide()
    }

    /**
     * Picture cards of a preference measurement (7.10 neck depth, 7.6 length). Length values are the 5'7" samples of
     * Measurements.kt; computing them from the profile's height comes with the data (Step 10).
     */
    fun presets(key: MeasurementKey): List<PresetOption> = when (key) {
        MeasurementKey.NeckDepthFront, MeasurementKey.NeckDepthBack -> NeckDepthPreset.entries.map(PresetOption::Neck)
        MeasurementKey.Length -> LengthPreset.entries.map(PresetOption::Length)
        else -> emptyList()
    }

    /**
     * The suggested default of a preference measurement (Overview §3), preselected while the step has no value:
     * the middle card, as the 7.6 / 7.10 designs show. Measurements.kt marks no default, so it is fixed here.
     */
    fun suggestedPreset(key: MeasurementKey): PresetOption? = when (key) {
        MeasurementKey.NeckDepthFront, MeasurementKey.NeckDepthBack -> PresetOption.Neck(NeckDepthPreset.Medium)
        MeasurementKey.Length -> PresetOption.Length(LengthPreset.AtKnee)
        else -> null
    }
}

enum class StepStatus { Done, Current, Pending }

/**
 * Tape-measure flow (7.1–7.10). [values] are inches by step index (salwar kameez has waist and hip in both
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
    /** 7.8 "stop measuring?" dialog over the entry screen. */
    val exitOpen: Boolean = false,
    /** The current step was opened from the summary: saving or skipping it returns there instead of moving on. */
    val reviewing: Boolean = false,
    /** Set when the last measurement was entered: the caller goes on to the summary. */
    val finished: Boolean = false,
) {
    val sections: List<MeasurementSection> get() = ManualPlan.sections(target.garment)
    val steps: List<MeasurementKey> get() = sections.flatMap { it.keys }
    val count: Int get() = steps.size
    val currentKey: MeasurementKey? get() = steps.getOrNull(current)
    val isLast: Boolean get() = count > 0 && current == count - 1

    /** Measurements taken so far (7.8 "যে ৪টি মাপ নিয়েছেন", 7.9 "৪/৮"). */
    val doneCount: Int get() = values.size

    /** Steps without a value — skipped or not reached yet (7.7 orange rows). */
    val missing: List<Int> get() = steps.indices.filterNot { it in values }

    /**
     * 7.7 lists: a garment of one part (panjabi) is one list, although its tape order has body and preference
     * sections; salwar kameez keeps its kameez / salwar sections (waist and hip are in both).
     */
    val summaryGroups: List<SummaryGroup>
        get() = when (target.garment) {
            Garment.SalwarKameez -> sections.mapIndexed { i, section ->
                SummaryGroup(section.titleKey, List(section.keys.size) { sectionStart(i) + it })
            }
            else -> listOf(SummaryGroup(null, steps.indices.toList()))
        }

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
    val presets: List<PresetOption> get() = currentKey?.let(ManualPlan::presets).orEmpty()
    val selectedPreset: PresetOption? get() = inputValue?.let { v -> presets.firstOrNull { it.inches == v } }

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

        /** 7.6: length 8/8, at the knee (42"). Hip was skipped; the rest from the panjabi sample (plan.md §6). */
        val SampleLength = ManualState(
            MeasureTarget.SampleRahat,
            values = mapOf(0 to 15.5f, 1 to 18f, 2 to 40f, 3 to 36f, 5 to 24.5f, 6 to 10f),
            skipped = setOf(4),
            current = 7,
            input = "42",
        )

        /** 7.7: every measurement but hip, after "শেষ করুন" on 7.6. */
        val SampleSummary = SampleLength.copy(values = SampleLength.values + (7 to 42f), finished = true)

        /** 7.8: back on chest (3/8) with 4 measurements taken — neck, shoulder, belly and hip. */
        val SampleExit = ManualState(
            MeasureTarget.SampleRahat,
            values = mapOf(0 to 15.5f, 1 to 18f, 3 to 36f, 4 to 41f),
            current = 2,
            input = "40",
            exitOpen = true,
        )
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

// ---------- 7.3–7.6 / 7.8 / 7.10 Entry ----------

sealed interface ManualEntryAction {
    data object Back : ManualEntryAction
    data object OpenList : ManualEntryAction
    data class InputChanged(val text: String) : ManualEntryAction
    data object Decrease : ManualEntryAction
    data object Increase : ManualEntryAction
    data class PickPreset(val preset: PresetOption) : ManualEntryAction
    data object ToggleTips : ManualEntryAction
    data object Later : ManualEntryAction
    data object Next : ManualEntryAction
    data object Remeasure : ManualEntryAction
    data object ConfirmValue : ManualEntryAction

    /** 7.8: close the dialog and stay. */
    data object KeepGoing : ManualEntryAction

    /** 7.8 "পরে করব": the caller keeps the session for Home (7.9) and leaves the flow. */
    data object StopForLater : ManualEntryAction
}

// ---------- 7.7 Summary ----------

sealed interface ManualSummaryAction {
    data object Back : ManualSummaryAction

    /** Row tap or "এখন মাপুন": measure step [index] again, then come back here. */
    data class Edit(val index: Int) : ManualSummaryAction
    data object Verify : ManualSummaryAction
    data object ContinueWithoutMissing : ManualSummaryAction
}

/** Summary reducer: Edit opens the step in review mode; the other actions are navigation for the caller. */
fun ManualState.reduce(action: ManualSummaryAction): ManualState = when (action) {
    is ManualSummaryAction.Edit -> if (action.index in steps.indices) goTo(action.index).copy(reviewing = true) else this
    ManualSummaryAction.Back, ManualSummaryAction.Verify, ManualSummaryAction.ContinueWithoutMissing -> this
}

/**
 * Opens step [index] with its saved value in the field; without one, a preference measurement starts at its
 * suggested preset (not saved until "next"), a body measurement empty.
 */
fun ManualState.goTo(index: Int): ManualState =
    if (index !in steps.indices) this
    else copy(
        current = index,
        input = (values[index] ?: ManualPlan.suggestedPreset(steps[index])?.inches)?.let(::inchesToInput) ?: "",
        tipsOpen = false,
        warningOpen = false,
        exitOpen = false,
        reviewing = false,
        finished = false,
    )

/**
 * Pure reducer for the entry screens. Back opens the 7.8 dialog once something was measured (with nothing to keep,
 * the caller just goes back to the list); OpenList and StopForLater are navigation for the caller.
 */
fun ManualState.reduce(action: ManualEntryAction): ManualState = when (action) {
    is ManualEntryAction.InputChanged -> sanitizeInchInput(action.text)?.let { copy(input = it) } ?: this
    ManualEntryAction.Decrease -> inputValue?.let { copy(input = inchesToInput((it - STEP_INCHES).coerceAtLeast(STEP_INCHES))) } ?: this
    ManualEntryAction.Increase -> {
        val start = inputValue ?: currentKey?.minInches?.minus(STEP_INCHES)
        start?.let { copy(input = inchesToInput(it + STEP_INCHES)) } ?: this
    }
    is ManualEntryAction.PickPreset -> copy(input = inchesToInput(action.preset.inches))
    ManualEntryAction.ToggleTips -> copy(tipsOpen = !tipsOpen)
    // Skip for now: a value already entered is kept; the next measurement becomes current.
    ManualEntryAction.Later -> {
        val marked = if (current in values) this else copy(skipped = skipped + current)
        if (reviewing) marked.copy(finished = true) else marked.goTo((current + 1).coerceAtMost(count - 1))
    }
    ManualEntryAction.Next -> inputValue?.let { if (isOutlier) copy(warningOpen = true) else commit(it) } ?: this
    ManualEntryAction.Remeasure -> copy(warningOpen = false)
    ManualEntryAction.ConfirmValue -> inputValue?.let { copy(warningOpen = false).commit(it) } ?: copy(warningOpen = false)
    ManualEntryAction.Back -> if (values.isNotEmpty()) copy(exitOpen = true) else this
    ManualEntryAction.KeepGoing, ManualEntryAction.StopForLater -> copy(exitOpen = false)
    ManualEntryAction.OpenList -> this
}

/**
 * Saves [value] for the current step and moves on; after the last step (or a step opened from the summary) the flow
 * is [ManualState.finished].
 */
private fun ManualState.commit(value: Float): ManualState {
    val saved = copy(values = values + (current to value), skipped = skipped - current)
    return if (current >= count - 1 || reviewing) saved.copy(finished = true) else saved.goTo(current + 1)
}
