package cn.itcast.caloriestracker03.domain.utils

data class ValidationResult(
    val successful: Boolean,
    val errorMessageResId: Int? = null
)
