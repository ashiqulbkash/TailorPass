package com.tech_hunt.tailorpass.ui.screens.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.app_logo
import com.tech_hunt.tailorpass.resources.app_name
import com.tech_hunt.tailorpass.resources.fit_with_label
import com.tech_hunt.tailorpass.resources.height_feet_inches
import com.tech_hunt.tailorpass.resources.home_saved_size
import com.tech_hunt.tailorpass.resources.pdf_approx
import com.tech_hunt.tailorpass.resources.pdf_card_title
import com.tech_hunt.tailorpass.resources.pdf_check_names
import com.tech_hunt.tailorpass.resources.pdf_col_measure
import com.tech_hunt.tailorpass.resources.pdf_col_no
import com.tech_hunt.tailorpass.resources.pdf_col_value
import com.tech_hunt.tailorpass.resources.pdf_col_value_cm
import com.tech_hunt.tailorpass.resources.pdf_date
import com.tech_hunt.tailorpass.resources.pdf_disclaimer
import com.tech_hunt.tailorpass.resources.pdf_fit
import com.tech_hunt.tailorpass.resources.pdf_for_readymade
import com.tech_hunt.tailorpass.resources.pdf_garment
import com.tech_hunt.tailorpass.resources.pdf_height
import com.tech_hunt.tailorpass.resources.pdf_name
import com.tech_hunt.tailorpass.resources.pdf_page
import com.tech_hunt.tailorpass.resources.pdf_pref_legend
import com.tech_hunt.tailorpass.resources.pdf_unit
import com.tech_hunt.tailorpass.resources.pdf_verify_with_tape
import com.tech_hunt.tailorpass.resources.png_approx
import com.tech_hunt.tailorpass.resources.png_check_names
import com.tech_hunt.tailorpass.resources.result_suggested_size
import com.tech_hunt.tailorpass.resources.tailor_note
import com.tech_hunt.tailorpass.resources.text_dot_pair
import com.tech_hunt.tailorpass.resources.unit_cm
import com.tech_hunt.tailorpass.resources.unit_inch
import com.tech_hunt.tailorpass.ui.components.NumberBadge
import com.tech_hunt.tailorpass.ui.diagram.NumberedDiagram
import com.tech_hunt.tailorpass.ui.screens.manual.MeasureUnit
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.theme.PdfCardShape
import com.tech_hunt.tailorpass.ui.theme.PillShape
import com.tech_hunt.tailorpass.ui.util.formatDate
import com.tech_hunt.tailorpass.ui.util.localDigits
import com.tech_hunt.tailorpass.ui.util.str
import com.tech_hunt.tailorpass.ui.util.useBanglaDigits
import com.tech_hunt.tailorpass.util.inchesToCmText
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/*
 * The files sent to the tailor (10.1–10.3). Not app screens: common layouts that Step 10.5 renders to an A4 PDF
 * (PdfRenderer) and a 1080px-wide PNG (GraphicsLayer.toImageBitmap() + ImageEncoder). Sizes are the design's CSS px
 * as dp, always in the light theme, with the font scale fixed at 1 so the user's text size cannot break the page.
 */

/** A4 page size in the design (CSS px at 96 dpi). */
val PdfPageWidth = 794.dp
val PdfPageHeight = 1123.dp

/** Width of the share image (the design renders 1080 × 2600 for panjabi; the height follows the content). */
val SharePngWidth = 1080.dp

/** Print colors of the PDF (10.1, 10.2): near-black ink and greys that print well, not the screen theme. */
private object Print {
    val Ink = Color(0xFF111111)
    val Secondary = Color(0xFF4A4F57)
    val Border = Color(0xFF8A8F96)
    val Rule = Color(0xFFC4C8CE)
    val DiagramOutline = Color(0xFF5A6068)
}

/** Grey outline of the PNG diagram (10.3). */
private val PngDiagramOutline = Color(0xFF7A828E)

