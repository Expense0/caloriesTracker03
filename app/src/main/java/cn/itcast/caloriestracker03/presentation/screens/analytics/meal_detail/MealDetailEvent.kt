package cn.itcast.caloriestracker03.presentation.screens.analytics.meal_detail

import cn.itcast.caloriestracker03.domain.model.meal.MealFoodProduct

sealed class MealDetailEvent {

    data class AddMealProduct(
        val mealProduct: MealFoodProduct,
    ) : MealDetailEvent()

    data class ChangeMealProductGrams(
        val weight: Float,
    ) : MealDetailEvent()

    data class ShowEditProductWeightDialog(
        val product: MealFoodProduct,
    ) : MealDetailEvent()


    object HideEditProductWeightDialog : MealDetailEvent()


}