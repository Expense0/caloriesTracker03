package cn.itcast.caloriestracker03.data.repositoryImpl

import cn.itcast.caloriestracker03.domain.repository.CaloriesCounterRepository
import cn.itcast.caloriestracker03.domain.user.ActivityLevel
import cn.itcast.caloriestracker03.domain.user.Gender
import cn.itcast.caloriestracker03.domain.user.GoalType
import kotlin.math.roundToInt

class CaloriesCounterRepositoryImpl (
) : CaloriesCounterRepository {
    companion object{
        @Volatile
        private var instance: CaloriesCounterRepositoryImpl ?= null

        fun getInstance(): CaloriesCounterRepositoryImpl{
            return instance ?: synchronized(this) {
                instance ?: CaloriesCounterRepositoryImpl().also {
                    instance = it
                }
            }
        }
    }

    override suspend fun calculateCalories(
        weight: Float,
        height: Int,
        age: Int,
        activityLevel: ActivityLevel,
        goalType: GoalType,
        gender: Gender,
    ): Int {
        //Multiply BMR by ActivityLevelMultiplier
        val activityLevelMultiplier = when (activityLevel) {
            is ActivityLevel.Low -> 1.2
            is ActivityLevel.Medium -> 1.375
            is ActivityLevel.High -> 1.55
            is ActivityLevel.SuperHigh -> 1.725
            is ActivityLevel.Unknown -> 1.375
        }
        //bmr = basal metabolic rate
        val bmr = when (gender) {
            is Gender.Female -> {
                655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age)
            }
            is Gender.Male -> {
                66.5 + (13.75 * weight) + (5.003 * height) - (6.775 * age)
            }
            is Gender.Unknown -> 2250.0 //Average calories
        }
        //AMR = bmr multiplied by ActivityLevelMultiplier
        val amr = bmr * activityLevelMultiplier
        //To lose weight it's recommended to reduce 3850 calories per week OR 550 calories per day
        //To gain weight it's recommended to add 3850 calories per week OR 550 calories per day
        //To keep weight it's recommended to keep your BMR the same
        val caloriesAdjustment = when (goalType) {
            is GoalType.LoseWeight -> -550
            is GoalType.KeepWeight -> 0
            is GoalType.GainWeight -> 550
            is GoalType.Unknown -> 0
        }
        return (amr + caloriesAdjustment).toInt()
    }

    override suspend fun calculateCarbs(dailyCalories: Int, carbsRatio: Float): Float {
        return dailyCalories * carbsRatio / 4
    }

    override suspend fun calculateProteins(dailyCalories: Int, proteinsRatio: Float): Float {
        return dailyCalories * proteinsRatio / 4
    }

    override suspend fun calculateFat(dailyCalories: Int, fatRatio: Float): Float {
        return dailyCalories * fatRatio / 9
    }

    override suspend fun getCaloriesByNutrition(proteins: Float, fat: Float, carbs: Float): Int {
        return ((proteins * 4) + (fat * 9) + (carbs * 4)).roundToInt()
    }


}