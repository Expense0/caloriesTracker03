package cn.itcast.caloriestracker03.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cn.itcast.caloriestracker03.data.repositoryImpl.MealRepositoryImpl
import cn.itcast.caloriestracker03.data.repositoryImpl.UserRepositoryImpl
import cn.itcast.caloriestracker03.presentation.screens.analytics.AnalyticsViewModel
import kotlinx.coroutines.*

class HomeViewModel(
    private val mealRepository: MealRepositoryImpl,
    private val userRepository: UserRepositoryImpl,
) : ViewModel() {

    private var getMealsJob: Job? = null


    var state by mutableStateOf(HomeState())
        private set

    init {
        getMeals(state.date)
        getDailyNutrition()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ChangeDate -> {
                if (event.date == state.date) return
                getMeals(date = event.date)
            }
        }
    }

    private fun getMeals(date: String) {
        getMealsJob?.cancel()
        getMealsJob = viewModelScope.launch {
            mealRepository.getMealsByDate(date).collect { meals ->
                var carbs = 0f
                var fat = 0f
                var proteins = 0f
                var calories = 0f
                val carbsJob = async {
                    carbs = meals.sumOf { it.carbs.toDouble() }.toFloat()
                }
                val fatJob = async {
                    fat = meals.sumOf { it.fat.toDouble() }.toFloat()
                }
                val proteinsJob = async {
                    proteins = meals.sumOf { it.proteins.toDouble() }.toFloat()
                }
                val kCalsJob = async {
                    calories = meals.sumOf { it.calories.toDouble() }.toFloat()
                }
                awaitAll(carbsJob, fatJob, proteinsJob, kCalsJob)
                state = state.copy(
                    meals = meals,
                    carbs = carbs,
                    fat = fat,
                    cals = calories,
                    proteins = proteins,
                    date = date
                )
            }
        }
    }

    private fun getDailyNutrition() {
        viewModelScope.launch {
            userRepository.getUserInfo().collect { userInfo ->
                val dailyKCals = userInfo.dailyCals
                val dailyProteins = userInfo.dailyProteins
                val dailyCarbs = userInfo.dailyCarbs
                val dailyFat = userInfo.dailyFat
                state = state.copy(
                    dailyCals = dailyKCals.toFloat(),
                    dailyFat = dailyFat,
                    dailyProteins = dailyProteins,
                    dailyCarbs = dailyCarbs
                )
            }
        }
    }
}

class HomeViewModelFactory(
    private val mealRepository: MealRepositoryImpl,
    private val userRepository: UserRepositoryImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(mealRepository,userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
