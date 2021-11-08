package my.com.covid.info.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "Countries")
@JsonClass(generateAdapter = true)
data class Countries(
    @PrimaryKey
    val name: String,
    @Embedded
    val All:AllData
)

@Entity(tableName = "country_data")
data class AllData(
    @PrimaryKey
    val confirmed: Long,
    val recovered: Long,
    val deaths: Long,
    val population: Long? = 0L,
    val life_expectancy: Double? = 0.0,
    val continent: String? = "",
    val location: String? = "",
    val capital_city: String? = "",
    val updated: String? = ""
)
