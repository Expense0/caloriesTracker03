package cn.itcast.caloriestracker03.domain.usecase.user_info_validation

import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.domain.utils.ValidationResult


class ValidateHeight() {

    operator fun invoke(height: Int): ValidationResult {
        if (height in (101..250)) {
            return ValidationResult(true)
        }
        return ValidationResult(
            successful = false,
            errorMessageResId = R.string.error_specify_actual_height
        )
    }

}