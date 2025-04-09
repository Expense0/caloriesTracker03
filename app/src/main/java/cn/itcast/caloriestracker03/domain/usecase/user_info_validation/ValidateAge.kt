package cn.itcast.caloriestracker03.domain.usecase.user_info_validation

import cn.itcast.caloriestracker03.domain.utils.ValidationResult
import cn.itcast.caloriestracker03.R

class ValidateAge() {

    operator fun invoke(age: Int): ValidationResult {
        if (age in (10..110)) {
            return ValidationResult(
                successful = true,
            )
        }
        return ValidationResult(
            successful = false,
            errorMessageResId = R.string.error_specify_actual_age
        )
    }
}