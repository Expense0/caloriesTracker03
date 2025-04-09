package cn.itcast.caloriestracker03.presentation.screens.settings.daily_calorie_intake

sealed class DailyCalorieIntakeEvent {

    data class ChangedProteins(val proteins: Float): DailyCalorieIntakeEvent()

    data class ChangedFats(val fat: Float): DailyCalorieIntakeEvent()

    data class ChangedCarbs(val carbs: Float): DailyCalorieIntakeEvent()

    data class ChangedCalories(val calories: Int): DailyCalorieIntakeEvent()

}
