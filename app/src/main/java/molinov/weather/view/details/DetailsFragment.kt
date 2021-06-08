package molinov.weather.view.details

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.fragment_details.*
import molinov.weather.R
import molinov.weather.databinding.FragmentDetailsBinding
import molinov.weather.model.City
import molinov.weather.model.Weather
import molinov.weather.utils.showSnackBar
import molinov.weather.app.AppState
import molinov.weather.model.conditionsMap
import molinov.weather.view.settings.SettingsFragment
import molinov.weather.viewmodel.DetailsViewModel

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
        viewModel.detailsLiveData.observe(viewLifecycleOwner, {
            renderData(it)
        })
        viewModel.getWeatherFromRemoteSource(weatherBundle.city.lat, weatherBundle.city.lon)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                setWeather(appState.weatherData as Weather)
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        viewModel.getWeatherFromRemoteSource(
                            weatherBundle.city.lat,
                            weatherBundle.city.lon
                        )
                    })
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setWeather(weather: Weather) {
        val city = weatherBundle.city
        saveCity(city, weather)
        binding.currentCity.text = city.city
        binding.coordinates.text = String.format(
            getString(R.string.city_coordinates),
            city.lat.toString(),
            city.lon.toString()
        )
        binding.weatherCondition.text = conditionsMap[weather.condition]
        binding.temperature.text = weather.temperature.toString() + TEMPERATURE_CONST
        binding.feelsLike.text = weather.feelsLike.toString() + TEMPERATURE_CONST
        headerIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
        weather.icon.let {
            GlideToVectorYou.justLoadImage(
                activity,
                Uri.parse("https://yastatic.net/weather/i/icons/blueye/color/svg/$it.svg"),
                weatherIcon
            )
        }
        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            if (sharedPreferences.getBoolean(SettingsFragment.PRESSURE, false)) {
                binding.pressure.text = weather.pressure_mm.toString() + PRESSURE_CONST
                binding.pressureLabel.visibility = View.VISIBLE
            }
            if (sharedPreferences.getBoolean(SettingsFragment.WIND_SPEED, false)) {
                binding.windSpeed.text = weather.wind_speed.toString() + WIND_SPEED_CONST
                binding.windSpeedLabel.visibility = View.VISIBLE
            }
        }
    }

    private fun saveCity(city: City, weather: Weather) {
        viewModel.saveCityToDB(
            Weather(
                city,
                weather.temperature,
                weather.feelsLike,
                weather.condition,
                weather.icon,
                weather.pressure_mm,
                weather.wind_speed
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PRESSURE_CONST = "мм"
        const val WIND_SPEED_CONST = "м/с"
        const val TEMPERATURE_CONST = "\u2103"
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
