package com.tech_hunt.tailorpass.ui.screens.result

import com.tech_hunt.tailorpass.model.DiagramSpec
import com.tech_hunt.tailorpass.model.Diagrams
import com.tech_hunt.tailorpass.model.Fit
import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.MeasurementKey
import com.tech_hunt.tailorpass.model.SampleMenShirtChart
import com.tech_hunt.tailorpass.model.SizeRow
import com.tech_hunt.tailorpass.model.SourceBadge
import com.tech_hunt.tailorpass.model.suggestSize
import com.tech_hunt.tailorpass.ui.screens.manual.MeasureUnit
import com.tech_hunt.tailorpass.ui.screens.prep.MeasureTarget
import com.tech_hunt.tailorpass.ui.screens.verify.VerifyState
import com.tech_hunt.tailorpass.ui.util.CalendarDate

/** One numbered measurement on the result and the share files. [inches] null = skipped, the tailor measures it. */
data class ResultRow(val key: MeasurementKey, val inches: Float?, val badge: SourceBadge) {
    /** Chosen by the user, not measured: starred on the PDF ("* চিহ্নিত মাপ ব্যবহারকারীর পছন্দ"). */
    val isChoice: Boolean get() = badge == SourceBadge.YourChoice

    /** Low AI accuracy: the files ask the tailor to check it with a tape (10.1 neck, 10.2 armhole). */
    val checkWithTape: Boolean get() = badge == SourceBadge.AccuracyLow
}

/** A garment part (kameez / salwar) with its own diagram and numbering; [diagram] null when none is drawn yet. */
data class ResultSection(val titleKey: String?, val diagram: DiagramSpec?, val rows: List<ResultRow>)

/**
 * Result (9.1–9.3) and the share files built from it (10.1–10.3). [tab] is the kameez / salwar tab of 9.3,
 * [size] the suggested ready-made size (null = no chart for the garment). Design phase: built from the verified
 * measurement or the design samples; a ViewModel replaces this in Step 10.
 */
data class ResultState(
    val target: MeasureTarget,
    val date: CalendarDate,
    val fit: Fit,
    val sections: List<ResultSection>,
    val size: String?,
    val note: String = "",
    val unit: MeasureUnit = MeasureUnit.Inch,
    val tab: Int = 0,
) {
    /** More than one part (salwar kameez): tabs on 9.3, section rows and two diagrams on the files. */
    val sectioned: Boolean get() = sections.size > 1
    val currentSection: ResultSection? get() = sections.getOrNull(tab)
    val rows: List<ResultRow> get() = sections.flatMap { it.rows }
    val hasChoices: Boolean get() = rows.any { it.isChoice }

    /** Measurements the tailor should check with a tape, each once (salwar kameez lists waist and hip twice). */
    val checkKeys: List<MeasurementKey> get() = rows.filter { it.checkWithTape }.map { it.key }.distinct()

    val titleKey: String get() = ResultPlan.titleKey(target.garment)
    val readymadeKey: String get() = ResultPlan.readymadeKey(target.garment)

    companion object {
        /** The design's date (9.x, 10.x). */
        val SampleDate = CalendarDate(2026, 10, 16)

        /** 9.1 / 10.1 / 10.3: Rahat's panjabi with the design's note. */
        val SamplePanjabi = fromVerify(
            VerifyState.SamplePanjabi.copy(note = "হাতা একটু ঢিলা রাখবেন। বন্ধ গলা, বোতাম কাপড়ের রঙে।"),
            SampleDate,
            Fit.Regular,
        )

        /** 9.3 / 10.2: Farhana's salwar kameez with the design's note. */
        val SampleSalwarKameez = fromVerify(
            VerifyState.SampleSalwarKameez.copy(note = "কামিজে সাইড কাটা ৮ ইঞ্চি রাখবেন। সালওয়ারে পকেট দেবেন।"),
            SampleDate,
            Fit.Regular,
        )

        /** The checked measurement as the result shows it: same sections and order, the note trimmed. */
        fun fromVerify(verify: VerifyState, date: CalendarDate, fit: Fit): ResultState {
            val garment = verify.target.garment
            val sections = verify.sections.mapIndexed { i, section ->
                ResultSection(
                    section.titleKey,
                    ResultPlan.diagram(garment, i),
                    section.rows.map { ResultRow(it.key, it.inches, it.badge) },
                )
            }
            return ResultState(
                target = verify.target,
                date = date,
                fit = fit,
                sections = sections,
                size = ResultPlan.suggestedSize(garment, sections.flatMap { it.rows }),
                note = verify.note.trim(),
            )
        }

        /** Design phase: a saved measurement of [target]'s garment shows that garment's design sample. */
        fun sample(target: MeasureTarget, date: CalendarDate): ResultState = when (target.garment) {
            Garment.Panjabi -> SamplePanjabi.copy(target = target, date = date)
            Garment.SalwarKameez -> SampleSalwarKameez.copy(target = target, date = date)
            Garment.Shirt, Garment.Pants, Garment.Pajama, Garment.TShirt ->
                fromVerify(VerifyState.sampleAi(target), date, Fit.Regular)
        }
    }
}

