package my.com.covid.info.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class History(
    val All:Contents
)

data class Contents(
    val country: String? = "",
    val population: Long? = 1L,
    val sq_km_area: Long? = 1L,
    val life_expectancy: Double? = 0.0,
    val continent: String? = "",
    val abbreviation: String? = "",
    val location: String? = "",
    val capital_city: String? = "",
    val dates: Map<String, Long> = HashMap()
)