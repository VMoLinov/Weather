package molinov.weather.view

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import molinov.weather.R
import molinov.weather.databinding.MainActivityWebviewBinding
import molinov.weather.view.main.MainFragment
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityWebviewBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ok.setOnClickListener(clickListener)
//        setContentView(R.layout.main_activity)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, MainFragment.newInstance())
//                .commitNow()
//        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private var clickListener = View.OnClickListener {
        try {
            val uri = URL(binding.url.text.toString())
            val handler = Handler(Looper.getMainLooper()) // Запоминаем основной поток
            Thread {
                var urlConnection: HttpsURLConnection? = null
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod =
                        "GET" // установка метода получения данных -- GET
                    urlConnection.readTimeout = 10000 // установка таймаута 10 секунд
                    val reader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream)) // Читаем данные в поток (бинар в стрим, в буффер)
                    val result = getLines(reader)
                    handler.post { // Возвращаемся к основному потоку
                        binding.webView.loadData(result, "text/html; charset=utf-8", "utf-8")
                    }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                } finally {
                    urlConnection?.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

//    object clickListener: View.OnClickListener {
//        var urlConnection: HttpsURLConnection? = null
//        try {
//            val uri = URL(binding.url.text.toString())
//        }
//    }
}
