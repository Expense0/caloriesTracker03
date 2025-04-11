package cn.itcast.caloriestracker03.domain.repository

import cn.itcast.caloriestracker03.domain.model.food_product.FoodProductInfo
import cn.itcast.caloriestracker03.domain.utils.Response

interface FoodProductRepository {
    //挂起函数，是携程实现非阻塞异步操作的核心机制
    suspend fun searchFeedProduct(
        query: String,
        page: Int = 1,
        pageSize: Int = 50
    ): Response<List<FoodProductInfo>>



}