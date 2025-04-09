package cn.itcast.caloriestracker03.presentation

import androidx.compose.runtime.Composable

data class TabItem(
    val title: String,
    val screen: @Composable () -> Unit,
)
