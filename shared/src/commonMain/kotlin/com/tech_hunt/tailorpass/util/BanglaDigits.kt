package com.tech_hunt.tailorpass.util

/** Bengali ↔ Latin digit conversion (০-৯ ↔ 0-9) and helpers for displaying measurements. */
private const val BN = "০১২৩৪৫৬৭৮৯"

fun String.toBanglaDigits(): String = buildString(length) {
    for (c in this@toBanglaDigits) append(if (c in '0'..'9') BN[c - '0'] else c)
}

fun String.toLatinDigits(): String = buildString(length) {
    for (c in this@toLatinDigits) {
        val i = BN.indexOf(c)
        append(if (i >= 0) ('0' + i) else c)
    }
}

/**
 * Formats inches: 24.5 → "২৪½", 40 → "৪০". Measurements use half-inch steps, so halves are shown as ½.
 * Other fractions are shown with one decimal. useBangla = false gives Latin digits.
 */
fun formatInches(value: Float, useBangla: Boolean = true): String {
    val whole = value.toInt()
    val frac = value - whole
    val text = when {
        frac < 0.01f -> "$whole"
        kotlin.math.abs(frac - 0.5f) < 0.01f -> "$whole½"
        else -> ((value * 10).toInt() / 10f).toString()
    }
    return if (useBangla) text.toBanglaDigits() else text
}

/** Inches → centimeters text, one decimal: 40 → "১০১.৬". */
fun inchesToCmText(inches: Float, useBangla: Boolean = true): String {
    val cm = kotlin.math.round(inches * 25.4f) / 10f
    val text = cm.toString()
    return if (useBangla) text.toBanglaDigits() else text
}

/** Feet + inches → centimeters (height): 5 ft 7 in → 170. */
fun feetInchesToCm(feet: Int, inches: Int): Int = kotlin.math.round((feet * 12 + inches) * 2.54f).toInt()

/** Step of the − / + buttons: half an inch. */
const val STEP_INCHES = 0.5f
