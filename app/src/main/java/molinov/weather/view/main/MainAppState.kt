package molinov.weather.view.main

import molinov.weather.model.Weather
import molinov.weather.viewmodel.AppState

sealed class MainAppState : AppState {
    data class Success(val weatherData: List<Weather>) : MainAppState()
    data class Error(val error: Throwable) : MainAppState()
    object Loading : MainAppState()
}
