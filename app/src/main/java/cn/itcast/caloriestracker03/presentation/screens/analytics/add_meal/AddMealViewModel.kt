package cn.itcast.caloriestracker03.presentation.screens.analytics.add_meal

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import cn.itcast.caloriestracker03.data.repositoryImpl.MealRepositoryImpl
import cn.itcast.caloriestracker03.domain.model.meal.Meal
import cn.itcast.caloriestracker03.domain.model.meal.MealFoodProduct
import cn.itcast.caloriestracker03.domain.usecase.meal_validation.ValidateMeal
import cn.itcast.caloriestracker03.presentation.navigation.Screen

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddMealViewModel(
    private val mealRepository: MealRepositoryImpl,
    private val validateMeal: ValidateMeal,
    private val application: Application,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var state by mutableStateOf(AddMealState())
        private set


    private val _uiEvent = MutableSharedFlow<AddMealUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    private var date: String? = LocalDate.now().toString()

//    init {
//        Log.d("AddMealViewModel", "SavedStateHandle: $savedStateHandle")
//        viewModelScope.launch {
//            savedStateHandle.get<String>(Screen.DATE_ARG)?.let { d ->
//                date = d
//            } ?: throw Exception("Date wasn't passed")
//        }
//    }

    init {
        date = savedStateHandle.get<String>(Screen.DATE_ARG)
        require(date != null) { "Date argument is required." }
    }

    fun onEvent(event: AddMealEvent) {
        when (event) {
            is AddMealEvent.AddMealProduct -> {
                addMealProduct(event.mealProduct)
            }
            is AddMealEvent.DeleteMealProduct -> {
                deleteMealProduct(event.product)
            }
            is AddMealEvent.ChangeMealProductGrams -> {
                changeMealProductGrams(event.mealProduct, event.weight)
            }
            is AddMealEvent.ChangeTime -> {
                state = state.copy(timeSeconds = event.time)
            }
            is AddMealEvent.SaveMeal -> {
                saveMeal(
                    name = state.name,
                    date = date.toString(),
                    timeSeconds = state.timeSeconds,
                    products = state.mealProducts
                )
            }
            is AddMealEvent.ChangeMealName -> {
                state = state.copy(name = event.name)
            }
            is AddMealEvent.HideEditProductWeightDialog -> {
                state = state.copy(showEditProductWeightDialog = false)
            }
            is AddMealEvent.ShowEditProductWeightDialog -> {
                state = state.copy(
                    showEditProductWeightDialog = true,
                    productToEdit = event.product
                )
            }
            is AddMealEvent.ShowTimePickerDialog -> {
                state = state.copy(showTimePickerDialog = event.show)
            }
        }
    }

    private fun saveMeal(
        name: String,
        date: String,
        timeSeconds: Long,
        products: List<MealFoodProduct>,
    ) {
        viewModelScope.launch {
            val calories = products.sumOf { it.cals.toDouble() }.toFloat()
            val carbs = products.sumOf { it.carbs.toDouble() }.toFloat()
            val fat = products.sumOf { it.fat.toDouble() }.toFloat()
            val proteins = products.sumOf { it.proteins.toDouble() }.toFloat()
            val meal = Meal(
                name = name.trim(),
                date = date,
                timeSeconds = timeSeconds,
                carbs = carbs,
                calories = calories,
                proteins = proteins,
                fat = fat,
                products = products
            )
            val validateResult = validateMeal.invoke(meal)
            if (validateResult.successful) {
                mealRepository.addMeal(meal)
                _uiEvent.emit(AddMealUiEvent.MealSaved)
                return@launch
            }
            if (validateResult.errorMessageResId != null) {
                _uiEvent.emit(
                    AddMealUiEvent.ShowSnackbar(
                        application.getString(
                            validateResult.errorMessageResId
                        )
                    )
                )
                return@launch
            }
            return@launch
        }
    }


    private fun addMealProduct(mealProduct: MealFoodProduct) {
        viewModelScope.launch {
            val newList = state.mealProducts.toMutableList().apply {
                add(mealProduct)
            }
            state = state.copy(mealProducts = newList)
        }
    }


    private fun deleteMealProduct(product: MealFoodProduct) {
        val newList = state.mealProducts.toMutableList().apply {
            remove(product)
        }
        state = state.copy(mealProducts = newList)
    }


    private fun changeMealProductGrams(product: MealFoodProduct, grams: Float) {
        val newList = state.mealProducts.toMutableList()
        val newItem = product.copy(grams = grams)
        val index = newList.indexOf(product)
        newList.removeAt(index)
        newList.add(index, newItem)
        state = state.copy(mealProducts = newList)
    }


}

class AddMealViewModelFactory(
    private val mealRepository: MealRepositoryImpl,
    private val validateMeal: ValidateMeal,
    private val application: Application,
//    private val savedStateHandle: SavedStateHandle,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (modelClass.isAssignableFrom(AddMealViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()

            savedStateHandle.get<String>(Screen.DATE_ARG)
                ?:savedStateHandle.set(Screen.DATE_ARG, LocalDate.now().toString())

            @Suppress("UNCHECKED_CAST")
            return AddMealViewModel(
                mealRepository,
                validateMeal,
                application,
                savedStateHandle
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}