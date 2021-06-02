package molinov.weather.repository

import molinov.weather.model.Weather
import molinov.weather.model.getRussianCities
import molinov.weather.model.getWorldCities

class MainRepositoryImpl : MainRepository {

    override fun getWeatherFromServer(): Weather = Weather()

    override fun getWeatherFromLocalStorageRus(): List<Weather> = getRussianCities()

    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getWorldCities()
}
