package cn.itcast.caloriestracker03.presentation.screens.home

sealed class HomeEvent {

    data class ChangeDate(val date: String) : HomeEvent()

}
