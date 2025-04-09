package cn.itcast.caloriestracker03.domain.model.meal

import android.os.Parcelable
import cn.itcast.caloriestracker03.utils.UNKNOWN_ID
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Meal(
    val id: Long = UNKNOWN_ID,
    val name: String,
    val date: String,
    val timeSeconds: Long,
    val calories: Float,
    val proteins: Float,
    val carbs: Float,
    val fat: Float,
    val products: List<MealFoodProduct>,
) : Parcelable
