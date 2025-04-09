package cn.itcast.caloriestracker03.presentation.screens.analytics

import cn.itcast.caloriestracker03.domain.model.meal.Meal

sealed class AnalyticsEvent {

    data class ChangeDate(val date: String) : AnalyticsEvent()

    data class ShowDatePickerDialog(val show: Boolean) : AnalyticsEvent()

    data class DeleteMeal(val meal: Meal) : AnalyticsEvent()

}
