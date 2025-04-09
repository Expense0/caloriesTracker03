package cn.itcast.caloriestracker03.data.localRoom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.itcast.caloriestracker03.data.localRoom.dao.MealDao
import cn.itcast.caloriestracker03.data.localRoom.model.MealEntity
import cn.itcast.caloriestracker03.data.localRoom.model.MealFoodProductEntity

@Database(
    entities = [
        MealEntity::class,
        MealFoodProductEntity::class,
    ],
    exportSchema = false,
    version = 1
)
abstract class DailyCaloriesDatabase : RoomDatabase() {

    abstract fun mealDao(): MealDao

    companion object {

        const val DB_NAME = "caloriesTracker03.db"

    }
}

object DatabaseProvider {
    private var instance: DailyCaloriesDatabase? = null

    fun getDatabase(context: Context): DailyCaloriesDatabase {
        if (instance == null) {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DailyCaloriesDatabase::class.java,
                        "caloriesTracker03.db"
                    ).build()
                }
            }
        }
        return instance!!
    }
}