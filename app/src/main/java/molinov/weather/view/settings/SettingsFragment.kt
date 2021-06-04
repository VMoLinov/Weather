package molinov.weather.view.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_settings.*
import molinov.weather.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        preferences = activity?.getPreferences(Context.MODE_PRIVATE)!!
        initBoxes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun initBoxes() {
        binding.pressureBtn.isChecked = preferences.getBoolean(PRESSURE, false)
        binding.windSpeedBTN.isChecked = preferences.getBoolean(WIND_SPEED, false)
    }

    private fun setListeners() {
        binding.pressureBtn.setOnClickListener {
            if (pressureBtn.isChecked) {
                preferences.edit().putBoolean(PRESSURE, true).apply()
            } else preferences.edit().putBoolean(PRESSURE, false).apply()
        }
        binding.windSpeedBTN.setOnClickListener {
            if (windSpeedBTN.isChecked) {
                preferences.edit().putBoolean(WIND_SPEED, true).apply()
            } else preferences.edit().putBoolean(WIND_SPEED, false).apply()
        }
    }

    companion object {
        const val PRESSURE = "PRESSURE"
        const val WIND_SPEED = "WIND SPEED"
        fun newInstance() = SettingsFragment()
    }
}
