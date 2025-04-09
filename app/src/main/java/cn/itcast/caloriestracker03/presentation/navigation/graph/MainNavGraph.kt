package cn.itcast.caloriestracker03.presentation.navigation.graph

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import cn.itcast.caloriestracker03.presentation.navigation.NavigationState
import cn.itcast.caloriestracker03.presentation.navigation.Screen
import cn.itcast.caloriestracker03.presentation.navigation.ext.navigateForResult
import cn.itcast.caloriestracker03.presentation.navigation.ext.popBackStackWithResult
import cn.itcast.caloriestracker03.presentation.screens.analytics.AnalyticsScreen
import cn.itcast.caloriestracker03.presentation.screens.analytics.AnalyticsViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.add_meal.AddMealScreen
import cn.itcast.caloriestracker03.presentation.screens.analytics.add_meal.AddMealViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.meal_detail.MealDetailScreen
import cn.itcast.caloriestracker03.presentation.screens.analytics.meal_detail.MealDetailViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.search_product.SearchProductScreen
import cn.itcast.caloriestracker03.presentation.screens.home.HomeScreen
import cn.itcast.caloriestracker03.presentation.screens.home.HomeViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.SettingsScreen
import cn.itcast.caloriestracker03.presentation.screens.settings.SettingsViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.calorie_calculator.CalorieCalculatorScreen
import cn.itcast.caloriestracker03.presentation.screens.settings.calorie_calculator.CaloriesCalculatorViewModel
import cn.itcast.caloriestracker03.presentation.screens.settings.daily_calorie_intake.DailyCalorieIntakeScreen
import cn.itcast.caloriestracker03.presentation.screens.settings.daily_calorie_intake.DailyCalorieIntakeViewModel
import cn.itcast.caloriestracker03.utils.UNKNOWN_ID
import cn.itcast.caloriestracker03.utils.getCurrentDateString
import cn.itcast.caloriestracker03.presentation.screens.analytics.edit_meal.EditMealScreen
import cn.itcast.caloriestracker03.presentation.screens.analytics.edit_meal.EditMealViewModel
import cn.itcast.caloriestracker03.presentation.screens.analytics.search_product.SearchProductViewModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mainNavGraph(
    navState: NavigationState,
    homeViewModel: HomeViewModel,
    analyticsViewModel: AnalyticsViewModel,
    addMealViewModel: AddMealViewModel,
    searchProductViewModel: SearchProductViewModel,
    mealDetailViewModel: MealDetailViewModel,
    editMealViewModel: EditMealViewModel,
    dailyCalorieIntakeViewModel: DailyCalorieIntakeViewModel,
    caloriesViewModel: CaloriesCalculatorViewModel,
    settingsViewModel: SettingsViewModel
) {
    navigation(
        route = Graph.MAIN,
        startDestination = Graph.HOME
    ) {
        homeScreenNavGraph(
            navState = navState,
            homeViewModel = homeViewModel
        )
        analyticsScreenNavGraph(
            analyticsViewModel = analyticsViewModel,
            addMealViewModel = addMealViewModel,
            searchProductViewModel = searchProductViewModel,
            mealDetailViewModel = mealDetailViewModel,
            editMealViewModel = editMealViewModel,
            navState = navState
        )
        settingsScreenNavGraph(
            settingsViewModel = settingsViewModel,
            caloriesViewModel = caloriesViewModel,
            dailyCalorieIntakeViewModel = dailyCalorieIntakeViewModel,
            navState = navState
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeScreenNavGraph(
    homeViewModel: HomeViewModel,
    navState: NavigationState,
) {
    navigation(
        startDestination = Screen.HomeScreen.fullRoute,
        route = Graph.HOME
    ) {
        composable(
            route = Screen.HomeScreen.fullRoute
        ) {
            HomeScreen(homeViewModel)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.analyticsScreenNavGraph(
    analyticsViewModel: AnalyticsViewModel,
    addMealViewModel: AddMealViewModel,
    searchProductViewModel: SearchProductViewModel,
    mealDetailViewModel: MealDetailViewModel,
    editMealViewModel: EditMealViewModel,
    navState: NavigationState,
) {
    navigation(
        startDestination = Screen.AnalyticsScreen.fullRoute,
        route = Graph.ANALYTICS
    ) {
        composable(
            route = Screen.AnalyticsScreen.fullRoute
        ) {
            AnalyticsScreen(
                viewModel = analyticsViewModel,

                onNavigateToAddMeal = { date ->
                    navState.navigateTo(route = Screen.AddMealScreen.getRouteWithArgs(date))
                },
                onNavigateToEditMeal = { id ->
                    navState.navigateTo(route = Screen.EditMealScreen.getRouteWithArgs(id))
                },
                onNavigateToMealDetail = { id ->
                    navState.navigateTo(route = Screen.MealDetailScreen.getRouteWithArgs(id))
                }
            )
        }
        composable(
            route = Screen.AddMealScreen.fullRoute,
            arguments = listOf(
                navArgument(Screen.DATE_ARG) {
                    type = NavType.StringType
                    defaultValue = getCurrentDateString()
                }
            ),
            enterTransition = {
                slideInVertically(initialOffsetY = { it }, animationSpec = tween(500))
                    .plus(fadeIn(tween(500)))
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(500))
                    .plus(fadeOut(tween(500)))
            },
            popEnterTransition = {
                slideInVertically(initialOffsetY = { -it }, animationSpec = tween(500))
                    .plus(fadeIn(tween(500)))
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500))
                    .plus(fadeOut(tween(500)))
            }
        ) {
            AddMealScreen(
                viewModel = addMealViewModel,
                onNavigateBack = {
                    navState.navigateBack()
                },
                onNavigateToSearchFood = { callback ->
                    Log.d("MainNavGraph", "onNavigateToSearchFood called")
                    navState.navHostController.navigateForResult(
                        Screen.SearchProductScreen.fullRoute,
                        navResultCallback = callback
                    )
                }
            )
        }
        composable(
            route = Screen.SearchProductScreen.fullRoute,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            }
        ) {
            SearchProductScreen(
                viewModel = searchProductViewModel,
                onConfirm = { mealFoodProduct ->
                    // 添加日志：记录用户确认添加产品
                    Log.d("MainNavGraph", "User confirmed searching product: ${mealFoodProduct.name}")
                    navState.navHostController.popBackStackWithResult(mealFoodProduct)
                },
                onNavigateBack = {
                    // 添加日志：记录用户返回操作
                    Log.d("MainNavGraph", "User navigated back from SearchProductScreen")
                    navState.navigateBack()
                }
            )
        }
        composable(
            route = Screen.EditMealScreen.fullRoute,
            arguments = listOf(
                navArgument(Screen.MEAL_ID_ARG) {
                    type = NavType.LongType
                    defaultValue = UNKNOWN_ID
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            }
        ) {
            EditMealScreen(
                viewModel = editMealViewModel,
                onNavigateBack = {
                    navState.navigateBack()
                },
                onNavigateToSearchFood = { callback ->
                    navState.navHostController.navigateForResult(
                        Screen.SearchProductScreen.fullRoute,
                        navResultCallback = callback
                    )
                }
            )
        }
        composable(
            route = Screen.MealDetailScreen.fullRoute,
            arguments = listOf(
                navArgument(Screen.MEAL_ID_ARG) {
                    type = NavType.LongType
                    defaultValue = UNKNOWN_ID
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            }
        ) {
            MealDetailScreen(
                viewModel = mealDetailViewModel,
                onNavigateBack = {
                    navState.navigateBack()
                },
                onNavigateToSearchFood = { callback ->
                    navState.navHostController.navigateForResult(
                        Screen.SearchProductScreen.fullRoute,
                        navResultCallback = callback
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsScreenNavGraph(
    navState: NavigationState,
    dailyCalorieIntakeViewModel: DailyCalorieIntakeViewModel,
    caloriesViewModel: CaloriesCalculatorViewModel,
    settingsViewModel: SettingsViewModel
) {
    navigation(
        startDestination = Screen.SettingsScreen.fullRoute,
        route = Graph.SETTINGS
    ) {
        composable(
            route = Screen.SettingsScreen.fullRoute
        ) {
            SettingsScreen(
                onNavigateToOnboarding = {
                    navState.navigateTo(Graph.ONBOARDING)
                },
                onNavigateToCalorieIntake = {
                    navState.navigateTo(Screen.DailyCalorieIntakeScreen.fullRoute)
                },
                onNavigateToCalorieCalculator = {
                    navState.navigateTo(Screen.CalorieCalculatorScreen.fullRoute)
                },
                viewModel = settingsViewModel
            )
        }
        composable(
            route = Screen.CalorieCalculatorScreen.fullRoute,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            }
        ) {
            CalorieCalculatorScreen(
                viewModel = caloriesViewModel,
                onNavigateBack = {
                    navState.navigateBack()
                }
            )
        }
        composable(
            route = Screen.DailyCalorieIntakeScreen.fullRoute,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            }
        ) {
            DailyCalorieIntakeScreen(
                viewModel = dailyCalorieIntakeViewModel,
                onNavigateBack = {
                    navState.navigateBack()
                }
            )
        }
    }
}
