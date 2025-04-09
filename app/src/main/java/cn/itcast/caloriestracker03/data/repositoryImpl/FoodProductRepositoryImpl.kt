package cn.itcast.caloriestracker03.data.repositoryImpl

import cn.itcast.caloriestracker03.data.remote.OpenFoodApiService
import cn.itcast.caloriestracker03.domain.repository.FoodProductRepository
import cn.itcast.caloriestracker03.domain.utils.Response
import cn.itcast.caloriestracker03.domain.model.food_product.FoodProductInfo
import cn.itcast.caloriestracker03.data.mapper.toListFoodProductInfo



class FoodProductRepositoryImpl (
    private val openFoodApi: OpenFoodApiService,
) : FoodProductRepository {

    companion object{
        @Volatile
        private var instance: FoodProductRepositoryImpl ?= null

        fun getInstance(openFoodApiService: OpenFoodApiService): FoodProductRepositoryImpl{
            return instance ?: synchronized(this) {
                instance ?: FoodProductRepositoryImpl(openFoodApiService).also {
                    instance = it
                }
            }
        }
    }

    override suspend fun searchFeedProduct(
        query: String,
        page: Int,
        pageSize: Int,
    ): Response<List<FoodProductInfo>> {
        return try {
            val productsSearchResponse = openFoodApi.searchFood(
                query.trim(),
                page,
                pageSize
            )
            val products = productsSearchResponse.products.toListFoodProductInfo()
            Response.success(products)
        } catch (e: Exception) {
            Response.failed(e.message.toString())
        }
    }

}