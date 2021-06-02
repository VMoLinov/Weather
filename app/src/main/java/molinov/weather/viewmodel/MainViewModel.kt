package molinov.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import molinov.weather.repository.MainRepository
import molinov.weather.repository.MainRepositoryImpl
import molinov.weather.view.main.MainAppState

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<MainAppState> = MutableLiveData(),
    private val repositoryImpl: MainRepository = MainRepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(false)

    fun getWeatherFromRemoteSource() = getDataFromLocalSource(true)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = MainAppState.Loading
        Thread {
//            sleep(1000)
//            if (!Random.nextBoolean()) {
//                liveDataToObserve.postValue(AppState.Error(Exception()))
//            } else {
                liveDataToObserve.postValue(
                    MainAppState.Success(
                        if (isRussian) repositoryImpl.getWeatherFromLocalStorageRus()
                        else repositoryImpl.getWeatherFromLocalStorageWorld()
                    )
                )
//            }
        }.start()
    }
}
