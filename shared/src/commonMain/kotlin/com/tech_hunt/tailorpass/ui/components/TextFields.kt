package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.ui.theme.Dimens

private val FieldShape = RoundedCornerShape(12.dp)

/** Form field: 16 semibold label above the content, 6dp apart. */
@Composable
fun LabeledField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(Dimens.space6)) {
        FieldLabel(label)
        content()
    }
}

@Composable
fun FieldLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

/**
 * Single-line text input: 56dp high, 12dp corners, 1dp outline (2dp primary when focused), 18 text.
 * Type-to-confirm field (12.4): 52dp, 14dp padding, 2dp red [accentColor], grey [placeholder] while empty.
 */
@Composable
fun MaapTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholder: String? = null,
    height: Dp = Dimens.textFieldHeight,
    horizontalPadding: Dp = Dimens.space16,
    accentColor: Color = MaterialTheme.colorScheme.primary,
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .optionalContentDescription(contentDescription),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardOptions = keyboardOptions,
        interactionSource = interaction,
        decorationBox = { inner ->
            Box(
                Modifier
                    .fieldFrame(focused, height, accentColor)
                    .padding(horizontal = horizontalPadding),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (placeholder != null && value.isEmpty()) {
                    Text(placeholder, fontSize = 18.sp, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                inner()
            }
        },
    )
}

/**
 * Large number + unit inside one box (height: "৫ ফুট", "৭ ইঞ্চি"): 64dp high, 30 bold number
 * right-aligned in a 44dp slot, 17 unit label. Tapping anywhere in the box focuses it.
 * The cursor always stays at the end (1–3 digit values), so new digits are always typed last.
 */
@Composable
fun UnitNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    unit: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    numberWidth: Dp = 44.dp,
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    BasicTextField(
        value = TextFieldValue(value, selection = TextRange(value.length)),
        onValueChange = { if (it.text != value) onValueChange(it.text) },
        modifier = modifier.optionalContentDescription(contentDescription),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 30.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardOptions = keyboardOptions,
        interactionSource = interaction,
        decorationBox = { inner ->
            Row(
                Modifier.fieldFrame(focused, 64.dp),
                horizontalArrangement = Arrangement.spacedBy(Dimens.space8, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.width(numberWidth), contentAlignment = Alignment.CenterEnd) { inner() }
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

/**
 * Multi-line note ("দর্জির জন্য নোট", 8.1–8.5): 64dp high, 12dp corners, 1dp outline (2dp primary when focused),
 * 8 / 14 padding, 16 text with line height 1.4; grey [placeholder] while empty. Long text scrolls inside the box.
 */
@Composable
fun NoteField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    val style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.4.sp, color = MaterialTheme.colorScheme.onSurface)
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .optionalContentDescription(contentDescription),
        textStyle = style,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        interactionSource = interaction,
        decorationBox = { inner ->
            Box(
                Modifier
                    .fieldFrame(focused, 64.dp)
                    .padding(horizontal = Dimens.space14, vertical = Dimens.space8),
            ) {
                if (value.isEmpty()) {
                    Text(placeholder, style = style, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                inner()
            }
        },
    )
}

@Composable
private fun Modifier.fieldFrame(
    focused: Boolean,
    height: Dp,
    accentColor: Color = MaterialTheme.colorScheme.primary,
): Modifier = this
    .fillMaxWidth()
    .height(height)
    .background(MaterialTheme.colorScheme.surface, FieldShape)
    .border(
        width = if (focused) 2.dp else 1.dp,
        color = if (focused) accentColor else MaterialTheme.colorScheme.outline,
        shape = FieldShape,
    )
