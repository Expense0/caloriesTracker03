package cn.itcast.caloriestracker03.presentation.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import cn.itcast.caloriestracker03.data.UserSettingsManager
import cn.itcast.caloriestracker03.data.repositoryImpl.UserRepositoryImpl
import kotlinx.coroutines.launch

class MainViewModel(
    userRepository: UserRepositoryImpl,
    userSettingsManager: UserSettingsManager
) : ViewModel() {

    var state by mutableStateOf<MainScreenState>(MainScreenState.Initial)
        private set

    val isDarkTheme = userSettingsManager.isDarkTheme

    init {
        viewModelScope.launch {
            userRepository.getShouldShowOnBoarding().collect { shouldShow ->
                state = if (shouldShow) {
                    MainScreenState.OnBoarding
                } else {
                    MainScreenState.Main
                }
                return@collect
            }
        }
    }

}

class MainViewModelFactory(
    private val userRepository: UserRepositoryImpl,
    private val userSettingsManager: UserSettingsManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(userRepository, userSettingsManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
