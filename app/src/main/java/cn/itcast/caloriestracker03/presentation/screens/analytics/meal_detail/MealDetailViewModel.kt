package cn.itcast.caloriestracker03.presentation.screens.analytics.meal_detail

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
import cn.itcast.caloriestracker03.presentation.navigation.Screen
import kotlinx.coroutines.launch


class MealDetailViewModel(
    private val mealRepository: MealRepositoryImpl,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var state by mutableStateOf(MealDetailState())
        private set

    private var meal: Meal? = null

    fun onEvent(event: MealDetailEvent) {
        viewModelScope.launch {
            savedStateHandle.get<Long>(Screen.MEAL_ID_ARG)?.let { id ->
                getMeal(id)
            } ?: throw Exception("MEAL ID wasn't passed")
        }
        when (event) {
            is MealDetailEvent.AddMealProduct -> {
                meal?.id?.let { id -> addMealProduct(event.mealProduct, id) }
            }
            is MealDetailEvent.ChangeMealProductGrams -> {
                state.productToEdit?.let { oldProduct ->
                    changeMealProductGrams(
                        oldProduct,
                        event.weight
                    )
                }
            }
            is MealDetailEvent.HideEditProductWeightDialog -> {
                state = state.copy(showEditProductWeightDialog = false)
            }
            is MealDetailEvent.ShowEditProductWeightDialog -> {
                state = state.copy(
                    showEditProductWeightDialog = true,
                    productToEdit = event.product
                )
            }

        }
    }


    private fun getMeal(id: Long) {
        viewModelScope.launch {
            mealRepository.getMealByIdFlow(id).collect { updatedMeal ->
                meal = updatedMeal
                state = state.copy(
                    mealProducts = updatedMeal.products,
                    timeSeconds = updatedMeal.timeSeconds,
                    name = updatedMeal.name,
                )
            }
        }
    }


    private fun addMealProduct(mealProduct: MealFoodProduct, mealId: Long) {
        viewModelScope.launch {
            mealRepository.addMealFoodProductToMeal(
                mealId = mealId,
                mealFoodProduct = mealProduct
            )
        }
    }


    private fun changeMealProductGrams(
        oldProduct: MealFoodProduct,
        grams: Float,
    ) {
        viewModelScope.launch {
            mealRepository.editMealFoodProductWeight(grams, oldProduct)
        }
    }
}

class MealDetailViewModelFactory(
    private val mealRepository: MealRepositoryImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (modelClass.isAssignableFrom(MealDetailViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()
            @Suppress("UNCHECKED_CAST")
            return MealDetailViewModel(mealRepository,savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
