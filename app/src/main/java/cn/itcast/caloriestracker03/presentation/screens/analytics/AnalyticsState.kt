package cn.itcast.caloriestracker03.presentation.screens.analytics

import cn.itcast.caloriestracker03.domain.model.meal.Meal
import cn.itcast.caloriestracker03.utils.getCurrentDateString

data class AnaliyticsState(
    val meals: List<Meal> = emptyList(),
    val date: String = getCurrentDateString(),
    val showDatePickerDialog: Boolean = false,
    //Consumed
    val cals: Float = 0.0f,
    val proteins: Float = 0.0f,
    val carbs: Float = 0.0f,
    val fat: Float = 0.0f,
    //Should be consumed in total
    val dailyCals: Float = 0.0f,
    val dailyProteins: Float = 0.0f,
    val dailyCarbs: Float = 0.0f,
    val dailyFat: Float = 0.0f,
)