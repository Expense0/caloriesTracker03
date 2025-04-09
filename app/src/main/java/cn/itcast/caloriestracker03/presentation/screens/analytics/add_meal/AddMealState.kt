package cn.itcast.caloriestracker03.presentation.screens.analytics.add_meal

import cn.itcast.caloriestracker03.domain.model.meal.MealFoodProduct
import cn.itcast.caloriestracker03.utils.EMPTY_STRING
import cn.itcast.caloriestracker03.utils.getCurrentTimeSeconds

data class AddMealState(
    val mealProducts: List<MealFoodProduct> = listOf(),
    val timeSeconds: Long = getCurrentTimeSeconds(),
    val name: String = EMPTY_STRING,
    val showEditProductWeightDialog: Boolean = false,
    val productToEdit: MealFoodProduct? = null,
    val showTimePickerDialog: Boolean = false,
)
