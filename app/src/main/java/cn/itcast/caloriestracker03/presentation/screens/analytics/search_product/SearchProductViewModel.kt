package cn.itcast.caloriestracker03.presentation.screens.analytics.search_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cn.itcast.caloriestracker03.data.repositoryImpl.FoodProductRepositoryImpl
import cn.itcast.caloriestracker03.domain.utils.Response
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class SearchProductViewModel(
    private val foodProductRepository: FoodProductRepositoryImpl,
) : ViewModel() {

    var state by mutableStateOf(SearchProductState())
        private set

    private val _uiEvent = MutableSharedFlow<SearchProductUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var getProductsJob: Job? = null

    fun onEvent(event: SearchProductEvent) {
        when (event) {
            is SearchProductEvent.ChangeQuery -> {
                state = state.copy(query = event.query)
                getProductsJob?.cancel()
                getProductsJob = viewModelScope.launch {
                    //这里添加相机权限获取与检查————————————————————————————————————

                    delay(200)
                    getProductInfos(event.query)
                }
            }

            is SearchProductEvent.ShowAddProductDialog -> {
                state = state.copy(
                    showAddProductDialog = true, selectedProduct = event.product
                )
            }

            is SearchProductEvent.HideAddProductDialog -> {
                state = state.copy(selectedProduct = null, showAddProductDialog = false)
            }
        }
    }


    private fun getProductInfos(query: String) {
        viewModelScope.launch {
            //这里添加相机调用逻辑——————————————————————————————————————————

            //原有业务逻辑
            state = state.copy(isLoading = true)
            state = when (val response = foodProductRepository.searchFeedProduct(query)) {
                is Response.Failed -> {
                    _uiEvent.emit(SearchProductUiEvent.ShowSnackbar(response.message))
                    state.copy(isLoading = false)
                }

                is Response.Success -> {
                    state.copy(products = response.data, isLoading = false)
                }
            }
        }
    }

}

class SearchProductViewModelFactory(
    private val foodProductRepository: FoodProductRepositoryImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return SearchProductViewModel(foodProductRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
