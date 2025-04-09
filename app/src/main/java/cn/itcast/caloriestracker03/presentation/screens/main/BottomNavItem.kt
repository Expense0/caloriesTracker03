package cn.itcast.caloriestracker03.presentation.screens.main

import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.presentation.navigation.graph.Graph

sealed class BottomNavItem(
    val titleResId: Int,
    val iconResId: Int,
    val route: String
) {


    object Home: BottomNavItem(
        titleResId = R.string.nav_item_title_home,
        iconResId = R.drawable.home,
        route = Graph.HOME
    )

    object Analytics: BottomNavItem(
        titleResId = R.string.nav_item_title_analytics,
        iconResId = R.drawable.analytics,
        route = Graph.ANALYTICS
    )

    object Settings: BottomNavItem(
        titleResId = R.string.nav_item_title_settings,
        iconResId = R.drawable.settings,
        route = Graph.SETTINGS
    )


}
