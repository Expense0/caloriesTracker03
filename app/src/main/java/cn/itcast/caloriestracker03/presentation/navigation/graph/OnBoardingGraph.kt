package cn.itcast.caloriestracker03.presentation.navigation.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import cn.itcast.caloriestracker03.presentation.navigation.NavigationState
import cn.itcast.caloriestracker03.presentation.navigation.Screen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import cn.itcast.caloriestracker03.presentation.screens.onboarding.OnboardingScreen
import cn.itcast.caloriestracker03.presentation.screens.onboarding.OnboardingViewModel

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.onBoardingGraph(
    viewModel: OnboardingViewModel,
    navState: NavigationState
) {
    navigation(
        startDestination = Screen.OnboardingScreen.fullRoute,
        route = Graph.ONBOARDING
    ) {
        composable(
            route = Screen.OnboardingScreen.fullRoute
        ) {
            OnboardingScreen(
                viewModel = viewModel,
                onNavigateForward = {
                    navState.navigateAndSetNewStartDestination(
                        Graph.MAIN,
                        Graph.MAIN
                    )
                }
            )
        }
    }
}