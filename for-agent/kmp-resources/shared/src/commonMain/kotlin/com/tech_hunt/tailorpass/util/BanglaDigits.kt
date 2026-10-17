package com.tech_hunt.tailorpass.util

/** ০-৯ ↔ 0-9 রূপান্তর এবং মাপ দেখানোর সহায়ক। */
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
 * ইঞ্চি দেখানো: ২৪.৫ → "২৪½", ৪০ → "৪০"। আধা ইঞ্চি ধাপে মাপ নেওয়া হয় বলে ½ ব্যবহার।
 * অন্য ভগ্নাংশ এক দশমিকে দেখায়। useBangla=false দিলে ইংরেজি অঙ্ক।
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

/** সেমি দেখানো এক দশমিক পর্যন্ত: ১০১.৬ */
fun inchesToCmText(inches: Float, useBangla: Boolean = true): String {
    val cm = kotlin.math.round(inches * 25.4f) / 10f
    val text = cm.toString()
    return if (useBangla) text.toBanglaDigits() else text
}

/** ফুট-ইঞ্চি → সেমি (উচ্চতা): ৫ ফুট ৭ ইঞ্চি → ১৭০ */
fun feetInchesToCm(feet: Int, inches: Int): Int = kotlin.math.round((feet * 12 + inches) * 2.54f).toInt()

/** − / + বোতামের ধাপ: আধা ইঞ্চি */
const val STEP_INCHES = 0.5f
