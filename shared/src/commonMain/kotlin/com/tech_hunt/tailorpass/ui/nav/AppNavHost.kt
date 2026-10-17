package com.tech_hunt.tailorpass.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.tab_home
import com.tech_hunt.tailorpass.ui.components.MaapScaffold
import com.tech_hunt.tailorpass.ui.components.TopBar
import com.tech_hunt.tailorpass.ui.screens.onboarding.LanguageAction
import com.tech_hunt.tailorpass.ui.screens.onboarding.LanguageScreen
import com.tech_hunt.tailorpass.ui.screens.onboarding.OnboardingAction
import com.tech_hunt.tailorpass.ui.screens.onboarding.OnboardingScreen
import com.tech_hunt.tailorpass.ui.screens.onboarding.OnboardingState
import com.tech_hunt.tailorpass.ui.screens.profile.ProfileCreateAction
import com.tech_hunt.tailorpass.ui.screens.profile.ProfileCreateScreen
import com.tech_hunt.tailorpass.ui.screens.profile.ProfileCreateState
import com.tech_hunt.tailorpass.ui.screens.profile.reduce
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
                    ProfileCreateAction.Save -> navController.navigate(MainRoute) {
                        popUpTo(LanguageRoute) { inclusive = true }
                    }
                    else -> state = state.reduce(action)
                }
            }
        }
        composable<MainRoute> {
            PlaceholderScreen(stringResource(Res.string.tab_home))
        }
    }
}

/** Stand-in for screens of later steps. */
@Composable
private fun PlaceholderScreen(title: String) {
    MaapScaffold(topBar = { TopBar(title = title) }) {}
}
