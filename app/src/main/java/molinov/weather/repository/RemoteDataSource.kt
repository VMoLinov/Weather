package molinov.weather.repository

import molinov.weather.BuildConfig
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

private const val REQUEST_API_KEY = "X-Yandex_API-key"

class RemoteDataSource {

    fun getWeatherDetails(requestLink: String, callback: Callback) {
        val builder: Request.Builder = Request.Builder().apply {
            header(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY)
            url(requestLink)
        }
        OkHttpClient().newCall(builder.build()).enqueue(callback)
    }
}