object ResultPlan {
    /**
     * 9.3's "M": there is no women's size chart yet (plan.md §3 — the user provides it), so salwar kameez shows the
     * design's sample size until Step 10.
     */
    const val DESIGN_SAMPLE_WOMEN_SIZE = "M"

    /** Diagram of section [section]; Shirt, Pants and Pajama have no measurement list or diagram yet. */
    fun diagram(garment: Garment, section: Int): DiagramSpec? = when (garment) {
        Garment.Panjabi -> Diagrams.panjabi.takeIf { section == 0 }
        Garment.SalwarKameez -> listOf(Diagrams.kameez, Diagrams.salwar).getOrNull(section)
        Garment.Shirt, Garment.Pants, Garment.Pajama, Garment.TShirt -> null
    }

    /** Men's tops from the chest (suggestSize); salwar kameez the design sample; others have no chart yet. */
    fun suggestedSize(garment: Garment, rows: List<ResultRow>): String? = when (garment) {
        Garment.Panjabi, Garment.Shirt ->
            rows.firstOrNull { it.key == MeasurementKey.Chest }?.inches?.let { suggestSize(it) }
        Garment.SalwarKameez -> DESIGN_SAMPLE_WOMEN_SIZE
        Garment.Pants, Garment.Pajama, Garment.TShirt -> null
    }

    /** "পাঞ্জাবির মাপ" — one key per garment, because the Bangla genitive ending differs (-র / -এর). */
    fun titleKey(garment: Garment): String = "result_title_" + garment.labelKey.removePrefix("garment_")

    /** What the size is for ("রেডিমেড কামিজ কিনতে"): the kameez for salwar kameez, else the garment. */
    fun readymadeKey(garment: Garment): String =
        if (garment == Garment.SalwarKameez) "garment_kameez" else garment.labelKey

    /** The tailor's short word on the files ("ঝুল") and its English term ("Length"; hidden when it repeats the word). */
    fun shortKey(key: MeasurementKey): String = key.labelKey + "_short"
    fun termKey(key: MeasurementKey): String = key.labelKey + "_term"
}

sealed interface ResultAction {
    data object Back : ResultAction
    data class SetUnit(val unit: MeasureUnit) : ResultAction
    data class SelectTab(val index: Int) : ResultAction

    /** Share the A4 PDF (10.1 / 10.2) or the PNG (10.3) — real files and the share sheet come in Step 10.5. */
    data object SharePdf : ResultAction
    data object ShareImage : ResultAction
}

/** Pure reducer. Back and the share actions are for the caller. */
fun ResultState.reduce(action: ResultAction): ResultState = when (action) {
    is ResultAction.SetUnit -> copy(unit = action.unit)
    is ResultAction.SelectTab -> if (action.index in sections.indices) copy(tab = action.index) else this
    ResultAction.Back, ResultAction.SharePdf, ResultAction.ShareImage -> this
}

/**
 * Genji / T-shirt size (9.4): the size from chest, shoulder and length, with its neighbours for a tighter or looser
 * fit. ⚠ The T-shirt chart is missing (plan.md §3), so [chart] is the sample men's chart, which only uses the chest.
 */
data class GenjiState(
    val profileName: String,
    val date: CalendarDate,
    val fit: Fit,
    val chestInches: Float,
    val shoulderInches: Float,
    val lengthInches: Float,
    val chart: List<SizeRow> = SampleMenShirtChart,
) {
    val size: String? get() = suggestSize(chestInches, chart)
    private val index: Int get() = chart.indexOfFirst { it.size == size }

    /** The size and its neighbours in chart order (M | L | XL); fewer at the ends of the chart. */
    val compared: List<SizeRow> get() = if (index < 0) emptyList() else chart.subList(maxOf(0, index - 1), minOf(chart.size, index + 2))
    val tighter: SizeRow? get() = chart.getOrNull(index - 1)?.takeIf { index > 0 }
    val looser: SizeRow? get() = if (index < 0) null else chart.getOrNull(index + 1)

    companion object {
        /** 9.4: Rahat — chest 40", shoulder 18", length 28" → L. */
        val Sample = GenjiState("রাহাত", ResultState.SampleDate, Fit.Regular, 40f, 18f, 28f)
    }
}

sealed interface GenjiAction {
    data object Back : GenjiAction

    /** Real share in Step 10.5. */
    data object Share : GenjiAction
    data object OpenSizeChart : GenjiAction
}
