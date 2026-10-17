package com.tech_hunt.tailorpass.ui.util

import androidx.compose.runtime.Composable
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.date_day_month_year
import com.tech_hunt.tailorpass.resources.month_1
import com.tech_hunt.tailorpass.resources.month_10
import com.tech_hunt.tailorpass.resources.month_11
import com.tech_hunt.tailorpass.resources.month_12
import com.tech_hunt.tailorpass.resources.month_2
import com.tech_hunt.tailorpass.resources.month_3
import com.tech_hunt.tailorpass.resources.month_4
import com.tech_hunt.tailorpass.resources.month_5
import com.tech_hunt.tailorpass.resources.month_6
import com.tech_hunt.tailorpass.resources.month_7
import com.tech_hunt.tailorpass.resources.month_8
import com.tech_hunt.tailorpass.resources.month_9
import org.jetbrains.compose.resources.stringResource

/** A calendar day (month 1–12). Design-phase date type; Step 10 stores real dates. */
data class CalendarDate(val year: Int, val month: Int, val day: Int) {
    init {
        require(month in 1..12) { "month must be 1..12" }
        require(day in 1..31) { "day must be 1..31" }
    }
}

private val MonthNames = listOf(
    Res.string.month_1, Res.string.month_2, Res.string.month_3, Res.string.month_4,
    Res.string.month_5, Res.string.month_6, Res.string.month_7, Res.string.month_8,
    Res.string.month_9, Res.string.month_10, Res.string.month_11, Res.string.month_12,
)

/** "১৬ অক্টোবর ২০২৬" in Bangla, "16 Oct 2026" in English. */
@Composable
fun formatDate(date: CalendarDate): String = stringResource(
    Res.string.date_day_month_year,
    localDigits(date.day.toString()),
    stringResource(MonthNames[date.month - 1]),
    localDigits(date.year.toString()),
)
