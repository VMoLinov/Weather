package molinov.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import molinov.weather.model.FactDTO
import molinov.weather.model.Weather
import molinov.weather.model.WeatherDTO
import molinov.weather.model.getDefaultCity
import molinov.weather.repository.DetailsRepository
import molinov.weather.repository.DetailsRepositoryImpl
import molinov.weather.repository.RemoteDataSource
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    fun getLiveData() = detailsLiveData

    fun getWeatherFromRemoteSource(requestLink: String) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(requestLink, callback)
    }

    private val callback = object : Callback {

        override fun onResponse(call: Call, response: Response) {
            val serverResponse: String? = response.body()?.string()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call, e: IOException) {
            detailsLiveData.postValue(AppState.Error(Throwable(e?.message ?: REQUEST_ERROR)))
        }
    }

    private fun checkResponse(serverResponse: String): AppState {
        val weatherDTO: WeatherDTO = Gson().fromJson(serverResponse, WeatherDTO::class.java)
        val fact = weatherDTO.fact
        return if (fact?.temp == null || fact.feels_like == null || fact.condition.isNullOrEmpty()) {
            AppState.Error(Throwable(CORRUPTED_DATA))
        } else {
            AppState.Success(convertDtoToModel(weatherDTO))
        }
    }

    private fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
        val fact: FactDTO = weatherDTO.fact!!
        return listOf(Weather(getDefaultCity(), fact.temp!!, fact.feels_like!!, fact.condition!!))
    }
}
