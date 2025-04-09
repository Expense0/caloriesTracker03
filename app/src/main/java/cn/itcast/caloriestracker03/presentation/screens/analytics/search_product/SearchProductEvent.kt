package cn.itcast.caloriestracker03.presentation.screens.analytics.search_product

import cn.itcast.caloriestracker03.domain.model.food_product.FoodProductInfo

sealed class SearchProductEvent {


    data class ChangeQuery(val query: String) : SearchProductEvent()


    data class ShowAddProductDialog(
        val product: FoodProductInfo,
    ) : SearchProductEvent()


    object HideAddProductDialog : SearchProductEvent()

//    data class CameraPermissionResult(val granted: Boolean) : SearchProductEvent() // 新增权限结果事件

}
