package cn.itcast.caloriestracker03.domain.model.food_product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class FoodProductInfo(
    val name: String,
    val caloriesIn100Grams: Float,
    val proteinsIn100Grams: Float,
    val carbsIn100Grams: Float,
    val fatIn100Grams: Float,
) : Parcelable