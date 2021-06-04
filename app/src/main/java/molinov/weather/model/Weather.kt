package molinov.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0,
    val condition: String = "clear",
    val icon: String? = "bkn_n",
    val pressure_mm: Int = 0,
    val wind_speed: Double = 0.0
) : Parcelable

val conditionsMap = mapOf(
    "clear" to "ясно",
    "partly-cloudy" to "малооблачно",
    "cloudy" to "облачно с прояснениями",
    "overcast" to "пасмурно",
    "drizzle" to "морось",
    "light - rain" to "небольшой дождь",
    "rain" to "дождь",
    "moderate - rain" to "умеренно сильный дождь",
    "heavy - rain" to "сильный дождь",
    "continuous - heavy - rain" to "длительный сильный дождь",
    "showers" to "ливень",
    "wet - snow" to "дождь со снегом",
    "light - snow" to "небольшой снег",
    "snow" to "снег",
    "snow" to "showers — снегопад",
    "hail" to "град",
    "thunderstorm" to "гроза",
    "thunderstorm - with - rain" to "дождь с грозой",
    "thunderstorm - with - hail" to "гроза с градом"
)

fun getDefaultCity() = City("Москва", 55.755826, 37.617299900000035)

fun getWorldCities(): List<Weather> {
    return listOf(
        Weather(City("Лондон", 51.5085300, -0.1257400), 1, 2),
        Weather(City("Токио", 35.6895000, 139.6917100), 3, 4),
        Weather(City("Париж", 48.8534100, 2.3488000), 5, 6),
        Weather(City("Берлин", 52.52000659999999, 13.404953999999975), 7, 8),
        Weather(City("Рим", 41.9027835, 12.496365500000024), 9, 10),
        Weather(City("Минск", 53.90453979999999, 27.561524400000053), 11, 12),
        Weather(City("Стамбул", 41.0082376, 28.97835889999999), 13, 14),
        Weather(City("Вашингтон", 38.9071923, -77.03687070000001), 15, 16),
        Weather(City("Киев", 50.4501, 30.523400000000038), 17, 18),
        Weather(City("Пекин", 39.90419989999999, 116.40739630000007), 19, 20)
    )
}

fun getRussianCities(): List<Weather> {
    return listOf(
        Weather(City("Москва", 55.755826, 37.617299900000035), 1, 2),
        Weather(City("Санкт-Петербург", 59.9342802, 30.335098600000038), 3, 3),
        Weather(City("Новосибирск", 55.00835259999999, 82.93573270000002), 5, 6),
        Weather(City("Екатеринбург", 56.83892609999999, 60.60570250000001), 7, 8),
        Weather(City("Нижний Новгород", 56.2965039, 43.936059), 9, 10),
        Weather(City("Казань", 55.8304307, 49.06608060000008), 11, 12),
        Weather(City("Челябинск", 55.1644419, 61.4368432), 13, 14),
        Weather(City("Омск", 54.9884804, 73.32423610000001), 15, 16),
        Weather(City("Ростов-на-Дону", 47.2357137, 39.701505), 17, 18),
        Weather(City("Уфа", 54.7387621, 55.972055400000045), 19, 20)
    )
}
