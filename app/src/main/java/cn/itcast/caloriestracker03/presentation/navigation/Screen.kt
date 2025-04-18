package cn.itcast.caloriestracker03.presentation.navigation

sealed class Screen(protected val route: String, vararg params: String) {

    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }


    object HomeScreen : Screen(route = HOME_SCREEN_ROUTE)

    object AnalyticsScreen : Screen(route = ANALYTICS_SCREEN_ROUTE)

    object SettingsScreen : Screen(route = SETTINGS_SCREEN_ROUTE)

    object DailyCalorieIntakeScreen : Screen(route = DAILY_CALORIE_INTAKE_SCREEN_ROUTE)

    object CalorieCalculatorScreen: Screen(route = CALORIE_CALCULATOR_SCREEN_ROUTE)

    object OnboardingScreen : Screen(route = ONBOARDING_SCREEN_ROUTE)

//    object SearchProductScreen : Screen(route = SEARCH_PRODUCT_SCREEN_ROUTE)

    object CameraScreen : Screen(route = CAMERA_SCREEN_ROUTE)

    object AddMealScreen : Screen(route = ADD_MEAL_SCREEN_ROUTE, DATE_ARG) {
        fun getRouteWithArgs(date: String): String = route.appendParams(
            DATE_ARG to date
        )
    }

    object MealDetailScreen : Screen(route = MEAL_DETAIL_SCREEN_ROUTE, MEAL_ID_ARG) {
        fun getRouteWithArgs(mealId: Long): String = route.appendParams(
            MEAL_ID_ARG to mealId
        )
    }

    object EditMealScreen : Screen(route = EDIT_MEAL_SCREEN_ROUTE, MEAL_ID_ARG) {
        fun getRouteWithArgs(mealId: Long): String = route.appendParams(
            MEAL_ID_ARG to mealId
        )
    }


    companion object {

        //Screen routes
        private const val HOME_SCREEN_ROUTE = "home"
        private const val ANALYTICS_SCREEN_ROUTE = "analytics"
        private const val SETTINGS_SCREEN_ROUTE = "settings"
        private const val DAILY_CALORIE_INTAKE_SCREEN_ROUTE = "daily_calorie_intake"
        private const val CALORIE_CALCULATOR_SCREEN_ROUTE = "calorie_calculator"
        private const val ONBOARDING_SCREEN_ROUTE = "onboarding"

//        private const val SEARCH_PRODUCT_SCREEN_ROUTE = "search_product"
        private const val CAMERA_SCREEN_ROUTE = "camera_meal"
        private const val EDIT_MEAL_SCREEN_ROUTE = "edit_meal"
        private const val ADD_MEAL_SCREEN_ROUTE = "add_meal"
        private const val MEAL_DETAIL_SCREEN_ROUTE = "meal_detail"

        //Arguments
        const val MEAL_ID_ARG = "meal_id_arg"
        const val DATE_ARG = "date_arg"
    }


    internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
        val builder = StringBuilder(this)

        params.forEach {
            it.second?.toString()?.let { arg ->
                builder.append("/$arg")
            }
        }

        return builder.toString()
    }
//    internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
//        val builder = StringBuilder(this)
//        params.forEach {
//            it.second?.toString()?.let { arg ->
//                builder.append("/{${it.first}}")
//            }
//        }
//        return builder.toString()
//    }
}
