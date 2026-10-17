package com.tech_hunt.tailorpass.ui.screens.capture

import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.Gender
import com.tech_hunt.tailorpass.ui.components.LineStatus
import com.tech_hunt.tailorpass.ui.diagram.BodyOutline
import com.tech_hunt.tailorpass.ui.screens.prep.MeasureTarget
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CaptureStateTest {

    @Test
    fun cameraSamplesMatchTheDesigns() {
        assertEquals(CameraGuidance.MoveBack, CameraState.Sample.guidance)        // 5.1
        assertEquals(CameraGuidance.ArmsOut, CameraState.SampleFemale.guidance)   // 5.3
        assertEquals(CameraGuidance.TurnRight, CameraState.SampleSide.guidance)   // 5.4
        assertTrue(CameraState.Sample.guidance!!.warning)
        assertFalse(CameraState.SampleSide.guidance!!.warning)
        assertEquals(0, CameraState.Sample.step)
        assertEquals(1, CameraState.SampleSide.step)
        assertEquals(2, CameraState.Sample.stepCount)
    }

    @Test
    fun bothFrontShotsUseTheHintAndTheSideShotConfirmsTheFront() {
        assertEquals("cam_timer_hint", CameraState.Sample.timerHintKey)
        assertEquals("cam_timer_hint", CameraState.SampleFemale.timerHintKey)
        assertEquals("cam_side_timer_hint", CameraState.SampleSide.timerHintKey)
    }

    @Test
    fun outlineFollowsShotAndVariant() {
        val man = MeasureTarget(Garment.Shirt, Gender.Male, "x")
        val woman = MeasureTarget(Garment.Pants, Gender.Female, "x")
        assertEquals(BodyOutline.MaleFront, man.outline(CameraShot.Front))
        assertEquals(BodyOutline.MaleSide, man.outline(CameraShot.Side))
        assertEquals(BodyOutline.FemaleFront, woman.outline(CameraShot.Front))
        assertEquals(BodyOutline.FemaleSide, woman.outline(CameraShot.Side))
    }

    @Test
    fun voiceToggleOnlyChangesVoice() {
        val off = CameraState.Sample.reduce(CameraAction.SetVoice(false))
        assertFalse(off.voiceOn)
        assertEquals(CameraState.Sample.copy(voiceOn = false), off)
        assertEquals(CameraState.Sample, CameraState.Sample.reduce(CameraAction.Start))
        assertEquals(CameraState.Sample, CameraState.Sample.reduce(CameraAction.Close))
    }

    @Test
    fun processingSampleIs68PercentWithCalculationRunning() {
        val s = ProcessingState.Sample
        assertEquals(
            listOf(LineStatus.Done, LineStatus.Done, LineStatus.Current, LineStatus.Pending),
            ProcessingStep.entries.map { s.status(it) },
        )
        assertFalse(s.done)
    }

    @Test
    fun processingStepsAtTheLimits() {
        val t = MeasureTarget.SampleRahat
        assertEquals(LineStatus.Current, ProcessingState(t, 0).status(ProcessingStep.Body))
        assertEquals(LineStatus.Pending, ProcessingState(t, 0).status(ProcessingStep.Height))
        assertEquals(LineStatus.Done, ProcessingState(t, 25).status(ProcessingStep.Body))
        assertEquals(LineStatus.Current, ProcessingState(t, 25).status(ProcessingStep.Height))
        assertEquals(LineStatus.Current, ProcessingState(t, 99).status(ProcessingStep.Size))
        val finished = ProcessingState(t, 100)
        assertTrue(finished.done)
        assertTrue(ProcessingStep.entries.all { finished.status(it) == LineStatus.Done })
    }

    @Test
    fun problemSampleHasOneFailedCheck() {
        val s = PhotoProblemState.Sample
        assertEquals(CameraShot.Front, s.failedShot)
        assertEquals(listOf("photo_bad_light"), s.checks.filterNot { it.ok }.map { it.labelKey })
    }
}
