package com.tech_hunt.tailorpass.util

/**
 * Possessive form of a name ("রাহাতের বুক", "Rahat's chest"). Bangla: "-ের" after a final consonant
 * ("রাহাত" → "রাহাতের"), "-র" after a vowel or vowel sign ("আব্বু" → "আব্বুর", "ফারহানা" → "ফারহানার"),
 * "-এর" after anything else ("Rahat" → "Rahat-এর"). English: "'s". Blank name → "".
 */
fun possessiveOf(name: String, bangla: Boolean): String {
    val s = name.trim()
    if (s.isEmpty()) return ""
    if (!bangla) return "$s's"
    val last = s.last()
    return when {
        last in 'ক'..'হ' || last == 'ৎ' || last in 'ড়'..'য়' -> s + "ের"
        last in 'অ'..'ঔ' || last in 'া'..'ৌ' || last == 'ৗ' -> s + "র"
        else -> "$s-এর"
    }
}
