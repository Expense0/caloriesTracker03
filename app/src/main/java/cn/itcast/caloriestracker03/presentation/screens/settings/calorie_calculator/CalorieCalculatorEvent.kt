package cn.itcast.caloriestracker03.presentation.screens.settings.calorie_calculator

import cn.itcast.caloriestracker03.domain.user.ActivityLevel
import cn.itcast.caloriestracker03.domain.user.Gender
import cn.itcast.caloriestracker03.domain.user.GoalType

sealed class CalorieCalculatorEvent {

    data class ActivityLevelChanged(val activityLevel: ActivityLevel) : CalorieCalculatorEvent()

    data class GoalTypeChanged(val goalType: GoalType) : CalorieCalculatorEvent()

    data class GenderChanged(val gender: Gender) : CalorieCalculatorEvent()

    data class HeightTextChanged(val height: String) : CalorieCalculatorEvent()

    data class AgeTextChanged(val age: String) : CalorieCalculatorEvent()

    data class WeightTextChanged(val weight: String) : CalorieCalculatorEvent()

    data class ProteinsRatioTextChanged(val ratio: String) : CalorieCalculatorEvent()

    data class FatsRatioTextChanged(val ratio: String) : CalorieCalculatorEvent()

    data class CarbsRatioTextChanged(val ratio: String) : CalorieCalculatorEvent()

    data class ShowResultDialog(val show: Boolean) : CalorieCalculatorEvent()

    data class SaveNutrition(
        val calories: Int,
        val proteins: Float,
        val fats: Float,
        val carbs: Float,
    ) : CalorieCalculatorEvent()

    object Calculate : CalorieCalculatorEvent()

}