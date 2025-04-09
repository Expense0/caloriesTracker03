package cn.itcast.caloriestracker03.presentation.screens.analytics.edit_meal

import cn.itcast.caloriestracker03.domain.model.meal.MealFoodProduct

sealed class EditMealEvent {

    data class AddMealProduct(
        val mealProduct: MealFoodProduct,
    ) : EditMealEvent()

    data class ChangeMealProductGrams(
        val editedMealProduct: MealFoodProduct,
        val weight: Float,
    ) : EditMealEvent()

    data class DeleteMealProduct(val product: MealFoodProduct) : EditMealEvent()

    data class ChangeMealName(val name: String) : EditMealEvent()

    data class ChangeTime(val time: Long) : EditMealEvent()

    object SaveMeal : EditMealEvent()

    data class ShowEditProductWeightDialog(
        val product: MealFoodProduct,
    ) : EditMealEvent()


    object HideEditProductWeightDialog : EditMealEvent()


    data class ShowTimePickerDialog(
        val show: Boolean,
    ) : EditMealEvent()

}