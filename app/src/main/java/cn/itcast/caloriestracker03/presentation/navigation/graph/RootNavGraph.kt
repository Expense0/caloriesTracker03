package cn.itcast.caloriestracker03.presentation.navigation.graph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.itcast.caloriestracker03.presentation.navigation.NavigationState
import cn.itcast.caloriestracker03.presentation.navigation.rememberNavigationState
import cn.itcast.caloriestracker03.presentation.screens.analytics.AnalyticsViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.add_meal.AddMealViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.cameraScreen.CameraViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.edit_meal.EditMealViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.meal_detail.MealDetailViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.search_product.SearchProductViewModel
import cn.itcast.caloriestracker03.presentation.screens.home.HomeViewModel
import cn.itcast.caloriestracker03.presentation.screens.onboarding.OnboardingViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.SettingsViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.calorie_calculator.CaloriesCalculatorViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.daily_calorie_intake.DailyCalorieIntakeViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    startGraphRoute: String,
    navState: NavigationState = rememberNavigationState(),
    onboardingViewModel: OnboardingViewModel,
    homeViewModel: HomeViewModel,
    analyticsViewModel: AnalyticsViewModel,
    addMealViewModel: AddMealViewModel,
//    searchProductViewModel: SearchProductViewModel,
    cameraViewModel: CameraViewModel,
    editMealViewModel: EditMealViewModel,
    mealDetailViewModel: MealDetailViewModel,
    dailyCalorieIntakeViewModel: DailyCalorieIntakeViewModel,
    caloriesCalculatorViewModel: CaloriesCalculatorViewModel,
    settingsViewModel: SettingsViewModel
) {
    AnimatedNavHost(
        modifier = modifier,
        navController = navState.navHostController,
        route = Graph.ROOT,
        startDestination = startGraphRoute,
    ) {
        onBoardingGraph(
            viewModel = onboardingViewModel,
            navState = navState
        )
        mainNavGraph(
            navState = navState,
            homeViewModel = homeViewModel,
            analyticsViewModel = analyticsViewModel,
            dailyCalorieIntakeViewModel = dailyCalorieIntakeViewModel,
            caloriesViewModel = caloriesCalculatorViewModel,
            settingsViewModel = settingsViewModel,
            addMealViewModel = addMealViewModel,
            cameraViewModel = cameraViewModel,
//            searchProductViewModel = searchProductViewModel,
            editMealViewModel = editMealViewModel,
            mealDetailViewModel = mealDetailViewModel
        )
    }

}

object Graph {
    //ROOT
    const val ROOT = "root_graph"

    //Main Graphs
    const val MAIN = "main_graph"
    const val ONBOARDING = "onboarding_graph"

    //Bottom Navigation Graphs
    const val SETTINGS = "settings_graph"
    const val HOME = "home_graph"
    const val ANALYTICS = "analytics_graph"
}