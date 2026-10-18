package com.tech_hunt.tailorpass.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

/**
 * [text] with [part] in [style] ("যা লাগবে:" bold in 7.1, the entered value in 7.5). The part is a separate
 * string in the same language; [last] picks its last occurrence. Plain text when the part is not found.
 */
fun emphasize(
    text: String,
    part: String,
    style: SpanStyle = SpanStyle(fontWeight = FontWeight.Bold),
    last: Boolean = false,
): AnnotatedString {
    val start = if (part.isEmpty()) -1 else if (last) text.lastIndexOf(part) else text.indexOf(part)
    return buildAnnotatedString {
        append(text)
        if (start >= 0) addStyle(style, start, start + part.length)
    }
}
