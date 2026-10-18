package com.tech_hunt.tailorpass.ui.screens.verify

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.GarmentMeasurements
import com.tech_hunt.tailorpass.model.MeasureMethod
import com.tech_hunt.tailorpass.model.MeasurementKey
import com.tech_hunt.tailorpass.model.MeasurementSection
import com.tech_hunt.tailorpass.model.SourceBadge
import com.tech_hunt.tailorpass.ui.screens.manual.ManualState
import com.tech_hunt.tailorpass.ui.screens.prep.MeasureTarget
import com.tech_hunt.tailorpass.util.STEP_INCHES
import com.tech_hunt.tailorpass.util.inchesToInput
import com.tech_hunt.tailorpass.util.parseInches
import com.tech_hunt.tailorpass.util.sanitizeInchInput

/**
 * One measurement to check: [inches] null = skipped on the tape, the tailor measures it. [aiInches] is the AI's own
 * value, shown on the edit sheet ("AI: ৩৬"") — null for tape and preference values.
 */
data class VerifyRow(
    val key: MeasurementKey,
    val inches: Float?,
    val badge: SourceBadge,
    val aiInches: Float? = null,
)

/** A list card: optional title key (kameez / salwar) and its rows. */
data class VerifySection(val titleKey: String?, val rows: List<VerifyRow>) {
    /** Drawable name of the section header icon (8.4). */
    val iconName: String? get() = titleKey?.let(SectionIcons::get)
}

private val SectionIcons = mapOf("garment_kameez" to "ic_garment_salwar_kameez", "garment_salwar" to "ic_garment_pajama")

object VerifyPlan {
    /** Order on verify / result / PDF (Measurements.kt). Shirt, Pants and Pajama lists are not defined yet (plan.md §3). */
    fun sections(garment: Garment): List<MeasurementSection> = when (garment) {
        Garment.Panjabi -> GarmentMeasurements.panjabi
        Garment.SalwarKameez -> GarmentMeasurements.salwarKameez
        Garment.Shirt, Garment.Pants, Garment.Pajama, Garment.TShirt -> emptyList()
    }

    /** A value the user typed on the edit sheet: a preference stays "your choice", a body value counts as tape-measured. */
    fun editedBadge(key: MeasurementKey): SourceBadge =
        if (key.isPreference) SourceBadge.YourChoice else SourceBadge.TapeMeasured
}

/**
 * Verify measurements (8.1–8.5) with the edit sheet (8.3) as an overlay. [editing] is the index of the edited row in
 * [rows] (salwar kameez has waist and hip in both sections); [editInput] holds Latin digits as typed ("36.5").
 * Design phase: AI values are the design samples; a ViewModel replaces this in Step 10.
 */
