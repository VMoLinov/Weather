package molinov.weather.app

import android.app.Application
import androidx.room.Room
import molinov.weather.room.HistoryDao
import molinov.weather.room.HistoryDataBase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {

        private lateinit var appInstance: App
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        val history_dao by lazy {
            Room.databaseBuilder(
                appInstance.applicationContext,
                HistoryDataBase::class.java,
                DB_NAME
            )
                .allowMainThreadQueries()
                .build()
                .historyDao()
        }

        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
//                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance.applicationContext,
                            HistoryDataBase::class.java,
                            DB_NAME
                        )
                            .build()
                    }
                }
            }
            return db!!.historyDao()
        }
    }
}