/** Light theme + font scale 1: a file looks the same whatever the phone's theme and text size. */
@Composable
private fun FileTheme(content: @Composable () -> Unit) {
    MaapTheme(darkTheme = false) {
        CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, fontScale = 1f)) {
            content()
        }
    }
}

/**
 * 10.1 / 10.2 A4 measurement card: logo + "মাপের কার্ড", info grid (name, garment, date, unit, fit, height),
 * diagram(s) next to the numbered table, note + suggested size, footer. Salwar kameez is the compact variant:
 * kameez and salwar diagrams stacked, section rows in the table, preference values starred.
 */
@Composable
fun TailorSheetPdfPage(state: ResultState, modifier: Modifier = Modifier) {
    FileTheme {
        val compact = state.sectioned
        Column(
            modifier
                .size(PdfPageWidth, PdfPageHeight)
                .background(Color.White)
                .padding(start = 56.dp, end = 56.dp, top = if (compact) 44.dp else 48.dp, bottom = if (compact) 36.dp else 40.dp),
        ) {
            PdfHeader(compact)
            PdfInfoGrid(state, compact, Modifier.padding(top = if (compact) 14.dp else 16.dp))
            Row(
                Modifier.padding(top = if (compact) 16.dp else 18.dp).height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(if (compact) 20.dp else 24.dp),
            ) {
                PdfDiagrams(state, compact)
                PdfTable(state, compact, Modifier.weight(1f))
            }
            Row(
                Modifier.padding(top = if (compact) 16.dp else 18.dp).height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(if (compact) 20.dp else 24.dp),
            ) {
                PdfNote(state, compact, Modifier.weight(1f).fillMaxHeight())
                PdfSize(state, compact, Modifier.fillMaxHeight())
            }
            Spacer(Modifier.weight(1f))
            PdfFooter(state, compact)
        }
    }
}

