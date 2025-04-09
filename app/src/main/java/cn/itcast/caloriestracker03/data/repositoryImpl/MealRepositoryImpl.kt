package cn.itcast.caloriestracker03.data.repositoryImpl

import cn.itcast.caloriestracker03.data.localRoom.dao.MealDao
import cn.itcast.caloriestracker03.data.mapper.toMeal
import cn.itcast.caloriestracker03.data.mapper.toMealEntity
import cn.itcast.caloriestracker03.data.mapper.toMealFoodProductEntity
import cn.itcast.caloriestracker03.domain.model.meal.Meal
import cn.itcast.caloriestracker03.domain.model.meal.MealFoodProduct
import cn.itcast.caloriestracker03.domain.repository.MealRepository
import cn.itcast.caloriestracker03.utils.HUNDRED_GRAMS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MealRepositoryImpl (
    private val mealDao: MealDao,
) : MealRepository {
    companion object{
        @Volatile
        private var instance: MealRepositoryImpl ?= null

        fun getInstance(mealDao: MealDao): MealRepositoryImpl{
            return  instance ?: synchronized(this) {
                instance ?: MealRepositoryImpl(mealDao).also {
                    instance = it
                }
            }
        }
    }

    override fun getMeals(): Flow<List<Meal>> {
        return mealDao.getMealsWithMealFoodProducts().map { list ->
            list.map { mealWithProducts ->
                val entity = mealWithProducts.mealEntity
                val products = mealWithProducts.mealFoodProducts
                entity.toMeal(products)
            }
        }
    }

    override fun getMealsByDate(date: String): Flow<List<Meal>> {
        return mealDao.getMealsWithMealFoodProductsByDate(date).map { list ->
            list.map { mealWithProducts ->
                val entity = mealWithProducts.mealEntity
                val products = mealWithProducts.mealFoodProducts
                entity.toMeal(products)
            }
        }
    }

    override suspend fun getMealById(id: Long): Meal {
        val mealWithProducts = mealDao.getMealWithMealFoodProductsById(id)
        val entity = mealWithProducts.mealEntity
        val products = mealWithProducts.mealFoodProducts
        return entity.toMeal(products)
    }

    override suspend fun getMealByIdFlow(id: Long): Flow<Meal> {
        return mealDao.getMealWithMealFoodProductsByIdFlow(id) .map { mealWithProducts ->
            val entity = mealWithProducts.mealEntity
            val products = mealWithProducts.mealFoodProducts
            entity.toMeal(products)
        }
    }

    override suspend fun addMeal(meal: Meal) {
        val mealEntity = meal.toMealEntity()
        val mealFoodProductEntities = meal.products.map { it.toMealFoodProductEntity() }
        mealDao.createMeal(meal = mealEntity, mealFoodProducts = mealFoodProductEntities)
    }

    override suspend fun editMeal(meal: Meal) {
        val mealEntity = meal.toMealEntity()
        val productEntities = meal.products.map { it.toMealFoodProductEntity() }
        mealDao.deleteMealFoodProductsByMealId(meal.id)
        mealDao.insertMealFoodProducts(productEntities)
        mealDao.updateMeal(mealEntity)
    }

    override suspend fun deleteMeal(id: Long) {
        mealDao.deleteMeal(id)
    }

    override suspend fun addMealFoodProductToMeal(mealId: Long, mealFoodProduct: MealFoodProduct) {
        val mealProductEntity = mealFoodProduct.toMealFoodProductEntity().copy(mealId = mealId)
        mealDao.insertMealFoodProduct(mealProductEntity)
    }

    override suspend fun deleteMealFoodProduct(mealFoodProductId: Long) {
        mealDao.deleteMealFoodProduct(mealFoodProductId)
    }

    override suspend fun editMealFoodProductWeight(
        newGrams: Float,
        mealFoodProduct: MealFoodProduct,
    ) {
        val calories = newGrams / HUNDRED_GRAMS * mealFoodProduct.caloriesIn100Grams
        val carbs = newGrams / HUNDRED_GRAMS * mealFoodProduct.carbsIn100Grams
        val fat = newGrams / HUNDRED_GRAMS * mealFoodProduct.fatIn100Grams
        val protein = newGrams / HUNDRED_GRAMS * mealFoodProduct.proteinsIn100Grams
        val newMealFoodProduct = mealFoodProduct.copy(
            cals = calories,
            carbs = carbs,
            fat = fat,
            proteins = protein,
            grams = newGrams
        ).toMealFoodProductEntity()
        mealDao.updateMealFoodProduct(newMealFoodProduct)
    }

}