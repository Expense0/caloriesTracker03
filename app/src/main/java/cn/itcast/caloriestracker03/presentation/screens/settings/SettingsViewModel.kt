package cn.itcast.caloriestracker03.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cn.itcast.caloriestracker03.data.UserSettingsManager
import kotlinx.coroutines.launch

class SettingsViewModel (
    private val userSettingsManager: UserSettingsManager,
) : ViewModel() {

    val isDarkTheme = userSettingsManager.isDarkTheme

    fun changeDarkTheme(darkTheme: Boolean) {
        viewModelScope.launch {
            userSettingsManager.setIsDarkTheme(darkTheme)
        }
    }
}

class SettingsViewModelFactory(
    private val userSettingsManager: UserSettingsManager
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(
                userSettingsManager
            )as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}