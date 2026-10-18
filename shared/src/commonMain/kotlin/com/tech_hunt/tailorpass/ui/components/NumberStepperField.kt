package com.tech_hunt.tailorpass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.ic_keyboard
import com.tech_hunt.tailorpass.resources.ic_minus
import com.tech_hunt.tailorpass.resources.ic_plus
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import org.jetbrains.compose.resources.painterResource

/**
 * Large number entry with − / + (7.3, 7.4, 7.10; later the edit sheet 8.3): 56dp-wide − and + buttons
 * (1dp outline, 14dp corners) around a field with a 2dp primary border (orange when [warning], 7.5),
 * big bold number + 18 unit label, centered. [height] 72 with a 44sp number, or 68 with 42sp.
 * [value] is the text as shown; typing calls [onValueChange] with the new text. The cursor stays at the end.
 * Decimal keyboard; "Done" closes it. Tapping anywhere in the field focuses it.
 */
@Composable
fun NumberStepperField(
    value: String,
    onValueChange: (String) -> Unit,
    unit: String,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    decreaseLabel: String,
    increaseLabel: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    warning: Boolean = false,
    height: Dp = Dimens.numberFieldHeight,
    fontSize: TextUnit = MaapTheme.numbers.inputValue.fontSize,
) {
    val numberStyle = MaapTheme.numbers.inputValue.copy(
        fontSize = fontSize,
        lineHeight = (fontSize.value + 4).sp,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.End,
    )
    // The number box is as wide as its text (min 24dp, so an empty field still shows the cursor).
    val measurer = rememberTextMeasurer()
    val numberWidth = with(LocalDensity.current) {
        (measurer.measure(value, numberStyle).size.width.toDp() + 2.dp).coerceAtLeast(24.dp)
    }
    val shape = MaterialTheme.shapes.medium
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.space10),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StepperButton(painterResource(Res.drawable.ic_minus), decreaseLabel, onDecrease, height)
        BasicTextField(
            value = TextFieldValue(value, selection = TextRange(value.length)),
            onValueChange = { if (it.text != value) onValueChange(it.text) },
            modifier = Modifier.weight(1f).optionalContentDescription(contentDescription),
            singleLine = true,
            textStyle = numberStyle,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
            decorationBox = { inner ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(height)
                        .background(MaterialTheme.colorScheme.surface, shape)
                        .border(2.dp, if (warning) MaapTheme.colors.problem else MaterialTheme.colorScheme.primary, shape),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.space6, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.width(numberWidth), contentAlignment = Alignment.CenterEnd) { inner() }
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
        )
        StepperButton(painterResource(Res.drawable.ic_plus), increaseLabel, onIncrease, height)
    }
}

/** 56dp-wide − / + button: 1dp outline, 14dp corners, 26dp icon. */
@Composable
private fun StepperButton(icon: Painter, label: String, onClick: () -> Unit, height: Dp) {
    val shape = MaterialTheme.shapes.medium
    Box(
        Modifier
            .size(Dimens.stepperButtonWidth, height)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(26.dp))
    }
}

/** Hint under the number field: optional 18dp keyboard icon + 14 grey text, centered ("চাপ দিয়ে লিখুন · −/+ আধা ইঞ্চি"). */
@Composable
fun StepperHint(text: String, modifier: Modifier = Modifier, showIcon: Boolean = true) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.space6, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showIcon) {
            Icon(
                painterResource(Res.drawable.ic_keyboard),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
