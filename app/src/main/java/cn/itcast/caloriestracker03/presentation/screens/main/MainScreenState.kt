package cn.itcast.caloriestracker03.presentation.screens.main

sealed class MainScreenState {

    object Initial: MainScreenState()

    object OnBoarding: MainScreenState()

    object Main: MainScreenState()

}
