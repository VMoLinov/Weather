package molinov.weather.room

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ID = "id"
const val CITY = "city"
const val TEMPERATURE = "temperature"

@Entity
data class HistoryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val city: String = "",
    val temperature: Int = 0,
    val feels_like: Int = 0,
    val condition: String = "",
    val icon: String = "",
    val pressure_mm: Int = 0,
    val wind_speed: Double = 0.0
)
