package cn.itcast.caloriestracker03.data.remote

import cn.itcast.caloriestracker03.data.remote.model.ProductsSearchResponseDto
import cn.itcast.caloriestracker03.domain.model.food_product.FoodProductInfoWithGram
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OpenFoodApiService {

    @GET("cgi/search.pl?search_simple=1&json=1&action=process&fields=product_name,nutriments")
    suspend fun searchFood(
        @Query("search_terms") query: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 50,
    ): ProductsSearchResponseDto

    @POST("analyze")
    fun searchProduct(@Body imageUri: String): FoodProductInfoWithGram

    companion object {
        const val BASE_URL = "https://world.openfoodfacts.org/"

    }

}
