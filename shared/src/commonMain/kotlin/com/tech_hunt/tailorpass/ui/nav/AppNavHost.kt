package com.tech_hunt.tailorpass.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.cam_front_step
import com.tech_hunt.tailorpass.resources.genji_title
import com.tech_hunt.tailorpass.resources.manual_prep_title
import com.tech_hunt.tailorpass.resources.result_title
import com.tech_hunt.tailorpass.resources.settings_title
import com.tech_hunt.tailorpass.resources.size_chart_title
import com.tech_hunt.tailorpass.resources.verify_title
import com.tech_hunt.tailorpass.ui.components.BottomNav
import com.tech_hunt.tailorpass.ui.components.BottomTab
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.screens.home.HomeAction
import com.tech_hunt.tailorpass.ui.screens.home.ProfileSummary
import com.tech_hunt.tailorpass.ui.screens.home.HomeScreen
import com.tech_hunt.tailorpass.ui.screens.home.HomeState
import com.tech_hunt.tailorpass.ui.screens.home.reduce
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
import com.tech_hunt.tailorpass.ui.util.str
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
                    is HomeAction.OpenSaved -> navController.navigate(ResultRoute(action.savedId))
                    is HomeAction.EditSaved -> navController.navigate(VerifyRoute(action.savedId))
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
        composable<CameraRoute> {
            PlaceholderScreen(stringResource(Res.string.cam_front_step)) { navController.popBackStack() }
        }
        composable<ManualPrepRoute> {
            PlaceholderScreen(stringResource(Res.string.manual_prep_title)) { navController.popBackStack() }
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
        composable<VerifyRoute> {
            PlaceholderScreen(stringResource(Res.string.verify_title)) { navController.popBackStack() }
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
    MeasureTarget(Garment.valueOf(garment), profile.gender, profile.name)
