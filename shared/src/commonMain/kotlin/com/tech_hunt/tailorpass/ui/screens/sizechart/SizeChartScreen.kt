package com.tech_hunt.tailorpass.ui.screens.sizechart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.model.SizeRow
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.size_chart_basis
import com.tech_hunt.tailorpass.resources.size_chart_for_whom
import com.tech_hunt.tailorpass.resources.size_chart_garments
import com.tech_hunt.tailorpass.resources.size_chart_in_cm
import com.tech_hunt.tailorpass.resources.size_chart_in_inches
import com.tech_hunt.tailorpass.resources.size_chart_missing
import com.tech_hunt.tailorpass.resources.size_chart_note
import com.tech_hunt.tailorpass.resources.size_chart_title
import com.tech_hunt.tailorpass.resources.size_col_chest
import com.tech_hunt.tailorpass.resources.size_col_hip
import com.tech_hunt.tailorpass.resources.size_col_size
import com.tech_hunt.tailorpass.resources.size_col_waist
import com.tech_hunt.tailorpass.resources.size_range
import com.tech_hunt.tailorpass.resources.size_yours
import com.tech_hunt.tailorpass.resources.text_dot_pair
import com.tech_hunt.tailorpass.ui.components.BottomNav
import com.tech_hunt.tailorpass.ui.components.BottomTab
import com.tech_hunt.tailorpass.ui.components.MaapCard
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.SegmentedToggle
import com.tech_hunt.tailorpass.ui.components.SelectChip
import com.tech_hunt.tailorpass.ui.components.TabTitleBar
import com.tech_hunt.tailorpass.ui.components.TextBadge
import com.tech_hunt.tailorpass.ui.components.WarningNote
import com.tech_hunt.tailorpass.ui.components.optionalContentDescription
import com.tech_hunt.tailorpass.ui.screens.manual.MeasureUnit
import com.tech_hunt.tailorpass.ui.screens.result.resultValue
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.str
import com.tech_hunt.tailorpass.ui.util.useBanglaDigits
import com.tech_hunt.tailorpass.util.formatInches
import com.tech_hunt.tailorpass.util.possessiveOf
import org.jetbrains.compose.resources.stringResource

/**
 * 11.1 Size chart tab: men / women toggle, garment chips, XS–XXXL table with the profile's row marked, the unit and
 * basis line, the brand note. Bottom nav.
 */
@Composable
fun SizeChartScreen(state: SizeChartState, onTabSelect: (BottomTab) -> Unit, onAction: (SizeChartAction) -> Unit) {
    MaapScaffold(
        topBar = { TabTitleBar(stringResource(Res.string.size_chart_title)) },
        bottomNav = { BottomNav(selected = BottomTab.SizeChart, onSelect = onTabSelect) },
        contentPadding = PaddingValues(start = Dimens.screenPadding, end = Dimens.screenPadding, top = Dimens.space4, bottom = Dimens.space12),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.space12)) {
            val genders = Gender.entries
            SegmentedToggle(
                options = genders.map { str(it.labelKey) },
                selectedIndex = genders.indexOf(state.gender),
                onSelect = { onAction(SizeChartAction.SetGender(genders[it])) },
                modifier = Modifier.fillMaxWidth(),
                contentDescription = stringResource(Res.string.size_chart_for_whom),
                height = 48.dp,
                fontSize = 17.sp,
                fill = true,
            )
            FlowRow(
                modifier = Modifier.optionalContentDescription(stringResource(Res.string.size_chart_garments)),
                horizontalArrangement = Arrangement.spacedBy(Dimens.space8),
                verticalArrangement = Arrangement.spacedBy(Dimens.space8),
            ) {
                state.garments.forEach { garment ->
                    SelectChip(
                        text = str(garment.labelKey),
                        selected = garment == state.garment,
                        onClick = { onAction(SizeChartAction.SelectGarment(garment)) },
                    )
                }
            }
            val chart = state.chart
            if (chart == null) {
                MaapCard(Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(Res.string.size_chart_missing),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.space8)) {
                    SizeTable(chart, state.yourSize, state.unit)
                    Text(
                        text = footnote(state),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            WarningNote(stringResource(Res.string.size_chart_note))
        }
    }
}

