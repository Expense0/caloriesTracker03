package cn.itcast.caloriestracker03.domain.usecase.user_info_validation

import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.domain.user.GoalType
import cn.itcast.caloriestracker03.domain.utils.ValidationResult


class ValidateGoal() {

    operator fun invoke(goalType: GoalType): ValidationResult {
        if (goalType is GoalType.Unknown) {
            return ValidationResult(
                successful = false,
                errorMessageResId = R.string.error_choose_goal
            )
        }
        return ValidationResult(true)
    }

}