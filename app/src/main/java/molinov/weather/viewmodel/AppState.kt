package molinov.weather.viewmodel

import molinov.weather.model.Weather

interface AppState {
    data class Success(val weatherData: List<Weather>) : AppState
    data class Error(val error: Throwable) : AppState
    object Loading : AppState
}