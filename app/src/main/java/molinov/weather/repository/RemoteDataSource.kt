package molinov.weather.repository

import com.google.gson.GsonBuilder
import molinov.weather.BuildConfig
import molinov.weather.model.WeatherDTO
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val YANDEX_BASE_URL = "https://api.weather.yandex.ru/"

class RemoteDataSource {

    private val weatherApi = Retrofit.Builder()
        .baseUrl(YANDEX_BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(WeatherAPI::class.java)

    fun getWeatherDetails(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        weatherApi.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).enqueue(callback)
    }
}
