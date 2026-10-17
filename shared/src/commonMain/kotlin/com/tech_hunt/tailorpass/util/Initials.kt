package com.tech_hunt.tailorpass.util

private const val BENGALI_VIRAMA = '্'

/**
 * First letter of a name for the avatar, with its vowel signs and conjunct kept whole:
 * "রাহাত" → "রা", "আব্বু" → "আ", "ক্ষমা" → "ক্ষ", "rahat" → "R". Blank name → "".
 */
fun initialOf(name: String): String {
    val s = name.trim()
    if (s.isEmpty()) return ""
    var end = if (s[0].isHighSurrogate() && s.length > 1) 2 else 1
    while (end < s.length) {
        val c = s[end]
        end += when {
            // Virama joins the next consonant into a conjunct (ক + ্ + ষ = ক্ষ).
            c == BENGALI_VIRAMA -> if (end + 1 < s.length) 2 else 1
            c.category == CharCategory.NON_SPACING_MARK ||
                c.category == CharCategory.COMBINING_SPACING_MARK ||
                c.category == CharCategory.ENCLOSING_MARK -> 1
            else -> break
        }
    }
    return s.substring(0, end).uppercase()
}
