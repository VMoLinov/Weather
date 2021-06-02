package molinov.weather.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import molinov.weather.R
import molinov.weather.databinding.FragmentMainBinding
import molinov.weather.model.Weather
import molinov.weather.view.details.DetailsFragment
import molinov.weather.app.AppState
import molinov.weather.viewmodel.MainViewModel

private const val IS_WORLD_KEY = "LIST_OF_TOWNS_KEY"

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var isShowListRus = true
    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(BUNDLE_BOOLEAN, isShowListRus)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        savedInstanceState?.let { isShowListRus = it.getBoolean(BUNDLE_BOOLEAN) }
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecycleView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        activity?.let {
            showListOfTowns(
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY, true)
            )
        }
    }


    private fun changeWeatherDataSet() {
        showListOfTowns(!isShowListRus)
        isShowListRus = !isShowListRus
        saveListOfTowns(isShowListRus)
    }

    private fun showListOfTowns(boolean: Boolean) {
        val fab = binding.mainFragmentFAB
        if (boolean) {
            viewModel.getWeatherFromLocalSourceRus()
            fab.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            fab.setImageResource(R.drawable.ic_earth)
        }
    }

    private fun saveListOfTowns(showListRus: Boolean) {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, showListRus)
                apply()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.apply {
                    includedLoadingLayout.loadingLayout.visibility = View.GONE
                    mainFragmentRecycleView.visibility = View.VISIBLE
                }
                adapter.setWeather(appState.weatherData as List<Weather>)
            }
            is AppState.Loading -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.apply {
                    includedLoadingLayout.loadingLayout.visibility = View.GONE
                    mainFragmentRecycleView.visibility = View.GONE
                    mainFragmentRootView.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { showListOfTowns(isShowListRus) }
                    )
                }
            }
        }
    }

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length)
            .setAction(actionText, action)
            .show()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

    companion object {
        const val BUNDLE_BOOLEAN = "main"
        fun newInstance() = MainFragment()
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }
}
