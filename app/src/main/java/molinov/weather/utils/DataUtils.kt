package molinov.weather.utils

import molinov.weather.model.FactDTO
import molinov.weather.model.Weather
import molinov.weather.model.WeatherDTO
import molinov.weather.model.getDefaultCity

class DataUtils {

    fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
        val fact: FactDTO = weatherDTO.fact!!
        return Weather(getDefaultCity(), fact.temp!!, fact.feels_like!!, fact.condition!!, fact.icon)
    }
}