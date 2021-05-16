package molinov.weather.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import molinov.weather.R
import molinov.weather.viewmodel.AppState
import molinov.weather.databinding.MainFragmentBinding
import molinov.weather.model.City
import molinov.weather.model.Weather
import molinov.weather.viewmodel.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var loadingLayout: FrameLayout
    private lateinit var mainView: ConstraintLayout
    private lateinit var currentCity: TextView
    private lateinit var coordinates: TextView
    private lateinit var temperature: TextView
    private lateinit var feelsLike: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingLayout = binding.loadingLayout
        mainView = binding.mainView
        currentCity = binding.currentCity
        coordinates = binding.coordinates
        temperature = binding.temperature
        feelsLike = binding.feelsLike
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalSource()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                loadingLayout.visibility = View.GONE
                mainView.visibility = View.VISIBLE
                setData(weatherData)
                Snackbar.make(mainView, "Success", Snackbar.LENGTH_LONG).show()
            }
            is AppState.Loading -> {
                loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
                mainView.visibility = View.GONE
                Snackbar
                    .make(mainView, "Load data error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getWeatherFromLocalSource() }
                    .show()
            }
        }
    }

    private fun setData(weatherData: Weather) {
        currentCity.text = weatherData.city.city
        coordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.lat.toString(),
            weatherData.city.lon.toString()
        )
        temperature.text = weatherData.temperature.toString()
        feelsLike.text = weatherData.feelsLike.toString()
    }
}
