package cn.itcast.caloriestracker03.presentation.screens.settings.calorie_calculator

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cn.itcast.caloriestracker03.data.repositoryImpl.CaloriesCounterRepositoryImpl
import cn.itcast.caloriestracker03.data.repositoryImpl.UserRepositoryImpl
import cn.itcast.caloriestracker03.domain.usecase.calorie_calculator_validation.ValidateInfoForCalculation
import cn.itcast.caloriestracker03.domain.user.ActivityLevel
import cn.itcast.caloriestracker03.domain.user.Gender
import cn.itcast.caloriestracker03.domain.user.GoalType
import cn.itcast.caloriestracker03.R
import cn.itcast.caloriestracker03.data.UserSettingsManager
import cn.itcast.caloriestracker03.presentation.screens.settings.SettingsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CaloriesCalculatorViewModel(
    private val calorieCounterRepository: CaloriesCounterRepositoryImpl,
    private val userRepository: UserRepositoryImpl,
    private val validateInfoForCalculation: ValidateInfoForCalculation,
    private val application: Application,
) : ViewModel() {

    var state by mutableStateOf(CalorieCalculatorState())
        private set

    private val _uiEvent = MutableSharedFlow<CalorieCalculatorUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    private var calculateCaloriesJob: Job? = null
    private var saveNutritionJob: Job? = null


    fun onEvent(event: CalorieCalculatorEvent) {
        when (event) {
            is CalorieCalculatorEvent.HeightTextChanged -> {
                state = state.copy(heightText = event.height)
            }
            is CalorieCalculatorEvent.WeightTextChanged -> {
                state = state.copy(weightText = event.weight)
            }
            is CalorieCalculatorEvent.AgeTextChanged -> {
                state = state.copy(ageText = event.age)
            }
            is CalorieCalculatorEvent.GenderChanged -> {
                state = state.copy(gender = event.gender)
            }
            is CalorieCalculatorEvent.ActivityLevelChanged -> {
                state = state.copy(activityLevel = event.activityLevel)
            }
            is CalorieCalculatorEvent.GoalTypeChanged -> {
                state = state.copy(goalType = event.goalType)
            }
            is CalorieCalculatorEvent.ProteinsRatioTextChanged -> {
                state = state.copy(proteinsRatioText = event.ratio)
            }
            is CalorieCalculatorEvent.CarbsRatioTextChanged -> {
                state = state.copy(carbsRatioText = event.ratio)
            }
            is CalorieCalculatorEvent.FatsRatioTextChanged -> {
                state = state.copy(fatsRatioText = event.ratio)
            }
            is CalorieCalculatorEvent.ShowResultDialog -> {
                state = state.copy(showResultDialog = event.show)
            }
            is CalorieCalculatorEvent.SaveNutrition -> {
                saveNutrition(event.carbs, event.proteins, event.fats, event.calories)
            }
            is CalorieCalculatorEvent.Calculate -> {
                calculateCalories(
                    proteinsRatio = state.proteinsRatioText.toIntOrNull() ?: 0,
                    fatRatio = state.fatsRatioText.toIntOrNull() ?: 0,
                    carbsRatio = state.carbsRatioText.toIntOrNull() ?: 0,
                    activityLevel = state.activityLevel,
                    gender = state.gender,
                    goalType = state.goalType,
                    weight = state.weightText.toFloatOrNull() ?: 0f,
                    height = state.heightText.toIntOrNull() ?: 0,
                    age = state.ageText.toIntOrNull() ?: 0
                )
            }
        }
    }


    private fun saveNutrition(
        carbs: Float,
        proteins: Float,
        fat: Float,
        calories: Int,
    ) {
        saveNutritionJob?.cancel()
        saveNutritionJob = viewModelScope.launch {
            val saveProteinsJob = async {
                userRepository.saveDailyProteins(proteins)
            }
            val saveFatJob = async {
                userRepository.saveDailyFat(fat)
            }
            val saveCarbsJob = async {
                userRepository.saveDailyCarbs(carbs)
            }
            val saveCaloriesJob = async {
                userRepository.saveDailyCalories(calories)
            }
            awaitAll(saveProteinsJob, saveFatJob, saveCarbsJob, saveCaloriesJob)
            _uiEvent.emit(CalorieCalculatorUiEvent.ShowSnackbar(application.getString(R.string.text_nutrition_saved)))
            return@launch
        }
    }


    private fun calculateCalories(
        proteinsRatio: Int,
        fatRatio: Int,
        carbsRatio: Int,
        activityLevel: ActivityLevel,
        gender: Gender,
        goalType: GoalType,
        weight: Float,
        height: Int,
        age: Int,
    ) {
        calculateCaloriesJob?.cancel()
        calculateCaloriesJob = viewModelScope.launch {
            val validationPfcRation = validateInfoForCalculation(
                proteinsRatio,
                fatRatio,
                carbsRatio,
                activityLevel,
                gender,
                goalType,
                weight,
                height,
                age
            )
            if (validationPfcRation.successful) {
                val calories = calorieCounterRepository.calculateCalories(
                    weight, height, age, activityLevel, goalType, gender
                )
                val proteins = calorieCounterRepository.calculateProteins(
                    calories,
                    proteinsRatio.toFloat() / 100f
                )
                val fat = calorieCounterRepository.calculateFat(
                    calories,
                    fatRatio.toFloat() / 100f
                )
                val carbs = calorieCounterRepository.calculateCarbs(
                    calories,
                    carbsRatio.toFloat() / 100f
                )
                state = state.copy(
                    calories = calories,
                    proteins = proteins.toInt(),
                    carbs = carbs.toInt(),
                    fats = fat.toInt(),
                    showResultDialog = true
                )
                return@launch
            }
            if (validationPfcRation.errorMessageResId != null) {
                _uiEvent.emit(
                    CalorieCalculatorUiEvent.ShowSnackbar(
                        application.getString(
                            validationPfcRation.errorMessageResId
                        )
                    )
                )
                return@launch
            }
            return@launch
        }
    }
}

class CaloriesCalculatorViewModelFactory(
    private val calorieCounterRepository: CaloriesCounterRepositoryImpl,
    private val userRepository: UserRepositoryImpl,
    private val validateInfoForCalculation: ValidateInfoForCalculation,
    private val application: Application,
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CaloriesCalculatorViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CaloriesCalculatorViewModel(
                calorieCounterRepository = calorieCounterRepository,
                userRepository = userRepository,
                validateInfoForCalculation = validateInfoForCalculation,
                application = application
            )as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}