/** "সব মাপ ইঞ্চিতে · রাহাতের বুক ৪০" অনুযায়ী"; only the unit when no row is marked. */
@Composable
private fun footnote(state: SizeChartState): String {
    val unit = stringResource(if (state.unit == MeasureUnit.Cm) Res.string.size_chart_in_cm else Res.string.size_chart_in_inches)
    val chest = state.chestInches
    if (state.yourSize == null || chest == null) return unit
    val basis = stringResource(
        Res.string.size_chart_basis,
        possessiveOf(state.profileName, useBanglaDigits()),
        resultValue(chest, state.unit),
    )
    return stringResource(Res.string.text_dot_pair, unit, basis)
}

/**
 * Card table: 44dp grey header (15 bold), 44dp rows (16, size bold) with a 1dp line above each;
 * the profile's row 52dp, 17 bold primary on primary container, 2dp primary frame, "আপনার" badge.
 */
@Composable
private fun SizeTable(rows: List<SizeRow>, yourSize: String?, unit: MeasureUnit) {
    MaapCard(Modifier.fillMaxWidth().clip(MaterialTheme.shapes.large), contentPadding = PaddingValues()) {
        TableRow(
            height = 44.dp,
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant).semantics { heading() },
        ) {
            val header = MaapTheme.colors.textEmphasis
            Cell(stringResource(Res.string.size_col_size), header, FontWeight.Bold, 15, TextAlign.Start)
            Cell(stringResource(Res.string.size_col_chest), header, FontWeight.Bold, 15)
            Cell(stringResource(Res.string.size_col_waist), header, FontWeight.Bold, 15)
            Cell(stringResource(Res.string.size_col_hip), header, FontWeight.Bold, 15)
        }
        val line = MaterialTheme.colorScheme.surfaceVariant
        rows.forEachIndexed { index, row ->
            val yours = row.size == yourSize
            val afterYours = index > 0 && rows[index - 1].size == yourSize
            val primary = MaterialTheme.colorScheme.primary
            TableRow(
                height = if (yours) 52.dp else 44.dp,
                modifier = if (yours) {
                    Modifier.background(MaterialTheme.colorScheme.primaryContainer).border(2.dp, primary)
                } else if (afterYours) {
                    Modifier
                } else {
                    Modifier.drawBehind { drawLine(line, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx()) }
                },
            ) {
                val color = if (yours) primary else MaterialTheme.colorScheme.onSurface
                val weight = if (yours) FontWeight.Bold else FontWeight.Normal
                val fontSize = if (yours) 17 else 16
                Row(
                    Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.space6),
                ) {
                    Text(row.size, style = MaterialTheme.typography.bodyMedium, fontSize = fontSize.sp, fontWeight = FontWeight.Bold, color = color, maxLines = 1)
                    if (yours) {
                        TextBadge(
                            text = stringResource(Res.string.size_yours),
                            background = primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 12.sp,
                            padding = PaddingValues(horizontal = Dimens.space6, vertical = 1.dp),
                            cornerRadius = 8.dp,
                        )
                    }
                }
                Cell(range(row.chestMin, row.chestMax, unit), color, weight, fontSize)
                Cell(range(row.waistMin, row.waistMax, unit), color, weight, fontSize)
                Cell(range(row.hipMin, row.hipMax, unit), color, weight, fontSize)
            }
        }
    }
}

@Composable
private fun TableRow(height: Dp, modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(height).then(modifier).padding(horizontal = Dimens.space12),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
private fun RowScope.Cell(
    text: String,
    color: Color,
    weight: FontWeight,
    fontSize: Int,
    align: TextAlign = TextAlign.Center,
) {
    Text(
        text = text,
        modifier = Modifier.weight(1f),
        style = MaterialTheme.typography.bodyMedium,
        fontSize = fontSize.sp,
        fontWeight = weight,
        color = color,
        textAlign = align,
        maxLines = 1,
    )
}

/** "৩৪–৩৬" in inches; whole centimeters in cm ("৮৬–৯১"), so the widest rows still fit their column. */
@Composable
private fun range(min: Float, max: Float, unit: MeasureUnit): String {
    val bangla = useBanglaDigits()
    val format: (Float) -> String = when (unit) {
        MeasureUnit.Inch -> { v -> formatInches(v, bangla) }
        MeasureUnit.Cm -> { v -> formatInches(inchesToWholeCm(v).toFloat(), bangla) }
    }
    return stringResource(Res.string.size_range, format(min), format(max))
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun SizeChartPreview() {
    MaapTheme(darkTheme = false) { SizeChartScreen(SizeChartState.Sample, {}) {} }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun SizeChartWomenPreview() {
    MaapTheme(darkTheme = false) {
        SizeChartScreen(SizeChartState.Sample.copy(gender = Gender.Female, garment = SizeChartState.garmentsFor(Gender.Female).first()), {}) {}
    }
}
