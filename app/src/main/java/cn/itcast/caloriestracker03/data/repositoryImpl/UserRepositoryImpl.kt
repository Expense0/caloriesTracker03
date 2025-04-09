package cn.itcast.caloriestracker03.data.repositoryImpl

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import cn.itcast.caloriestracker03.domain.repository.UserRepository
import cn.itcast.caloriestracker03.domain.user.ActivityLevel
import cn.itcast.caloriestracker03.domain.user.Gender
import cn.itcast.caloriestracker03.domain.user.GoalType
import cn.itcast.caloriestracker03.domain.user.UserInfo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserRepositoryImpl private constructor(
    application: Application
) : UserRepository {
    companion object{
        @Volatile
        private var instance: UserRepositoryImpl ?= null

        fun getInstance(application: Application): UserRepositoryImpl{
            return instance ?: synchronized(this) {
                instance ?: UserRepositoryImpl(application).also {
                    instance = it
                }
            }
        }
    }

    private val Application.dataStore: DataStore<Preferences> by preferencesDataStore(
        "datastore.preferences"
    )
    private val dataStore: DataStore<Preferences> = application.dataStore

    override suspend fun saveGender(gender: Gender) {
        dataStore.edit { preferences ->
            preferences[UserRepository.KEY_GENDER] = gender.type
        }
    }

    override suspend fun saveAge(age: Int) {
        dataStore.edit { preferences ->
            preferences[UserRepository.KEY_AGE] = age
        }
    }

    override suspend fun saveWeight(weight: Float) {
        dataStore.edit { preferences ->
            preferences[UserRepository.KEY_WEIGHT] = weight
        }
    }

    override suspend fun saveHeight(height: Int) {
        dataStore.edit { preferences ->
            preferences[UserRepository.KEY_HEIGHT] = height
        }
    }

    override suspend fun saveActivityLevel(level: ActivityLevel) {
        dataStore.edit { preferences ->
            preferences[UserRepository.KEY_ACTIVITY_LEVEL] = level.type
        }
    }

    override suspend fun saveGoalType(type: GoalType) {
        dataStore.edit { preferences ->
            preferences[UserRepository.KEY_GOAL_TYPE] = type.type
        }
    }

    override suspend fun saveDailyCarbs(amount: Float) {
        dataStore.edit { preferences ->
            preferences[UserRepository.KEY_CARBS] = amount
        }
    }

    override suspend fun saveDailyProteins(amount: Float) {
        dataStore.edit { preferences ->
            preferences[UserRepository.KEY_PROTEINS] = amount
        }
    }

    override suspend fun saveDailyFat(amount: Float) {
        dataStore.edit { preferences ->
            preferences[UserRepository.KEY_FAT] = amount
        }
    }

    override suspend fun saveDailyCalories(amount: Int) {
        dataStore.edit { preferences ->
            preferences[UserRepository.KEY_CALORIES] = amount
        }
    }

    override suspend fun getUserInfo(): Flow<UserInfo> {
        return dataStore.data.map { preferences ->
            val gender = preferences[UserRepository.KEY_GENDER] ?: Gender.Unknown.type
            val goalType = preferences[UserRepository.KEY_GOAL_TYPE] ?: GoalType.Unknown.type
            val activityLevel =
                preferences[UserRepository.KEY_ACTIVITY_LEVEL] ?: ActivityLevel.Unknown.type
            val age = preferences[UserRepository.KEY_AGE] ?: 25
            val height = preferences[UserRepository.KEY_HEIGHT] ?: 170
            val weight = preferences[UserRepository.KEY_WEIGHT] ?: 70f
            val dailyCarbs = preferences[UserRepository.KEY_CARBS] ?: 0f
            val dailyProteins = preferences[UserRepository.KEY_PROTEINS] ?: 0f
            val dailyFat = preferences[UserRepository.KEY_FAT] ?: 0f
            val dailyCalories = preferences[UserRepository.KEY_CALORIES] ?: 0
            UserInfo(
                gender = Gender.fromString(gender),
                goalType = GoalType.fromString(goalType),
                activityLevel = ActivityLevel.fromString(activityLevel),
                age = age,
                height = height,
                weight = weight,
                dailyCarbs = dailyCarbs,
                dailyProteins = dailyProteins,
                dailyFat = dailyFat,
                dailyCals = dailyCalories
            )
        }
    }

    override suspend fun saveShouldShowOnBoarding(shouldShow: Boolean) {
        dataStore.edit { preferences ->
            preferences[UserRepository.KEY_SHOULD_SHOW_ON_BOARDING] = shouldShow
        }
    }

    override suspend fun getShouldShowOnBoarding(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[UserRepository.KEY_SHOULD_SHOW_ON_BOARDING] != false
        }
    }


}