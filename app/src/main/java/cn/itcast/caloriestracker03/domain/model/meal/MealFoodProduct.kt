package cn.itcast.caloriestracker03.domain.model.meal

import android.os.Parcelable
import cn.itcast.caloriestracker03.utils.UNKNOWN_ID
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class MealFoodProduct(
    val id: Long = UNKNOWN_ID,
    val mealId: Long = UNKNOWN_ID,
    val name: String,
    val grams: Float,
    val cals: Float = 0f,
    val proteins: Float = 0f,
    val carbs: Float = 0f,
    val fat: Float = 0f,
    val caloriesIn100Grams: Float,
    val proteinsIn100Grams: Float,
    val carbsIn100Grams: Float,
    val fatIn100Grams: Float,
) : Parcelable
