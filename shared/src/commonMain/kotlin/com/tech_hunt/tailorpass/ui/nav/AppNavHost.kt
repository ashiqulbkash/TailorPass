package com.tech_hunt.tailorpass.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.tech_hunt.tailorpass.model.Garment
import com.tech_hunt.tailorpass.model.MeasureMethod
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.genji_title
import com.tech_hunt.tailorpass.resources.result_title
import com.tech_hunt.tailorpass.resources.settings_title
import com.tech_hunt.tailorpass.resources.size_chart_title
import com.tech_hunt.tailorpass.ui.components.BottomNav
import com.tech_hunt.tailorpass.ui.components.BottomTab
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.screens.capture.AiFailedAction
import com.tech_hunt.tailorpass.ui.screens.capture.AiFailedScreen
import com.tech_hunt.tailorpass.ui.screens.capture.CameraAction
import com.tech_hunt.tailorpass.ui.screens.capture.CameraScreen
import com.tech_hunt.tailorpass.ui.screens.capture.CameraShot
import com.tech_hunt.tailorpass.ui.screens.capture.CameraState
import com.tech_hunt.tailorpass.ui.screens.capture.PhotoCheckProblemScreen
import com.tech_hunt.tailorpass.ui.screens.capture.PhotoCheckSuccessScreen
import com.tech_hunt.tailorpass.ui.screens.capture.PhotoProblemAction
import com.tech_hunt.tailorpass.ui.screens.capture.PhotoProblemState
import com.tech_hunt.tailorpass.ui.screens.capture.ProcessingAction
import com.tech_hunt.tailorpass.ui.screens.capture.ProcessingState
import com.tech_hunt.tailorpass.ui.screens.home.HomeAction
import com.tech_hunt.tailorpass.ui.screens.home.IncompleteMeasurement
import com.tech_hunt.tailorpass.ui.screens.home.ProfileSummary
import com.tech_hunt.tailorpass.ui.screens.home.HomeScreen
import com.tech_hunt.tailorpass.ui.screens.home.HomeState
import com.tech_hunt.tailorpass.ui.screens.home.reduce
import com.tech_hunt.tailorpass.ui.screens.home.withIncomplete
import com.tech_hunt.tailorpass.ui.screens.home.withoutIncomplete
import com.tech_hunt.tailorpass.ui.screens.manual.ManualEntryAction
import com.tech_hunt.tailorpass.ui.screens.manual.ManualEntryScreen
import com.tech_hunt.tailorpass.ui.screens.manual.ManualListAction
import com.tech_hunt.tailorpass.ui.screens.manual.ManualListScreen
import com.tech_hunt.tailorpass.ui.screens.manual.ManualPrepAction
import com.tech_hunt.tailorpass.ui.screens.manual.ManualPrepScreen
import com.tech_hunt.tailorpass.ui.screens.manual.ManualState
import com.tech_hunt.tailorpass.ui.screens.manual.ManualSummaryAction
import com.tech_hunt.tailorpass.ui.screens.manual.ManualSummaryScreen
import com.tech_hunt.tailorpass.ui.screens.manual.MeasureUnit
import com.tech_hunt.tailorpass.ui.screens.manual.goTo
import com.tech_hunt.tailorpass.ui.screens.manual.reduce
import com.tech_hunt.tailorpass.ui.screens.onboarding.LanguageAction
import com.tech_hunt.tailorpass.ui.screens.onboarding.LanguageScreen
import com.tech_hunt.tailorpass.ui.screens.onboarding.OnboardingAction
import com.tech_hunt.tailorpass.ui.screens.onboarding.OnboardingScreen
import com.tech_hunt.tailorpass.ui.screens.onboarding.OnboardingState
import com.tech_hunt.tailorpass.ui.screens.prep.CameraPermissionAction
import com.tech_hunt.tailorpass.ui.screens.prep.CameraPermissionScreen
import com.tech_hunt.tailorpass.ui.screens.prep.ChecklistAction
import com.tech_hunt.tailorpass.ui.screens.prep.ChecklistScreen
import com.tech_hunt.tailorpass.ui.screens.prep.ChecklistState
import com.tech_hunt.tailorpass.ui.screens.prep.MeasureTarget
import com.tech_hunt.tailorpass.ui.screens.prep.MethodAction
import com.tech_hunt.tailorpass.ui.screens.prep.MethodScreen
import com.tech_hunt.tailorpass.ui.screens.prep.PoseGuideAction
import com.tech_hunt.tailorpass.ui.screens.prep.PoseGuideScreen
import com.tech_hunt.tailorpass.ui.screens.prep.reduce
import com.tech_hunt.tailorpass.ui.screens.profile.ProfileCreateAction
import com.tech_hunt.tailorpass.ui.screens.profile.ProfileCreateScreen
import com.tech_hunt.tailorpass.ui.screens.profile.ProfileCreateState
import com.tech_hunt.tailorpass.ui.screens.profile.reduce
import com.tech_hunt.tailorpass.ui.screens.verify.VerifyAction
import com.tech_hunt.tailorpass.ui.screens.verify.VerifyScreen
import com.tech_hunt.tailorpass.ui.screens.verify.VerifyState
import com.tech_hunt.tailorpass.ui.screens.verify.reduce
import com.tech_hunt.tailorpass.ui.util.str
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