data class VerifyState(
    val target: MeasureTarget,
    val method: MeasureMethod,
    val sections: List<VerifySection>,
    val note: String = "",
    val editing: Int? = null,
    val editInput: String = "",
) {
    val rows: List<VerifyRow> get() = sections.flatMap { it.rows }
    val count: Int get() = rows.size

    /** More than one part (salwar kameez): titled sections, a scrolling list and the count in the subtitle (8.4). */
    val sectioned: Boolean get() = sections.size > 1

    /** After photos, "your choice" values are explained: the AI does not measure them (8.4). */
    val showPreferenceHint: Boolean
        get() = method == MeasureMethod.Ai && rows.any { it.badge == SourceBadge.YourChoice }

    /** Placeholder of the tailor note: the design gives salwar kameez its own example (8.4). */
    val noteHintKey: String
        get() = if (target.garment == Garment.SalwarKameez) "tailor_note_hint_sk" else "tailor_note_hint"

    val editingRow: VerifyRow? get() = editing?.let(rows::getOrNull)
    val editValue: Float? get() = parseInches(editInput)
    val canSave: Boolean get() = editValue != null

    /** The edit sheet's value differs from the AI's ("আপনি বদলেছেন"). */
    val editChanged: Boolean
        get() {
            val ai = editingRow?.aiInches ?: return false
            val value = editValue ?: return false
            return value != ai
        }

    /** Index of the first row of [section] in [rows]. */
    fun sectionStart(section: Int): Int = sections.take(section).sumOf { it.rows.size }

    companion object {
        /** 8.1: Rahat's panjabi from photos (plan.md §3 sample values, the design's badges). */
        val SamplePanjabi = VerifyState(
            MeasureTarget.SampleRahat,
            MeasureMethod.Ai,
            listOf(
                VerifySection(
                    null,
                    listOf(
                        ai(MeasurementKey.Length, 42f, SourceBadge.AccuracyHigh),
                        ai(MeasurementKey.Chest, 40f, SourceBadge.AccuracyHigh),
                        ai(MeasurementKey.WaistBelly, 36f, SourceBadge.AccuracyMedium),
                        ai(MeasurementKey.Hip, 41f, SourceBadge.AccuracyMedium),
                        ai(MeasurementKey.Shoulder, 18f, SourceBadge.AccuracyHigh),
                        ai(MeasurementKey.Sleeve, 24.5f, SourceBadge.AccuracyMedium),
                        ai(MeasurementKey.Cuff, 10f, SourceBadge.AccuracyMedium),
                        ai(MeasurementKey.Neck, 15.5f, SourceBadge.AccuracyLow),
                    ),
                ),
            ),
        )

        /** 8.3: editing belly, AI 36" changed to 36½". */
        val SampleEdit = SamplePanjabi.copy(editing = 2, editInput = "36.5")

        /** 8.4: Farhana's salwar kameez from photos, 10 + 4 measurements, preferences as "your choice". */
        val SampleSalwarKameez = VerifyState(
            MeasureTarget.SampleFarhana,
            MeasureMethod.Ai,
            listOf(
                VerifySection(
                    "garment_kameez",
                    listOf(
                        ai(MeasurementKey.Bust, 36f, SourceBadge.AccuracyHigh),
                        ai(MeasurementKey.Waist, 30f, SourceBadge.AccuracyMedium),
                        ai(MeasurementKey.Hip, 38f, SourceBadge.AccuracyMedium),
                        ai(MeasurementKey.Shoulder, 14f, SourceBadge.AccuracyHigh),
                        ai(MeasurementKey.Armhole, 16f, SourceBadge.AccuracyLow),
                        ai(MeasurementKey.Sleeve, 18f, SourceBadge.AccuracyHigh),
                        VerifyRow(MeasurementKey.Cuff, 9f, SourceBadge.YourChoice),
                        VerifyRow(MeasurementKey.NeckDepthFront, 7f, SourceBadge.YourChoice),
                        VerifyRow(MeasurementKey.NeckDepthBack, 5f, SourceBadge.YourChoice),
                        VerifyRow(MeasurementKey.Length, 40f, SourceBadge.YourChoice),
                    ),
                ),
                VerifySection(
                    "garment_salwar",
                    listOf(
                        ai(MeasurementKey.Waist, 30f, SourceBadge.AccuracyMedium),
                        ai(MeasurementKey.Hip, 38f, SourceBadge.AccuracyMedium),
                        ai(MeasurementKey.SalwarLength, 38f, SourceBadge.AccuracyHigh),
                        VerifyRow(MeasurementKey.Bottom, 12f, SourceBadge.YourChoice),
                    ),
                ),
            ),
        )

        /** 8.5: Rahat's panjabi after the tape summary (7.7) — hip skipped. */
        val SampleManual: VerifyState get() = fromTape(ManualState.SampleSummary)

        /** Design phase: the AI's result for [target] is the design sample of its garment (none for undefined lists). */
        fun sampleAi(target: MeasureTarget): VerifyState = when (target.garment) {
            Garment.Panjabi -> SamplePanjabi.copy(target = target)
            Garment.SalwarKameez -> SampleSalwarKameez.copy(target = target)
            Garment.Shirt, Garment.Pants, Garment.Pajama, Garment.TShirt -> VerifyState(target, MeasureMethod.Ai, emptyList())
        }

        /**
         * The tape session in verify order. The tape order can differ (panjabi), so each row takes the value of the
         * same key; a key listed twice (salwar kameez waist/hip) takes the matching occurrence in the tape steps.
         */
        fun fromTape(manual: ManualState): VerifyState {
            val seen = mutableMapOf<MeasurementKey, Int>()
            val sections = VerifyPlan.sections(manual.target.garment).map { section ->
                VerifySection(
                    section.titleKey,
                    section.keys.map { key ->
                        val occurrence = seen[key] ?: 0
                        seen[key] = occurrence + 1
                        val step = manual.steps.indices.filter { manual.steps[it] == key }.getOrNull(occurrence)
                        val value = step?.let { manual.values[it] }
                        val badge = when {
                            value == null -> SourceBadge.Missing
                            key.isPreference -> SourceBadge.YourChoice
                            else -> SourceBadge.TapeMeasured
                        }
                        VerifyRow(key, value, badge)
                    },
                )
            }
            return VerifyState(manual.target, MeasureMethod.Tape, sections)
        }

        private fun ai(key: MeasurementKey, inches: Float, badge: SourceBadge) = VerifyRow(key, inches, badge, aiInches = inches)
    }
}

