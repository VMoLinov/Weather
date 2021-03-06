package molinov.weather.utils

import molinov.weather.model.*
import molinov.weather.room.HistoryEntity

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.fact!!
    return Weather(
        getDefaultCity(),
        fact.temp!!,
        fact.feels_like!!,
        fact.condition!!,
        fact.icon!!,
        fact.pressure_mm!!,
        fact.wind_speed!!
    )
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(
            City(it.city, 0.0, 0.0),
            it.temperature,
            it.feels_like,
            it.condition,
            it.icon,
            it.pressure_mm,
            it.wind_speed
        )
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(
        0,
        weather.city.city,
        weather.temperature,
        weather.feelsLike,
        weather.condition,
        weather.icon,
        weather.pressure_mm,
        weather.wind_speed
    )
}
