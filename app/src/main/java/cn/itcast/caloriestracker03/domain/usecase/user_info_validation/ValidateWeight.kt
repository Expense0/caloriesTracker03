package cn.itcast.caloriestracker03.domain.usecase.user_info_validation

import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.domain.utils.ValidationResult


class ValidateWeight() {

    operator fun invoke(weight: Float): ValidationResult {

        if (weight in (20f..499f)) {
            return ValidationResult(true)
        }
        return ValidationResult(
            successful = false,
            errorMessageResId = R.string.error_specify_actual_weight
        )
    }

}