@Composable
private fun PdfHeader(compact: Boolean) {
    Row(
        Modifier.fillMaxWidth().rules(Print.Ink, 2.dp, bottom = true).padding(bottom = if (compact) 10.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Image(painterResource(Res.drawable.app_logo), null, Modifier.size(if (compact) 38.dp else 40.dp))
            FileText(stringResource(Res.string.app_name), 28.sp, weight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        FileText(stringResource(Res.string.pdf_card_title), 22.sp, weight = FontWeight.Bold)
    }
}

/** 3 × 2 grid in a 10dp-rounded box: grey label over a bold value, light rules between the cells. */
@Composable
private fun PdfInfoGrid(state: ResultState, compact: Boolean, modifier: Modifier) {
    val height = state.target.heightInches
    val cells = listOf(
        stringResource(Res.string.pdf_name) to state.target.profileName,
        stringResource(Res.string.pdf_garment) to str(state.target.garment.labelKey),
        stringResource(Res.string.pdf_date) to formatDate(state.date),
        stringResource(Res.string.pdf_unit) to unitName(state.unit),
        stringResource(Res.string.pdf_fit) to str(state.fit.labelKey),
        stringResource(Res.string.pdf_height) to (
            height?.let {
                stringResource(Res.string.height_feet_inches, localDigits((it / 12).toString()), localDigits((it % 12).toString()))
            } ?: "—"
            ),
    )
    Column(modifier.fillMaxWidth().clip(PdfCardShape).border(1.dp, Print.Border, PdfCardShape)) {
        cells.chunked(3).forEachIndexed { r, line ->
            Row(Modifier.height(IntrinsicSize.Min)) {
                line.forEachIndexed { c, (label, value) ->
                    Column(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .rules(Print.Rule, 1.dp, end = c < 2, bottom = r == 0)
                            .padding(horizontal = 16.dp, vertical = if (compact) 6.dp else 8.dp),
                    ) {
                        FileText(label, 14.sp, color = Print.Secondary)
                        FileText(value, if (compact) 21.sp else 22.sp, weight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/** One 300dp diagram box (panjabi), or the kameez and salwar diagrams stacked in a 250dp column with titles. */
@Composable
private fun PdfDiagrams(state: ResultState, compact: Boolean) {
    val boxed = Modifier.border(1.dp, Print.Rule, PdfCardShape)
    if (compact) {
        Column(Modifier.width(250.dp).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            state.sections.forEach { section ->
                Column(
                    boxed.weight(1f).fillMaxWidth().padding(top = 8.dp, bottom = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    FileText(str(section.titleKey.orEmpty()), 16.sp, weight = FontWeight.Bold)
                    section.diagram?.let {
                        NumberedDiagram(it, Modifier.size(186.dp, 279.dp), Print.DiagramOutline, badgeRadius = 15f, badgeFontSize = 17f)
                    }
                }
            }
        }
    } else {
        Box(boxed.width(300.dp).fillMaxHeight(), contentAlignment = Alignment.Center) {
            state.sections.firstOrNull()?.diagram?.let {
                NumberedDiagram(it, Modifier.size(284.dp, 355.dp), Print.DiagramOutline, badgeRadius = 9f, badgeFontSize = 12f)
            }
        }
    }
}

/** Numbered table: grey column header, a tinted row per section (compact), one row per measurement. */
@Composable
private fun PdfTable(state: ResultState, compact: Boolean, modifier: Modifier) {
    val padding = if (compact) 14.dp else 16.dp
    val gap = if (compact) 12.dp else 14.dp
    val numberWidth = if (compact) 26.dp else 30.dp
    Column(modifier.clip(PdfCardShape).border(1.dp, Print.Border, PdfCardShape)) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(if (compact) 34.dp else 42.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .rules(Print.Border, 1.dp, bottom = true)
                .padding(horizontal = padding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            val size = if (compact) 14.sp else 15.sp
            val color = MaapTheme.colors.textEmphasis
            FileText(stringResource(Res.string.pdf_col_no), size, Modifier.width(numberWidth), FontWeight.Bold, color)
            FileText(stringResource(Res.string.pdf_col_measure), size, Modifier.weight(1f), FontWeight.Bold, color)
            FileText(
                stringResource(if (state.unit == MeasureUnit.Cm) Res.string.pdf_col_value_cm else Res.string.pdf_col_value),
                size, weight = FontWeight.Bold, color = color,
            )
        }
        val last = state.rows.size - 1
        var index = 0
        state.sections.forEach { section ->
            if (compact) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .rules(Print.Border, 1.dp, bottom = true)
                        .padding(horizontal = padding),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    FileText(str(section.titleKey.orEmpty()), 17.sp, weight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
            section.rows.forEachIndexed { i, row ->
                FileRow(
                    number = localDigits((i + 1).toString()),
                    row = row,
                    unit = state.unit,
                    sizes = if (compact) PdfCompactRow else PdfRow,
                    star = row.isChoice,
                    tapeNote = !compact && row.checkWithTape,
                    modifier = Modifier
                        .rules(Print.Rule, 1.dp, bottom = compact || index != last)
                        .padding(horizontal = padding),
                )
                index++
            }
        }
    }
}

/** "দর্জির জন্য নোট" box; empty when no note was written, so the tailor can still write on the printout. */
@Composable
private fun PdfNote(state: ResultState, compact: Boolean, modifier: Modifier) {
    Column(
        modifier.border(1.dp, Print.Border, PdfCardShape).padding(horizontal = 18.dp, vertical = if (compact) 10.dp else 12.dp),
        verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 6.dp),
    ) {
        FileText(stringResource(Res.string.tailor_note), 16.sp, weight = FontWeight.Bold, color = MaapTheme.colors.textEmphasis)
        if (state.note.isNotEmpty()) FileText(state.note, 18.sp, lineHeight = if (compact) 1.5f else 1.55f)
    }
}

/** 200dp box with a 2dp primary border: "সাজেস্ট করা সাইজ", the size, "রেডিমেড কেনার জন্য · রেগুলার ফিট". */
@Composable
private fun PdfSize(state: ResultState, compact: Boolean, modifier: Modifier) {
    Column(
        modifier
            .width(200.dp)
            .border(2.dp, MaterialTheme.colorScheme.primary, PdfCardShape)
            .padding(horizontal = 16.dp, vertical = if (compact) 8.dp else 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FileText(stringResource(Res.string.result_suggested_size), 15.sp, weight = FontWeight.SemiBold, color = MaapTheme.colors.textEmphasis)
        SizeLetter(
            localDigits(state.size ?: "—"),
            if (compact) 50.sp else 54.sp,
            if (compact) 52.5.dp else 56.7.dp,
            MaterialTheme.colorScheme.primary,
        )
        FileText(
            stringResource(
                Res.string.text_dot_pair,
                stringResource(Res.string.pdf_for_readymade),
                stringResource(Res.string.fit_with_label, str(state.fit.labelKey)),
            ),
            13.sp,
            color = Print.Secondary,
            align = TextAlign.Center,
        )
    }
}

/**
 * Footer: the approximate-values line, the star legend when values are starred, and (compact) the measurements to
 * check with a tape — the full layout notes those under their rows instead. "পৃষ্ঠা ১/১" at the end.
 */
@Composable
private fun PdfFooter(state: ResultState, compact: Boolean) {
    val parts = buildList {
        add(stringResource(if (compact) Res.string.pdf_approx else Res.string.pdf_disclaimer))
        if (state.hasChoices) add(stringResource(Res.string.pdf_pref_legend))
        if (compact && state.checkKeys.isNotEmpty()) add(stringResource(Res.string.pdf_check_names, checkNames(state)))
    }
    Row(
        Modifier.fillMaxWidth().rules(Print.Rule, 1.dp, top = true).padding(top = if (compact) 10.dp else 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        FileText(parts.joinToString(" "), 13.sp, Modifier.weight(1f), color = Print.Secondary, lineHeight = 1.5f)
        FileText(stringResource(Res.string.pdf_page, localDigits("1"), localDigits("1")), 13.sp, color = Print.Secondary, lineHeight = 1.5f)
    }
}

/**
 * 10.3 share image, 1080dp wide: blue header card (logo, name, garment · date, size / fit / unit chips), diagram
 * card, numbered list card, note card, footer line. Salwar kameez shows both diagrams and a title row per part.
 */
@Composable
fun TailorSharePng(state: ResultState, modifier: Modifier = Modifier) {
    FileTheme {
        Column(
            modifier.width(SharePngWidth).background(MaterialTheme.colorScheme.background).padding(64.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            PngHeader(state)
            PngCard(Modifier.height(590.dp), PaddingValues()) {
                Row(
                    Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val diagrams = state.sections.mapNotNull { it.diagram }
                    diagrams.forEach {
                        NumberedDiagram(
                            it,
                            if (diagrams.size > 1) Modifier.size(320.dp, 480.dp) else Modifier.size(432.dp, 540.dp),
                            PngDiagramOutline,
                            badgeRadius = if (diagrams.size > 1) 15f else 9f,
                            badgeFontSize = if (diagrams.size > 1) 17f else 12f,
                        )
                    }
                }
            }
            PngCard(Modifier, PaddingValues(horizontal = 48.dp, vertical = 16.dp)) {
                state.sections.forEach { section ->
                    if (state.sectioned) {
                        Box(Modifier.fillMaxWidth().height(96.dp).rules(MaapTheme.colors.divider, 2.dp, bottom = true), contentAlignment = Alignment.CenterStart) {
                            FileText(str(section.titleKey.orEmpty()), 40.sp, weight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    section.rows.forEachIndexed { i, row ->
                        FileRow(
                            number = localDigits((i + 1).toString()),
                            row = row,
                            unit = state.unit,
                            sizes = PngRow,
                            star = false,
                            tapeNote = false,
                            modifier = Modifier.rules(MaapTheme.colors.divider, 2.dp, bottom = true),
                            ink = MaterialTheme.colorScheme.onSurface,
                            secondary = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            if (state.note.isNotEmpty()) {
                PngCard(Modifier, PaddingValues(horizontal = 48.dp, vertical = 36.dp), Arrangement.spacedBy(12.dp)) {
                    FileText(stringResource(Res.string.tailor_note), 36.sp, weight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    FileText(state.note, 46.sp, lineHeight = 1.45f)
                }
            }
            val approx = stringResource(Res.string.png_approx)
            FileText(
                if (state.checkKeys.isEmpty()) approx
                else stringResource(Res.string.text_dot_pair, approx, stringResource(Res.string.png_check_names, checkNames(state))),
                30.sp,
                Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 1.4f,
                align = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun PngHeader(state: ResultState) {
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(48.dp))
            .padding(horizontal = 56.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // The logo's blue tile disappears on the blue card; its white mark stays.
            Image(painterResource(Res.drawable.app_logo), null, Modifier.size(56.dp))
            FileText(stringResource(Res.string.app_name), 40.sp, Modifier.alpha(0.92f), FontWeight.Bold, onPrimary)
        }
        FileText(state.target.profileName, 88.sp, weight = FontWeight.Bold, color = onPrimary, lineHeight = 1.15f)
        FileText(
            stringResource(Res.string.text_dot_pair, str(state.target.garment.labelKey), formatDate(state.date)),
            48.sp, weight = FontWeight.SemiBold, color = onPrimary, lineHeight = 1.3f,
        )
        FlowRow(
            Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            state.size?.let {
                PngChip(stringResource(Res.string.home_saved_size, localDigits(it)), filled = true)
            }
            PngChip(stringResource(Res.string.fit_with_label, str(state.fit.labelKey)), filled = false)
            PngChip(unitName(state.unit), filled = false)
        }
    }
}

/** Header chip: white with primary text (size), or a 3dp white outline (fit, unit). */
@Composable
private fun PngChip(text: String, filled: Boolean) {
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    Box(
        Modifier
            .then(if (filled) Modifier.background(onPrimary, PillShape) else Modifier.border(3.dp, onPrimary, PillShape))
            .padding(horizontal = 28.dp, vertical = 8.dp),
    ) {
        FileText(
            text,
            36.sp,
            weight = if (filled) FontWeight.Bold else FontWeight.SemiBold,
            color = if (filled) MaterialTheme.colorScheme.primary else onPrimary,
        )
    }
}

/** White card, 3dp light border, 48dp corners. */
@Composable
private fun PngCard(
    modifier: Modifier,
    padding: PaddingValues,
    arrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(48.dp)
    Column(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape)
            .border(3.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .padding(padding),
        verticalArrangement = arrangement,
        content = content,
    )
}

/** Sizes of a file table row: height, badge, name / term / value text, gaps. */
private data class FileRowSizes(
    val height: Dp,
    val badge: Dp,
    val badgeFont: TextUnit,
    val name: TextUnit,
    val term: TextUnit,
    val value: TextUnit,
    val gap: Dp,
    val termGap: Dp,
)

private val PdfRow = FileRowSizes(50.dp, 30.dp, 16.sp, 20.sp, 13.sp, 28.sp, 14.dp, 6.dp)
private val PdfCompactRow = FileRowSizes(37.dp, 26.dp, 14.sp, 17.sp, 12.sp, 23.sp, 12.dp, 6.dp)
private val PngRow = FileRowSizes(116.dp, 68.dp, 36.sp, 52.sp, 30.sp, 72.sp, 28.dp, 14.dp)

/**
 * One measurement on a file: number, the tailor's short word (bold; "*" when [star]) with the English term after it
 * (left out when the same, i.e. in English), [tapeNote] "* ফিতায় মিলিয়ে নিন" under it, the bold value.
 */
@Composable
private fun FileRow(
    number: String,
    row: ResultRow,
    unit: MeasureUnit,
    sizes: FileRowSizes,
    star: Boolean,
    tapeNote: Boolean,
    modifier: Modifier,
    ink: Color = Print.Ink,
    secondary: Color = Print.Secondary,
) {
    val short = str(ResultPlan.shortKey(row.key))
    val term = str(ResultPlan.termKey(row.key))
    Row(
        modifier.fillMaxWidth().height(sizes.height),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(sizes.gap),
    ) {
        NumberBadge(number, size = sizes.badge, fontSize = sizes.badgeFont)
        Column(Modifier.weight(1f)) {
            Row(horizontalArrangement = Arrangement.spacedBy(sizes.termGap)) {
                FileText(if (star) "$short *" else short, sizes.name, Modifier.alignByBaseline(), FontWeight.Bold, ink)
                if (term != short) FileText(term, sizes.term, Modifier.alignByBaseline(), color = secondary)
            }
            if (tapeNote) FileText(stringResource(Res.string.pdf_verify_with_tape), sizes.term, color = secondary, lineHeight = 1.15f)
        }
        FileText(fileValue(row.inches, unit), sizes.value, weight = FontWeight.Bold, color = ink)
    }
}

/** A file value: "২৪½"" in inches; in cm only the number (the column header and the unit cell say "সেমি"). */
@Composable
private fun fileValue(inches: Float?, unit: MeasureUnit): String {
    val bangla = useBanglaDigits()
    return when {
        inches == null -> "—"
        unit == MeasureUnit.Cm -> inchesToCmText(inches, bangla)
        else -> resultValue(inches, unit)
    }
}

@Composable
private fun unitName(unit: MeasureUnit): String =
    stringResource(if (unit == MeasureUnit.Cm) Res.string.unit_cm else Res.string.unit_inch)

/** "গলা" / "আর্মহোল, হিপ" — the short words of the measurements to check with a tape. */
@Composable
private fun checkNames(state: ResultState): String = state.checkKeys.map { str(ResultPlan.shortKey(it)) }.joinToString(", ")

/** File text: Hind Siliguri at [size] with line height [lineHeight] × size (the design's CSS line-height). */
@Composable
private fun FileText(
    text: String,
    size: TextUnit,
    modifier: Modifier = Modifier,
    weight: FontWeight = FontWeight.Normal,
    color: Color = Print.Ink,
    lineHeight: Float = 1.25f,
    align: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        fontSize = size,
        lineHeight = size * lineHeight,
        fontWeight = weight,
        color = color,
        textAlign = align ?: TextAlign.Unspecified,
    )
}

/** Thin lines on chosen edges (table and grid rules); [end] follows the layout direction. */
private fun Modifier.rules(
    color: Color,
    width: Dp,
    top: Boolean = false,
    bottom: Boolean = false,
    end: Boolean = false,
): Modifier = drawBehind {
    val w = width.toPx()
    if (top) drawLine(color, Offset(0f, w / 2), Offset(size.width, w / 2), w)
    if (bottom) drawLine(color, Offset(0f, size.height - w / 2), Offset(size.width, size.height - w / 2), w)
    if (end) {
        val x = if (layoutDirection == LayoutDirection.Ltr) size.width - w / 2 else w / 2
        drawLine(color, Offset(x, 0f), Offset(x, size.height), w)
    }
}

@Preview(widthDp = 794, heightDp = 1123)
@Composable
private fun TailorPdfPreview() {
    TailorSheetPdfPage(ResultState.SamplePanjabi)
}

@Preview(widthDp = 794, heightDp = 1123)
@Composable
private fun TailorPdfSalwarKameezPreview() {
    TailorSheetPdfPage(ResultState.SampleSalwarKameez)
}

@Preview(widthDp = 1080, heightDp = 2600)
@Composable
private fun SharePngPreview() {
    TailorSharePng(ResultState.SamplePanjabi)
}
