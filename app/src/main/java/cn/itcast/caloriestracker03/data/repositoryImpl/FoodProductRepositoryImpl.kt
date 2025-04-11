package cn.itcast.caloriestracker03.data.repositoryImpl

import cn.itcast.caloriestracker03.data.remote.OpenFoodApiService
import cn.itcast.caloriestracker03.domain.repository.FoodProductRepository
import cn.itcast.caloriestracker03.domain.utils.Response
import cn.itcast.caloriestracker03.domain.model.food_product.FoodProductInfo
import cn.itcast.caloriestracker03.data.mapper.toListFoodProductInfo
import cn.itcast.caloriestracker03.domain.model.food_product.FoodProductInfoWithGram
import cn.itcast.caloriestracker03.domain.model.meal.MealFoodProduct
import cn.itcast.caloriestracker03.utils.HUNDRED_GRAMS


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

    fun getMealFoodProduct(
        imageUri: String
    ): MealFoodProduct {
        val foodProductInfoWithGram = openFoodApi.searchProduct(imageUri)
        val mealFoodProduct: MealFoodProduct = createMealFoodProduct(foodProductInfoWithGram)

        return  mealFoodProduct
    }

    private fun createMealFoodProduct(foodProductInfoWithGram: FoodProductInfoWithGram): MealFoodProduct {
        val foodProductInfo = foodProductInfoWithGram.foodProductInfo
        val grams = foodProductInfoWithGram.gram

        val calories = grams / HUNDRED_GRAMS * foodProductInfo.caloriesIn100Grams
        val fat = grams / HUNDRED_GRAMS * foodProductInfo.fatIn100Grams
        val proteins = grams / HUNDRED_GRAMS * foodProductInfo.proteinsIn100Grams
        val carbs = grams / HUNDRED_GRAMS * foodProductInfo.carbsIn100Grams

        return MealFoodProduct(
            name = foodProductInfo.name,
            grams = grams,
            cals = calories,
            proteins = proteins,
            fat = fat,
            carbs = carbs,
            caloriesIn100Grams = foodProductInfo.caloriesIn100Grams,
            proteinsIn100Grams = foodProductInfo.proteinsIn100Grams,
            carbsIn100Grams = foodProductInfo.carbsIn100Grams,
            fatIn100Grams = foodProductInfo.fatIn100Grams
        )
    }
}