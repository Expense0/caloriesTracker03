package cn.itcast.caloriestracker03.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSettingsManager(
    context: Context,
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        PREFERENCES_DATASTORE_NAME
    )
    private val dataStore: DataStore<Preferences> = context.dataStore


    suspend fun setIsDarkTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME_KEY] = isDarkTheme
        }
    }

    val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_DARK_THEME_KEY] ?: false
    }


    companion object {

        private const val PREFERENCES_DATASTORE_NAME = "user_settings_pref"

        private val IS_DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme")

        @Volatile
        private var instance: UserSettingsManager? = null

        fun getInstance(context: Context): UserSettingsManager {
            return instance ?: synchronized(this) {
                instance ?: UserSettingsManager(context.applicationContext).also { instance = it }
            }
        }
    }

}