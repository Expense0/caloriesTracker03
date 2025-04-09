package cn.itcast.caloriestracker03.domain.repository

import cn.itcast.caloriestracker03.domain.model.food_product.FoodProductInfo
import cn.itcast.caloriestracker03.domain.utils.Response

interface FoodProductRepository {
    suspend fun searchFeedProduct(
        query: String,
        page: Int = 1,
        pageSize: Int = 50
    ): Response<List<FoodProductInfo>>

}