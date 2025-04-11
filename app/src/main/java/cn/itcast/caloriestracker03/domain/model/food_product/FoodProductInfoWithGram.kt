package cn.itcast.caloriestracker03.domain.model.food_product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class FoodProductInfoWithGram(
    val gram: Float,
    val foodProductInfo: FoodProductInfo
) : Parcelable