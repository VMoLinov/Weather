package molinov.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import molinov.weather.R
import molinov.weather.databinding.FragmentDetailsBinding
import molinov.weather.model.Weather
import molinov.weather.utils.showSnackBar
import molinov.weather.viewmodel.AppState
import molinov.weather.viewmodel.DetailsViewModel

private const val PROCESS_ERROR = "Обработка ошибки"
private const val REQUEST_API_KEY = "X-Yandex-API-Key"
private const val MAIN_LINK = "https://api.weather.yandex.ru/v2/informers?"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        viewModel.getWeatherFromRemoteSource(
            MAIN_LINK +
                    "lat=${weatherBundle.city.lat}&lon= ${weatherBundle.city.lon}"
        )
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                setWeather(appState.weatherData[0])
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        viewModel.getWeatherFromRemoteSource(
                            MAIN_LINK +
                                    "lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}"
                        )
                    })
            }
        }
    }

    private fun setWeather(weather: Weather) {
        val city = weatherBundle.city
        binding.currentCity.text = city.city
        binding.coordinates.text = String.format(
            getString(R.string.city_coordinates),
            city.lat.toString(),
            city.lon.toString()
        )
        binding.temperature.text = weather.temperature.toString()
        binding.feelsLike.text = weather.feelsLike.toString()
        binding.weatherCondition.text = weather.condition
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
