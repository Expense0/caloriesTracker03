package cn.itcast.caloriestracker03.domain.usecase.meal_validation

import cn.itcast.caloriestracker03.domain.model.meal.Meal
import cn.itcast.caloriestracker03.domain.utils.ValidationResult
import cn.itcast.caloriestracker03.R

class ValidateMeal() {

    operator fun invoke(meal: Meal): ValidationResult {
        if (meal.name.isBlank() && meal.name.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessageResId = R.string.error_name_is_empty
            )
        }
        if (meal.products.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessageResId = R.string.error_add_food_meal
            )
        }
        return ValidationResult(true)
    }

}