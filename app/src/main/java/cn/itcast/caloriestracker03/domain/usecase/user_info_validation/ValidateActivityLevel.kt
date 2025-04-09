package cn.itcast.caloriestracker03.domain.usecase.user_info_validation

import cn.itcast.caloriestracker03.domain.user.ActivityLevel
import cn.itcast.caloriestracker03.domain.utils.ValidationResult
import cn.itcast.caloriestracker03.R

class ValidateActivityLevel() {

    operator fun invoke(activityLevel: ActivityLevel): ValidationResult {
        if (activityLevel is ActivityLevel.Unknown) {
            return ValidationResult(
                successful = false,
                errorMessageResId = R.string.error_choose_activity_level
            )
        }
        return ValidationResult(true)
    }

}