package cn.itcast.caloriestracker03.domain.usecase.user_info_validation

import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.domain.user.Gender
import cn.itcast.caloriestracker03.domain.utils.ValidationResult


class ValidateGender() {

    operator fun invoke(gender: Gender): ValidationResult {
        if (gender is Gender.Unknown) {
            return ValidationResult(
                successful = false,
                R.string.error_choose_gender
            )
        }
        return ValidationResult(true)
    }

}