package my.com.covid.info.dbs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import my.com.covid.info.models.Countries

@Dao
interface AppDao {

    @Query("SELECT * from Countries")
    suspend fun getAllCountries(): List<Countries>

    @Query("DELETE from countries")
    suspend fun deleteAllData()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(countries: List<Countries>)

    @Query("SELECT * from Countries where name = :countryName Limit 1")
    suspend fun findCountryByName(countryName: String): Countries?

    @Query("SELECT * from countries where name Like '%' || :keyword || '%'")
    suspend fun searchCountryByName(keyword: String): List<Countries>
}