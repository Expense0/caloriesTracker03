package cn.itcast.caloriestracker03.domain.usecase.calorie_calculator_validation

import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.domain.user.ActivityLevel
import cn.itcast.caloriestracker03.domain.user.GoalType
import cn.itcast.caloriestracker03.domain.user.Gender
import cn.itcast.caloriestracker03.domain.utils.ValidationResult

class ValidateInfoForCalculation() {

    operator fun invoke(
        proteinsPercentage: Int,
        fatPercentage: Int,
        carbsPercentage: Int,
        activityLevel: ActivityLevel,
        gender: Gender,
        goalType: GoalType,
        weight: Float,
        height: Int,
        age: Int,
    ): ValidationResult {
        val sum = proteinsPercentage + fatPercentage + carbsPercentage
        if (sum != 100) {
            return ValidationResult(
                successful = false,
                errorMessageResId = R.string.error_pfc_ration
            )
        }
        if (activityLevel is ActivityLevel.Unknown) {
            return ValidationResult(
                successful = false,
                errorMessageResId = R.string.error_choose_activity_level
            )
        }
        if (gender is Gender.Unknown) {
            return ValidationResult(
                successful = false,
                errorMessageResId = R.string.error_choose_gender
            )
        }
        if (goalType is GoalType.Unknown) {
            return ValidationResult(
                successful = false,
                errorMessageResId = R.string.error_choose_goal
            )
        }
        if (weight <= 0f) {
            return ValidationResult(
                successful = false,
                errorMessageResId = R.string.error_weight
            )
        }
        if (height <= 0f) {
            return ValidationResult(
                successful = false,
                errorMessageResId = R.string.error_height
            )
        }
        if (age <= 0) {
            return ValidationResult(
                successful = false,
                errorMessageResId = R.string.error_age
            )
        }
        return ValidationResult(true)
    }

}