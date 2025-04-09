package cn.itcast.caloriestracker03.presentation.screens.settings.calorie_calculator

sealed class CalorieCalculatorUiEvent {

    data class ShowSnackbar(val message: String): CalorieCalculatorUiEvent()

}
