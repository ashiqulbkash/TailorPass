package com.tech_hunt.tailorpass.ui.screens.profile

import com.tech_hunt.tailorpass.model.Fit
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.util.feetInchesToCm
import com.tech_hunt.tailorpass.util.toLatinDigits
import kotlin.math.roundToInt

enum class HeightUnit { FeetInch, Cm }

/**
 * 1.5 Create profile form. Height fields hold Latin digits as typed ("5", "7", "170");
 * the screen shows them in Bengali digits when the app language is Bangla.
 */
data class ProfileCreateState(
    val name: String = "",
    val gender: Gender = Gender.Male,
    val heightUnit: HeightUnit = HeightUnit.FeetInch,
    val feet: String = "",
    val inches: String = "",
    val cm: String = "",
    val fit: Fit = Fit.Regular,
) {
    /** Height in whole centimeters, or null while the height is incomplete. */
    val heightCm: Int?
        get() = when (heightUnit) {
            HeightUnit.FeetInch -> {
                val f = feet.toIntOrNull()
                val i = if (inches.isEmpty()) 0 else inches.toIntOrNull()
                if (f == null || i == null || f <= 0) null else feetInchesToCm(f, i)
            }
            HeightUnit.Cm -> cm.toIntOrNull()?.takeIf { it > 0 }
        }

    val canSave: Boolean get() = name.isNotBlank() && heightCm != null

    companion object {
        /** Design sample (1.5): Rahat, male, 5′7″, regular. */
        val Sample = ProfileCreateState(name = "রাহাত", gender = Gender.Male, feet = "5", inches = "7", fit = Fit.Regular)
    }
}

sealed interface ProfileCreateAction {
    data class NameChanged(val name: String) : ProfileCreateAction
    data class GenderSelected(val gender: Gender) : ProfileCreateAction
    data class HeightUnitSelected(val unit: HeightUnit) : ProfileCreateAction
    data class FeetChanged(val text: String) : ProfileCreateAction
    data class InchesChanged(val text: String) : ProfileCreateAction
    data class CmChanged(val text: String) : ProfileCreateAction
    data class FitSelected(val fit: Fit) : ProfileCreateAction
    data object Save : ProfileCreateAction
    data object Back : ProfileCreateAction
}

/** Pure form reducer. Save/Back are handled by the caller (navigation) and leave the state unchanged. */
fun ProfileCreateState.reduce(action: ProfileCreateAction): ProfileCreateState = when (action) {
    is ProfileCreateAction.NameChanged -> copy(name = action.name)
    is ProfileCreateAction.GenderSelected -> copy(gender = action.gender)
    is ProfileCreateAction.HeightUnitSelected -> switchUnit(action.unit)
    is ProfileCreateAction.FeetChanged -> digits(action.text, maxLength = 1)?.let { copy(feet = it) } ?: this
    is ProfileCreateAction.InchesChanged ->
        digits(action.text, maxLength = 2) { it.toInt() <= 11 }?.let { copy(inches = it) } ?: this
    is ProfileCreateAction.CmChanged -> digits(action.text, maxLength = 3)?.let { copy(cm = it) } ?: this
    is ProfileCreateAction.FitSelected -> copy(fit = action.fit)
    ProfileCreateAction.Save, ProfileCreateAction.Back -> this
}

/** Converts the entered height to the other unit, so switching does not lose it. */
private fun ProfileCreateState.switchUnit(unit: HeightUnit): ProfileCreateState {
    if (unit == heightUnit) return this
    val cmValue = heightCm
    return when (unit) {
        HeightUnit.Cm -> copy(heightUnit = unit, cm = cmValue?.toString() ?: "")
        HeightUnit.FeetInch -> {
            val totalInches = cmValue?.let { (it / 2.54).roundToInt() }
            copy(
                heightUnit = unit,
                feet = totalInches?.let { (it / 12).toString() } ?: "",
                inches = totalInches?.let { (it % 12).toString() } ?: "",
            )
        }
    }
}

/**
 * Typed text → Latin digits (Bengali keyboards type ০-৯), or null if it contains anything else.
 * The field's cursor stays at the end, so when the result is too long or out of range (typing into a full
 * field: feet "5" → "56", inches "7" → "71") the newly typed last digit starts the number again.
 */
private fun digits(text: String, maxLength: Int, inRange: (String) -> Boolean = { true }): String? {
    val latin = text.toLatinDigits()
    if (!latin.all { it in '0'..'9' }) return null
    if (latin.isEmpty() || (latin.length <= maxLength && inRange(latin))) return latin
    return latin.takeLast(1).takeIf(inRange)
}
