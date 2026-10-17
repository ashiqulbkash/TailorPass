package com.tech_hunt.tailorpass

import com.tech_hunt.tailorpass.model.Diagrams
import com.tech_hunt.tailorpass.model.Fit
import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.GarmentMeasurements
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.model.LengthPreset
import com.tech_hunt.tailorpass.model.MeasurementKey
import com.tech_hunt.tailorpass.model.NeckDepthPreset
import com.tech_hunt.tailorpass.model.SourceBadge
import com.tech_hunt.tailorpass.ui.screens.home.HomeState
import com.tech_hunt.tailorpass.ui.screens.home.SavedDetail
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Checks the key rule: every key/name the models use exists in composeResources (bn and en). */
class ResourceKeysTest {
    private val resDir = File("src/commonMain/composeResources")

    private fun stringKeys(dir: String): Set<String> =
        Regex("""<string name="([a-z0-9_]+)"""").findAll(File(resDir, "$dir/strings.xml").readText())
            .map { it.groupValues[1] }.toSet()

    @Test
    fun banglaAndEnglishHaveTheSameKeys() {
        val bn = stringKeys("values")
        val en = stringKeys("values-en")
        assertTrue(bn.isNotEmpty())
        assertEquals(bn - en, emptySet(), "missing in values-en")
        assertEquals(en - bn, emptySet(), "missing in values (bn)")
    }

    @Test
    fun noAndroidQuoteEscapes() {
        // Compose resources show \" and \' literally (found on device in Step 0), so quotes are written plain.
        listOf("values", "values-en").forEach { dir ->
            val bad = File(resDir, "$dir/strings.xml").readLines().filter { "\\\"" in it || "\\'" in it }
            assertEquals(emptyList(), bad, dir)
        }
    }

    @Test
    fun modelStringKeysExist() {
        val keys = stringKeys("values")
        val used = MeasurementKey.entries.map { it.labelKey } +
            Garment.entries.map { it.labelKey } +
            SourceBadge.entries.map { it.labelKey } +
            LengthPreset.entries.map { it.labelKey } +
            NeckDepthPreset.entries.map { it.labelKey } +
            Fit.entries.map { it.labelKey } +
            Gender.entries.map { it.labelKey } +
            (GarmentMeasurements.panjabiManualOrder + GarmentMeasurements.salwarKameez).mapNotNull { it.titleKey }
        assertEquals(emptyList(), used.filterNot { it in keys }, "keys missing in strings.xml")
    }

    @Test
    fun homeSampleStringKeysExist() {
        val keys = stringKeys("values")
        val used = HomeState.Sample.saved.mapNotNull { (it.detail as? SavedDetail.Value)?.labelKey } +
            (1..12).map { "month_$it" }
        assertEquals(emptyList(), used.filterNot { it in keys }, "keys missing in strings.xml")
    }

    @Test
    fun modelDrawableNamesExist() {
        val drawables = File(resDir, "drawable").listFiles()!!.map { it.nameWithoutExtension }.toSet()
        val used = Garment.entries.map { it.iconName } +
            listOf(Diagrams.panjabi, Diagrams.kameez, Diagrams.salwar).flatMap { listOf(it.outlineName, it.marksName) }
        assertEquals(emptyList(), used.filterNot { it in drawables }, "drawables missing")
    }

    @Test
    fun fontsAreBundled() {
        listOf("regular", "medium", "semibold", "bold").forEach {
            assertTrue(File(resDir, "font/hind_siliguri_$it.ttf").length() > 0, it)
        }
    }
}
