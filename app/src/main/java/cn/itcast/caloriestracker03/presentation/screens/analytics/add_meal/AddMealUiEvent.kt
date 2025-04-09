package cn.itcast.caloriestracker03.presentation.screens.analytics.add_meal

sealed class AddMealUiEvent {

    data class ShowSnackbar(val message: String) : AddMealUiEvent()

    object MealSaved : AddMealUiEvent()


}