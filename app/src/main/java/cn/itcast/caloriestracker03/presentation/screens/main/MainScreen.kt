package cn.itcast.caloriestracker03.presentation.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import cn.itcast.caloriestracker03.presentation.navigation.rememberNavigationState
import cn.itcast.caloriestracker03.presentation.navigation.Screen
import cn.itcast.caloriestracker03.presentation.navigation.graph.Graph
import cn.itcast.caloriestracker03.presentation.navigation.graph.RootNavGraph
import cn.itcast.caloriestracker03.presentation.screens.analytics.AnalyticsViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.add_meal.AddMealViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.cameraScreen.CameraViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.edit_meal.EditMealViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.meal_detail.MealDetailViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.search_product.SearchProductViewModel
import cn.itcast.caloriestracker03.presentation.screens.home.HomeViewModel
import cn.itcast.caloriestracker03.presentation.screens.main.components.AppBottomBar
import cn.itcast.caloriestracker03.presentation.screens.onboarding.OnboardingViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.SettingsViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.calorie_calculator.CaloriesCalculatorViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.daily_calorie_intake.DailyCalorieIntakeViewModel

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
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
    val navState = rememberNavigationState()

    var bottomBarIsVisible by rememberSaveable { (mutableStateOf(true)) }

    val backStackEntry by navState.navHostController.currentBackStackEntryAsState()

    bottomBarIsVisible = when (backStackEntry?.destination?.route) {
        Screen.HomeScreen.fullRoute -> {
            true
        }
        Screen.SettingsScreen.fullRoute -> {
            true
        }
        Screen.AnalyticsScreen.fullRoute -> {
            true
        }
        else -> {
            false
        }
    }

    Scaffold(
        bottomBar = {
            if (bottomBarIsVisible) {
                AppBottomBar(
                    navItems = listOf(
                        BottomNavItem.Home,
                        BottomNavItem.Analytics,
                        BottomNavItem.Settings
                    ),
                    backStackEntry = backStackEntry,
                    onNavItemClick = { navItem ->
                        navState.navigateWithPopUpToStartDestination(navItem.route)
                    }
                )
            }
        }
    ) {
        val startGraphRoute = when (mainViewModel.state) {
//            is MainScreenState.OnBoarding -> Graph.ONBOARDING
            is MainScreenState.Main -> Graph.MAIN
            is MainScreenState.Initial -> Graph.MAIN
            is MainScreenState.OnBoarding -> Graph.ONBOARDING
        }
        RootNavGraph(
            modifier = Modifier.padding(it),
            navState = navState,
            startGraphRoute = startGraphRoute,
            onboardingViewModel = onboardingViewModel,
            homeViewModel = homeViewModel,
            analyticsViewModel = analyticsViewModel,
            dailyCalorieIntakeViewModel = dailyCalorieIntakeViewModel,
            caloriesCalculatorViewModel = caloriesCalculatorViewModel,
            settingsViewModel = settingsViewModel,
            addMealViewModel = addMealViewModel,
//            searchProductViewModel = searchProductViewModel,
            cameraViewModel = cameraViewModel,
            editMealViewModel = editMealViewModel,
            mealDetailViewModel = mealDetailViewModel
        )
    }
}