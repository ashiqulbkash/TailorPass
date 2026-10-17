package com.tech_hunt.tailorpass.ui.screens.capture

import com.tech_hunt.tailorpass.ui.components.LineStatus
import com.tech_hunt.tailorpass.ui.diagram.BodyOutline
import com.tech_hunt.tailorpass.ui.screens.prep.MeasureTarget

/** The two photos, in order. */
enum class CameraShot(val stepKey: String) {
    Front("cam_front_step"),
    Side("cam_side_step"),
}

/** Body outline for a shot (front/side, male/female). */
fun MeasureTarget.outline(shot: CameraShot): BodyOutline = when (shot) {
    CameraShot.Front -> if (female) BodyOutline.FemaleFront else BodyOutline.MaleFront
    CameraShot.Side -> if (female) BodyOutline.FemaleSide else BodyOutline.MaleSide
}

/** Live guidance banner. warning = orange (fix something); otherwise a white instruction. */
enum class CameraGuidance(val iconName: String, val titleKey: String, val subtitleKey: String, val warning: Boolean) {
    MoveBack("ic_move_back", "cam_move_back", "cam_move_back_sub", warning = true),
    ArmsOut("ic_arms_out", "cam_arms_out", "cam_arms_out_sub", warning = true),
    TurnRight("ic_rotate_right", "cam_turn_right", "cam_turn_right_sub", warning = false),
}

// ---------- 5.1–5.4 Camera ----------

/**
 * Camera screen state. [guidance] null = nothing to fix (no banner). Design phase: the guidance is the designs' sample;
 * from Step 10.7/10.8 it comes from live frame analysis.
 */
data class CameraState(
    val target: MeasureTarget,
    val shot: CameraShot,
    val guidance: CameraGuidance?,
    val voiceOn: Boolean = true,
) {
    val step: Int get() = shot.ordinal
    val stepCount: Int get() = CameraShot.entries.size
    val outline: BodyOutline get() = target.outline(shot)

    /** Hint under the timer. Both front shots use the hint (user decision, Step 4); the side shot confirms the front one. */
    val timerHintKey: String
        get() = when (shot) {
            CameraShot.Front -> "cam_timer_hint"
            CameraShot.Side -> "cam_side_timer_hint"
        }

    companion object {
        /** Timer length in seconds ("১০ সেকেন্ডের টাইমার"). */
        const val TIMER_SECONDS = 10

        /** Design-phase guidance per shot: 5.1 move back (male), 5.3 arms out (female), 5.4 turn right. */
        fun initial(target: MeasureTarget, shot: CameraShot): CameraState = CameraState(
            target = target,
            shot = shot,
            guidance = when (shot) {
                CameraShot.Front -> if (target.female) CameraGuidance.ArmsOut else CameraGuidance.MoveBack
                CameraShot.Side -> CameraGuidance.TurnRight
            },
        )

        val Sample = initial(MeasureTarget.SampleRahat, CameraShot.Front)
        val SampleFemale = initial(MeasureTarget.SampleFarhana, CameraShot.Front)
        val SampleSide = initial(MeasureTarget.SampleRahat, CameraShot.Side)
    }
}

sealed interface CameraAction {
    data object Close : CameraAction
    data class SetVoice(val on: Boolean) : CameraAction
    data object Start : CameraAction
}

/** Pure reducer. Close/Start are handled by the caller (navigation; capture in Step 10.7). */
fun CameraState.reduce(action: CameraAction): CameraState = when (action) {
    is CameraAction.SetVoice -> copy(voiceOn = action.on)
    CameraAction.Close, CameraAction.Start -> this
}

// ---------- 6.1 Photo check — success and processing ----------

/** Processing steps (6.1), each done once [ProcessingState.progress] reaches its threshold. */
enum class ProcessingStep(val labelKey: String, val doneAt: Int) {
    Body("processing_step_body", 25),
    Height("processing_step_height", 50),
    Calculate("processing_step_calc", 75),
    Size("processing_step_size", 100),
}

/** [progress] 0–100 %. Steps: done up to the progress, the next one current, the rest pending. */
data class ProcessingState(val target: MeasureTarget, val progress: Int = 0) {
    val done: Boolean get() = progress >= 100

    fun status(step: ProcessingStep): LineStatus {
        val current = ProcessingStep.entries.firstOrNull { progress < it.doneAt }
        return when {
            progress >= step.doneAt -> LineStatus.Done
            step == current -> LineStatus.Current
            else -> LineStatus.Pending
        }
    }

    companion object {
        /** 6.1: 68 % — body and height done, calculating, size pending. */
        val Sample = ProcessingState(MeasureTarget.SampleRahat, progress = 68)
    }
}

sealed interface ProcessingAction {
    data object Back : ProcessingAction
    data object Cancel : ProcessingAction
}

// ---------- 6.2 Photo check — problem ----------

/** Why a photo was rejected. Only "too dark" has a design so far; others (blurry, cut off) follow the same shape. */
enum class PhotoProblem(val tagKey: String, val titleKey: String, val bodyKey: String) {
    TooDark("photo_low_light_tag", "photo_dark_title", "photo_dark_body"),
}

/** One photo check line: string key + passed or not. */
data class PhotoCheck(val labelKey: String, val ok: Boolean)

data class PhotoProblemState(
    val target: MeasureTarget,
    val failedShot: CameraShot,
    val problem: PhotoProblem,
    val checks: List<PhotoCheck>,
) {
    companion object {
        /** 6.2: front photo too dark; frame and posture fine. */
        val Sample = PhotoProblemState(
            target = MeasureTarget.SampleRahat,
            failedShot = CameraShot.Front,
            problem = PhotoProblem.TooDark,
            checks = listOf(
                PhotoCheck("photo_ok_frame", ok = true),
                PhotoCheck("photo_ok_straight", ok = true),
                PhotoCheck("photo_bad_light", ok = false),
            ),
        )
    }
}

sealed interface PhotoProblemAction {
    data object Back : PhotoProblemAction
    data object Retake : PhotoProblemAction
}

// ---------- 6.3 AI failed ----------

/** Possible reasons (6.3): drawable name + string key. */
data class FailReason(val iconName: String, val textKey: String)

object AiFailedReasons {
    val all = listOf(
        FailReason("ic_frame_out", "ai_failed_reason_frame"),
        FailReason("ic_garment_tshirt", "ai_failed_reason_loose"),
        FailReason("ic_sun", "ai_failed_reason_light"),
    )
}

sealed interface AiFailedAction {
    data object Close : AiFailedAction
    data object Retry : AiFailedAction
    data object UseTape : AiFailedAction
}
