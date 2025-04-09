package cn.itcast.caloriestracker03.presentation.screens.analytics.add_meal

import cn.itcast.caloriestracker03.domain.model.meal.MealFoodProduct

sealed class AddMealEvent {

    data class AddMealProduct(
        val mealProduct: MealFoodProduct,
    ) : AddMealEvent()

    data class ChangeMealProductGrams(
        val mealProduct: MealFoodProduct,
        val weight: Float,
    ) : AddMealEvent()

    data class DeleteMealProduct(val product: MealFoodProduct) : AddMealEvent()

    data class ChangeMealName(val name: String) : AddMealEvent()

    data class ChangeTime(val time: Long) : AddMealEvent()

    object SaveMeal : AddMealEvent()

    data class ShowEditProductWeightDialog(
        val product: MealFoodProduct,
    ) : AddMealEvent()


    object HideEditProductWeightDialog : AddMealEvent()


    data class ShowTimePickerDialog(
        val show: Boolean,
    ) : AddMealEvent()

}