package com.tech_hunt.tailorpass.util

/**
 * Text of a measurement input field (tape entry, edit sheet). The field holds Latin digits as typed
 * ("24.5"); the screen shows them as "২৪½" in Bangla (see displayInchInput).
 */

/** Typed text → inches. Accepts Latin or Bengali digits, "." and "½" ("24.5", "২৪½"). Null if not a positive number. */
fun parseInches(text: String): Float? {
    val latin = text.trim().toLatinDigits().replace("½", ".5")
    if (latin.isEmpty() || latin == ".") return null
    return latin.toFloatOrNull()?.takeIf { it > 0f }
}

/**
 * Cleans a new field text: Bengali digits → Latin, "," / "½" → decimal point. Allows up to 3 whole digits and
 * one decimal digit. Returns null when the text is not allowed (the field keeps its old text).
 */
fun sanitizeInchInput(text: String): String? {
    val latin = text.toLatinDigits().replace(',', '.').replace("½", ".5")
    if (latin.any { it != '.' && it !in '0'..'9' }) return null
    val parts = latin.split('.')
    if (parts.size > 2) return null
    if (parts[0].length > 3) return null
    if (parts.size == 2 && parts[1].length > 1) return null
    return latin
}

/** Inches → field text in Latin digits: 24.5 → "24.5", 40 → "40". */
fun inchesToInput(value: Float): String {
    val tenths = kotlin.math.round(value * 10f).toInt()
    return if (tenths % 10 == 0) "${tenths / 10}" else "${tenths / 10}.${tenths % 10}"
}

/** Field text as shown: a trailing ".5" becomes "½", digits are Bengali when [useBangla]. "24.5" → "২৪½". */
fun displayInchInput(text: String, useBangla: Boolean = true): String {
    val shown = if (text.endsWith(".5") && text.length > 2) text.dropLast(2) + "½" else text
    return if (useBangla) shown.toBanglaDigits() else shown
}
