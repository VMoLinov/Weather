package molinov.weather.view

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import molinov.weather.R
import molinov.weather.databinding.MainActivityBinding
import molinov.weather.repository.content_provider.ContentProviderFragment
import molinov.weather.view.history.HistoryFragment
import molinov.weather.view.main.MainFragment
import molinov.weather.view.map.GoogleMapsFragment
import molinov.weather.view.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(R.id.container, HistoryFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.menu_settings -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.menu_content_provider -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(R.id.container, ContentProviderFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
                R.id.menu_google_maps -> {
                    supportFragmentManager.apply {
                        beginTransaction()
                            .replace(R.id.container, GoogleMapsFragment())
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                    true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
