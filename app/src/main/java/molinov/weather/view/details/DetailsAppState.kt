package molinov.weather.view.details

import molinov.weather.model.Weather
import molinov.weather.viewmodel.AppState

sealed class DetailsAppState : AppState {
    data class Success(val weatherData: Weather) : DetailsAppState()
    data class Error(val throwable: Throwable) : DetailsAppState()
    object Loading : DetailsAppState()
}