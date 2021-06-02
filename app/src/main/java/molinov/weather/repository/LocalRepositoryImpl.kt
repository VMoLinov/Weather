package molinov.weather.repository

import molinov.weather.model.Weather
import molinov.weather.room.HistoryDao
import molinov.weather.utils.convertHistoryEntityToWeather
import molinov.weather.utils.convertWeatherToEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDao) : LocalRepository {

    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}