package cn.itcast.caloriestracker03.domain.repository

import cn.itcast.caloriestracker03.domain.user.ActivityLevel
import cn.itcast.caloriestracker03.domain.user.Gender
import cn.itcast.caloriestracker03.domain.user.GoalType


interface CaloriesCounterRepository {

    suspend fun calculateCalories(
        weight: Float,
        height: Int,
        age: Int,
        activityLevel: ActivityLevel,
        goalType: GoalType,
        gender: Gender,
    ): Int

    suspend fun calculateCarbs(
        dailyCalories: Int,
        carbsRatio: Float = 0.4f
    ): Float

    suspend fun calculateProteins(
        dailyCalories: Int,
        proteinsRatio: Float = 0.3f
    ): Float

    suspend fun calculateFat(
        dailyCalories: Int,
        fatRatio: Float = 0.3f
    ): Float


    suspend fun getCaloriesByNutrition(
        proteins: Float,
        fat: Float,
        carbs: Float
    ): Int

}