/**
 * App navigation graph (navigation.json + plan.md §5 rule 7). First launch: Language → Onboarding → ProfileCreate → Main.
 * Until Step 10 there is no stored "onboarding seen" flag, so the app always starts at Language.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    onLanguageChosen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Design phase: Home sample data lives here (not in the Main entry) so it survives visits to other screens;
    // a ViewModel replaces it in Step 10.
    var homeState by remember { mutableStateOf(HomeState.Sample) }
    // Design phase: the real OS camera permission comes in Step 10.7; until then "Allow" on 3.2 counts as granted.
    var cameraAllowed by remember { mutableStateOf(false) }
    // Voice guide on/off for the whole camera flow (stored in settings from Step 10).
    var voiceOn by remember { mutableStateOf(true) }
    // Design-phase demo of the photo outcomes until the AI arrives (Step 10.8): the 1st photo pair is rejected (6.2),
    // the 2nd cannot be measured (6.3), later ones go on to Verify.
    var photoAttempt by remember { mutableIntStateOf(0) }
    // Design phase: the tape session is shared by its list, entry and summary screens; a ViewModel replaces it in
    // Step 10. Sessions stopped with "পরে করব" (7.8) wait here by profile and garment until continued from Home (7.9).
    var manualState by remember { mutableStateOf<ManualState?>(null) }
    var pausedSessions by remember { mutableStateOf(emptyMap<Pair<Long, Garment>, ManualState>()) }
    // Design phase: the measurement being verified (8.1–8.5), set just before Verify opens; a ViewModel replaces it in
    // Step 10.
    var verifyState by remember { mutableStateOf<VerifyState?>(null) }
    val onTabSelect: (BottomTab) -> Unit = { navController.navigateToTab(it) }

    NavHost(navController = navController, startDestination = LanguageRoute, modifier = modifier) {
        composable<LanguageRoute> {
            LanguageScreen { action ->
                when (action) {
                    is LanguageAction.Choose -> {
                        navController.navigate(OnboardingRoute)
                        onLanguageChosen(action.tag)
                    }
                }
            }
        }
        composable<OnboardingRoute> {
            OnboardingScreen(OnboardingState()) { action ->
                when (action) {
                    OnboardingAction.Skip, OnboardingAction.Start -> navController.navigate(ProfileCreateRoute)
                }
            }
        }
        composable<ProfileCreateRoute> {
            // Design phase: form state lives here with the design sample; a ViewModel replaces it in Step 10.
            var state by remember { mutableStateOf(ProfileCreateState.Sample) }
            ProfileCreateScreen(state) { action ->
                when (action) {
                    ProfileCreateAction.Back -> navController.popBackStack()
                    // Opened from Home's profile sheet: go back to Home. First launch: Home replaces the whole flow.
                    ProfileCreateAction.Save -> if (!navController.popBackStack(MainRoute, inclusive = false)) {
                        navController.navigate(MainRoute) { popUpTo(LanguageRoute) { inclusive = true } }
                    }
                    else -> state = state.reduce(action)
                }
            }
        }
        composable<MainRoute> {
            HomeScreen(homeState, onTabSelect) { action ->
                homeState = homeState.reduce(action)
                when (action) {
                    is HomeAction.StartMeasure -> navController.navigate(
                        if (action.garment.sizeOnly) GenjiResultRoute else MethodRoute(action.garment.name),
                    )
                    is HomeAction.ContinueIncomplete -> {
                        manualState = pausedSessions[homeState.currentProfileId to action.garment]
                            ?: ManualState(measureTarget(action.garment.name, homeState.currentProfile))
                        navController.navigate(ManualListRoute(action.garment.name))
                    }
                    is HomeAction.OpenSaved -> navController.navigate(ResultRoute(action.savedId))
                    // Design phase: a saved measurement's values are the design sample of its garment.
                    is HomeAction.EditSaved -> homeState.saved.firstOrNull { it.id == action.savedId }?.let {
                        verifyState = VerifyState.sampleAi(measureTarget(it.garment.name, homeState.currentProfile))
                        navController.navigate(VerifyRoute(action.savedId))
                    }
                    is HomeAction.RemeasureSaved -> homeState.saved.firstOrNull { it.id == action.savedId }?.let {
                        navController.navigate(MethodRoute(it.garment.name))
                    }
                    // Design phase: profile editing reuses the profile form (no edit design yet).
                    HomeAction.NewProfile, is HomeAction.EditProfile -> navController.navigate(ProfileCreateRoute)
                    // ShareSaved: real share in Step 10.5. Help: no destination in the design yet.
                    else -> Unit
                }
            }
        }
        composable<SizeChartRoute> {
            TabPlaceholder(stringResource(Res.string.size_chart_title), BottomTab.SizeChart, onTabSelect)
        }
        composable<SettingsRoute> {
            TabPlaceholder(stringResource(Res.string.settings_title), BottomTab.Settings, onTabSelect)
        }
        composable<MethodRoute> { entry ->
            val garment = entry.toRoute<MethodRoute>().garment
            MethodScreen(measureTarget(garment, homeState.currentProfile)) { action ->
                when (action) {
                    MethodAction.Back -> navController.popBackStack()
                    MethodAction.ChooseAi -> navController.navigate(ChecklistRoute(garment))
                    MethodAction.ChooseTape -> navController.navigate(ManualPrepRoute(garment))
                }
            }
        }
        composable<ChecklistRoute> { entry ->
            val garment = entry.toRoute<ChecklistRoute>().garment
            // Design phase: ticks live in this entry, saveable so they survive a visit to the pose guide and back
            // (reset when the flow is left); a ViewModel replaces it in Step 10.
            var checked by rememberSaveable { mutableStateOf(emptyList<Int>()) }
            val state = ChecklistState(measureTarget(garment, homeState.currentProfile), checked.toSet())
            ChecklistScreen(state) { action ->
                when (action) {
                    ChecklistAction.Back -> navController.popBackStack()
                    ChecklistAction.Continue -> navController.navigate(
                        if (cameraAllowed) PoseGuideRoute(garment) else CameraPermissionRoute(garment),
                    )
                    is ChecklistAction.Toggle -> checked = state.reduce(action).checked.sorted()
                }
            }
        }
        composable<CameraPermissionRoute> { entry ->
            val garment = entry.toRoute<CameraPermissionRoute>().garment
            CameraPermissionScreen { action ->
                when (action) {
                    CameraPermissionAction.Back -> navController.popBackStack()
                    // Back from the pose guide returns to the checklist, as in the design (4.3 → Checklist).
                    CameraPermissionAction.Allow -> {
                        cameraAllowed = true
                        navController.navigate(PoseGuideRoute(garment)) {
                            popUpTo(CameraPermissionRoute(garment)) { inclusive = true }
                        }
                    }
                    CameraPermissionAction.UseTape -> navController.navigate(ManualPrepRoute(garment))
                }
            }
        }
        composable<PoseGuideRoute> { entry ->
            val garment = entry.toRoute<PoseGuideRoute>().garment
            PoseGuideScreen(measureTarget(garment, homeState.currentProfile)) { action ->
                when (action) {
                    PoseGuideAction.Back -> navController.popBackStack()
                    PoseGuideAction.OpenCamera -> navController.navigate(CameraRoute(garment))
                }
            }
        }
        composable<CameraRoute> { entry ->
            val route = entry.toRoute<CameraRoute>()
            val garment = route.garment
            val shot = CameraShot.valueOf(route.shot)
            val state = CameraState.initial(measureTarget(garment, homeState.currentProfile), shot).copy(voiceOn = voiceOn)
            CameraScreen(state) { action ->
                when (action) {
                    CameraAction.Close -> navController.popBackStack(MainRoute, inclusive = false)
                    is CameraAction.SetVoice -> voiceOn = action.on
                    // Design phase: "start" stands for the timer + capture of Step 10.7.
                    CameraAction.Start -> when (shot) {
                        CameraShot.Front -> navController.navigate(CameraRoute(garment, CameraShot.Side.name))
                        CameraShot.Side -> {
                            photoAttempt += 1
                            navController.navigate(
                                if (photoAttempt == 1) PhotoProblemRoute(garment, CameraShot.Front.name) else PhotoCheckRoute(garment),
                            )
                        }
                    }
                }
            }
        }
        composable<PhotoProblemRoute> { entry ->
            val route = entry.toRoute<PhotoProblemRoute>()
            val failedShot = CameraShot.valueOf(route.failedShot)
            val state = PhotoProblemState.Sample.copy(
                target = measureTarget(route.garment, homeState.currentProfile),
                failedShot = failedShot,
            )
            PhotoCheckProblemScreen(state) { action ->
                when (action) {
                    PhotoProblemAction.Back -> navController.popBackStack()
                    PhotoProblemAction.Retake ->
                        navController.popBackStack(CameraRoute(route.garment, failedShot.name), inclusive = false)
                }
            }
        }
        composable<PhotoCheckRoute> { entry ->
            val garment = entry.toRoute<PhotoCheckRoute>().garment
            var progress by rememberSaveable { mutableIntStateOf(0) }
            // Design phase: simulated progress; the real on-device measuring (BodyMeasurer) comes in Step 10.8.
            // When done, the camera screens leave the back stack (photos are gone by then).
            LaunchedEffect(Unit) {
                while (progress < 100) {
                    delay(40)
                    progress += 1
                }
                if (photoAttempt != 2) verifyState = VerifyState.sampleAi(measureTarget(garment, homeState.currentProfile))
                navController.navigate(
                    if (photoAttempt == 2) AiFailedRoute(garment) else VerifyNewRoute(garment, MeasureMethod.Ai.name),
                ) {
                    popUpTo(MethodRoute(garment))
                }
            }
            PhotoCheckSuccessScreen(ProcessingState(measureTarget(garment, homeState.currentProfile), progress)) { action ->
                when (action) {
                    ProcessingAction.Back -> navController.popBackStack()
                    ProcessingAction.Cancel -> navController.popBackStack(MethodRoute(garment), inclusive = false)
                }
            }
        }
        composable<AiFailedRoute> { entry ->
            val garment = entry.toRoute<AiFailedRoute>().garment
            AiFailedScreen(measureTarget(garment, homeState.currentProfile)) { action ->
                when (action) {
                    AiFailedAction.Close -> navController.popBackStack(MainRoute, inclusive = false)
                    AiFailedAction.Retry -> navController.navigate(CameraRoute(garment)) { popUpTo(MethodRoute(garment)) }
                    AiFailedAction.UseTape -> navController.navigate(ManualPrepRoute(garment)) { popUpTo(MethodRoute(garment)) }
                }
            }
        }
        composable<VerifyNewRoute> { entry ->
            val garment = entry.toRoute<VerifyNewRoute>().garment
            val state = verifyState ?: VerifyState.sampleAi(measureTarget(garment, homeState.currentProfile))
            VerifyScreen(state) { action ->
                verifyState = state.reduce(action)
                when (action) {
                    // Photos: back to Method (the camera screens left the stack with the photos). Tape: the summary.
                    VerifyAction.Back -> navController.popBackStack()
                    VerifyAction.ShowResult -> navController.navigate(ResultNewRoute(garment))
                    else -> Unit
                }
            }
        }
        composable<ManualPrepRoute> { entry ->
            val garment = entry.toRoute<ManualPrepRoute>().garment
            var unit by rememberSaveable { mutableStateOf(MeasureUnit.Inch) }
            ManualPrepScreen(ManualState(measureTarget(garment, homeState.currentProfile), unit)) { action ->
                when (action) {
                    ManualPrepAction.Back -> navController.popBackStack()
                    is ManualPrepAction.SetUnit -> unit = action.unit
                    // A new tape session starts here; an unfinished one is continued from Home (7.9).
                    ManualPrepAction.Start -> {
                        manualState = ManualState(measureTarget(garment, homeState.currentProfile), unit)
                        navController.navigate(ManualListRoute(garment))
                    }
                }
            }
        }
        composable<ManualListRoute> { entry ->
            val garment = entry.toRoute<ManualListRoute>().garment
            val state = manualState ?: ManualState(measureTarget(garment, homeState.currentProfile))
            ManualListScreen(state) { action ->
                when (action) {
                    ManualListAction.Back -> navController.popBackStack()
                    is ManualListAction.Open -> {
                        manualState = state.goTo(action.index)
                        navController.navigate(ManualEntryRoute(garment))
                    }
                    // Back to the current measurement as it was left (a value typed but not yet saved stays).
                    ManualListAction.Continue -> navController.navigate(ManualEntryRoute(garment))
                }
            }
        }
        composable<ManualEntryRoute> { entry ->
            val garment = entry.toRoute<ManualEntryRoute>().garment
            val state = manualState ?: ManualState(measureTarget(garment, homeState.currentProfile))
            ManualEntryScreen(state) { action ->
                val next = state.reduce(action)
                manualState = next
                when (action) {
                    // The list is the entry's parent on the stack (list icon / "later" → ManualList). Back opens the
                    // 7.8 dialog once something was measured; with nothing to keep it goes straight to the list.
                    ManualEntryAction.Back ->
                        if (!next.exitOpen) navController.popBackStack(ManualListRoute(garment), inclusive = false)
                    ManualEntryAction.OpenList -> navController.popBackStack(ManualListRoute(garment), inclusive = false)
                    // A step opened from the summary returns there when saved or skipped.
                    ManualEntryAction.Later ->
                        if (next.finished) navController.navigate(ManualSummaryRoute(garment))
                        else navController.popBackStack(ManualListRoute(garment), inclusive = false)
                    ManualEntryAction.Next, ManualEntryAction.ConfirmValue ->
                        if (next.finished) navController.navigate(ManualSummaryRoute(garment))
                    // 7.8 "পরে করব": keep the session and show it on Home (7.9).
                    ManualEntryAction.StopForLater -> {
                        val profileId = homeState.currentProfileId
                        pausedSessions = pausedSessions + ((profileId to next.target.garment) to next)
                        homeState = homeState.withIncomplete(
                            IncompleteMeasurement(profileId, next.target.garment, next.doneCount, next.count),
                        )
                        navController.popBackStack(MainRoute, inclusive = false)
                    }
                    else -> Unit
                }
            }
        }
        composable<ManualSummaryRoute> { entry ->
            val garment = entry.toRoute<ManualSummaryRoute>().garment
            val state = manualState ?: ManualState(measureTarget(garment, homeState.currentProfile))
            ManualSummaryScreen(state) { action ->
                manualState = state.reduce(action)
                when (action) {
                    ManualSummaryAction.Back -> navController.popBackStack()
                    // The entry screen is below the summary; it now shows the chosen step.
                    is ManualSummaryAction.Edit ->
                        if (!navController.popBackStack(ManualEntryRoute(garment), inclusive = false)) {
                            navController.navigate(ManualEntryRoute(garment))
                        }
                    // Done measuring: the session is no longer unfinished on Home. Missing ones go to Verify as
                    // "the tailor measures it".
                    ManualSummaryAction.Verify, ManualSummaryAction.ContinueWithoutMissing -> {
                        val key = homeState.currentProfileId to Garment.valueOf(garment)
                        pausedSessions = pausedSessions - key
                        homeState = homeState.withoutIncomplete(key.first, key.second)
                        verifyState = VerifyState.fromTape(state)
                        navController.navigate(VerifyNewRoute(garment, MeasureMethod.Tape.name))
                    }
                }
            }
        }
        composable<GenjiResultRoute> {
            PlaceholderScreen(stringResource(Res.string.genji_title)) { navController.popBackStack() }
        }
        composable<ResultRoute> { entry ->
            val garment = homeState.saved.firstOrNull { it.id == entry.toRoute<ResultRoute>().savedId }?.garment
            PlaceholderScreen(stringResource(Res.string.result_title, garment?.let { str(it.labelKey) } ?: "")) {
                navController.popBackStack()
            }
        }
        composable<ResultNewRoute> { entry ->
            val garment = Garment.valueOf(entry.toRoute<ResultNewRoute>().garment)
            PlaceholderScreen(stringResource(Res.string.result_title, str(garment.labelKey))) { navController.popBackStack() }
        }
        composable<VerifyRoute> { entry ->
            val savedId = entry.toRoute<VerifyRoute>().savedId
            val garment = homeState.saved.firstOrNull { it.id == savedId }?.garment ?: Garment.Panjabi
            val state = verifyState ?: VerifyState.sampleAi(measureTarget(garment.name, homeState.currentProfile))
            VerifyScreen(state) { action ->
                verifyState = state.reduce(action)
                when (action) {
                    VerifyAction.Back -> navController.popBackStack()
                    VerifyAction.ShowResult -> navController.navigate(ResultRoute(savedId)) {
                        popUpTo(VerifyRoute(savedId)) { inclusive = true }
                    }
                    else -> Unit
                }
            }
        }
    }
}

/** Tab switch: one copy of each tab on the stack above Home, keeping each tab's state. */
private fun NavHostController.navigateToTab(tab: BottomTab) {
    val route: Any = when (tab) {
        BottomTab.Home -> MainRoute
        BottomTab.SizeChart -> SizeChartRoute
        BottomTab.Settings -> SettingsRoute
    }
    navigate(route) {
        popUpTo(MainRoute) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Stand-in for screens of later steps, with a back action. */
@Composable
private fun PlaceholderScreen(title: String, subtitle: String? = null, onBack: () -> Unit) {
    MaapScaffold(topBar = { TopBar(title = title, subtitle = subtitle, onBack = onBack) }) {}
}

/** Stand-in for the Size chart / Settings tabs (Step 9), with the bottom nav. */
@Composable
private fun TabPlaceholder(title: String, tab: BottomTab, onTabSelect: (BottomTab) -> Unit) {
    MaapScaffold(
        topBar = { TopBar(title = title) },
        bottomNav = { BottomNav(selected = tab, onSelect = onTabSelect) },
    ) {}
}

/** Measuring-flow target from a route argument (Garment.name) and Home's current profile. */
private fun measureTarget(garment: String, profile: ProfileSummary): MeasureTarget =
    MeasureTarget(Garment.valueOf(garment), profile.gender, profile.name, profile.heightInches)
