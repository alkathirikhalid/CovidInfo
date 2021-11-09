package my.com.covid.info.dbs

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import my.com.covid.info.models.Countries
import my.com.covid.info.utils.JsonHelper
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: AppDao
    private lateinit var helper: JsonHelper
    private var list: ArrayList<Countries> = ArrayList()

    private val testJson =
        "{ \"Afghanistan\": { \"All\": { \"confirmed\": 156397, \"recovered\": 0, \"deaths\": 7288, \"country\": \"Afghanistan\", \"population\": 35530081, \"sq_km_area\": 652090, \"life_expectancy\": \"45.9\", \"elevation_in_meters\": null, \"continent\": \"Asia\", \"abbreviation\": \"AF\", \"location\": \"Southern and Central Asia\", \"iso\": 4, \"capital_city\": \"Kabul\", \"lat\": \"33.93911\", \"long\": \"67.709953\", \"updated\": \"2021/11/08 15:21:54+00\" } }, \"Albania\": { \"All\": { \"confirmed\": 189125, \"recovered\": 0, \"deaths\": 2955, \"country\": \"Albania\", \"population\": 2930187, \"sq_km_area\": 28748, \"life_expectancy\": \"71.6\", \"elevation_in_meters\": null, \"continent\": \"Europe\", \"abbreviation\": \"AL\", \"location\": \"Southern Europe\", \"iso\": 8, \"capital_city\": \"Tirana\", \"lat\": \"41.1533\", \"long\": \"20.1683\", \"updated\": \"2021/11/08 15:21:54+00\" } }}"

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = database.dao()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        helper = JsonHelper(moshi)

        list = helper.ilterateCountries(testJson)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun writeAndReadDatabase() {
        runBlocking {
            dao.insertCountries(list)

            val getAll = dao.getAllCountries()
            assertThat(getAll.contains(list[0])).isTrue()
        }
    }

    @Test
    fun validateDataSize() {
        runBlocking {
            dao.insertCountries(list)

            val getAll = dao.getAllCountries()
            assertThat(getAll.size == 2).isTrue()
        }
    }
}