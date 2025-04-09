package cn.itcast.caloriestracker03.presentation.screens.onboarding

import cn.itcast.caloriestracker03.domain.user.ActivityLevel
import cn.itcast.caloriestracker03.domain.user.Gender
import cn.itcast.caloriestracker03.domain.user.GoalType


sealed class OnboardingEvent {

    data class SaveAge(val age: Int) : OnboardingEvent()

    data class SaveGoal(val goal: GoalType) : OnboardingEvent()

    data class SaveWeight(val weight: Float) : OnboardingEvent()

    data class SaveHeight(val height: Int) : OnboardingEvent()

    data class SaveGender(val gender: Gender) : OnboardingEvent()

    data class SaveActivityLevel(val activityLevel: ActivityLevel) : OnboardingEvent()

    data class SaveDailyCaloriesIntake(
        val fat: Float,
        val proteins: Float,
        val carbs: Float,
        val calories: Int,
    ): OnboardingEvent()


}
