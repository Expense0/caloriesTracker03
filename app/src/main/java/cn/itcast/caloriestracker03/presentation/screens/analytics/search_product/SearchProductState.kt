package cn.itcast.caloriestracker03.presentation.screens.analytics.search_product

import cn.itcast.caloriestracker03.domain.model.food_product.FoodProductInfo


data class SearchProductState(
    val query: String = "",
    val products: List<FoodProductInfo> = emptyList(),
    val showAddProductDialog: Boolean = false,
    val selectedProduct: FoodProductInfo? = null,
    val isLoading: Boolean = false
)
