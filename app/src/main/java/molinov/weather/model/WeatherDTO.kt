package molinov.weather.model

data class WeatherDTO(
    val fact: FactDTO?
)

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val condition: String?,
    val icon: String?,
    val pressure_mm: Int?,
    val wind_speed: Double
)
