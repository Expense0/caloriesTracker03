package cn.itcast.caloriestracker03.presentation.screens.analytics.edit_meal

sealed class EditMealUiEvent {

    data class ShowSnackbar(val message: String) : EditMealUiEvent()

    object MealSaved : EditMealUiEvent()


}