sealed interface VerifyAction {
    data object Back : VerifyAction

    /** Row tap: open the edit sheet for row [index]. */
    data class Edit(val index: Int) : VerifyAction
    data class NoteChanged(val text: String) : VerifyAction
    data object ShowResult : VerifyAction

    // 8.3 Edit sheet
    data class EditInputChanged(val text: String) : VerifyAction
    data object EditDecrease : VerifyAction
    data object EditIncrease : VerifyAction
    data object SaveEdit : VerifyAction
    data object DismissEdit : VerifyAction
}

/** Pure reducer. Back and ShowResult are navigation for the caller. */
fun VerifyState.reduce(action: VerifyAction): VerifyState = when (action) {
    is VerifyAction.Edit -> rows.getOrNull(action.index)?.let { row ->
        copy(editing = action.index, editInput = row.inches?.let(::inchesToInput) ?: "")
    } ?: this
    is VerifyAction.NoteChanged -> copy(note = action.text)
    is VerifyAction.EditInputChanged -> sanitizeInchInput(action.text)?.let { copy(editInput = it) } ?: this
    VerifyAction.EditDecrease ->
        editValue?.let { copy(editInput = inchesToInput((it - STEP_INCHES).coerceAtLeast(STEP_INCHES))) } ?: this
    VerifyAction.EditIncrease -> {
        val start = editValue ?: editingRow?.key?.minInches?.minus(STEP_INCHES)
        start?.let { copy(editInput = inchesToInput(it + STEP_INCHES)) } ?: this
    }
    VerifyAction.SaveEdit -> {
        val index = editing
        val value = editValue
        if (index == null || value == null) this
        else updateRow(index) { row ->
            if (row.inches == value) row else row.copy(inches = value, badge = VerifyPlan.editedBadge(row.key))
        }.copy(editing = null, editInput = "")
    }
    VerifyAction.DismissEdit -> copy(editing = null, editInput = "")
    VerifyAction.Back, VerifyAction.ShowResult -> this
}

/** Replaces row [index] of [VerifyState.rows] inside its section. */
private fun VerifyState.updateRow(index: Int, transform: (VerifyRow) -> VerifyRow): VerifyState {
    var start = 0
    return copy(
        sections = sections.map { section ->
            val offset = index - start
            start += section.rows.size
            if (offset in section.rows.indices) {
                section.copy(rows = section.rows.mapIndexed { i, row -> if (i == offset) transform(row) else row })
            } else {
                section
            }
        },
    )
}
