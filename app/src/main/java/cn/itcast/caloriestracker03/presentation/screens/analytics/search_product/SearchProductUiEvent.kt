package cn.itcast.caloriestracker03.presentation.screens.analytics.search_product

sealed class SearchProductUiEvent {

    data class ShowSnackbar(val message: String) : SearchProductUiEvent()
//    object RequestCameraPermission : SearchProductUiEvent() // 新增权限请求事件

}
