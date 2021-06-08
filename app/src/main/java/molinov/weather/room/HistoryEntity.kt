package molinov.weather.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val temperature: Int,
    val feels_like: Int,
    val condition: String,
    val icon: String,
    val pressure_mm: Int,
    val wind_speed: Double
)
