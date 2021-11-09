package my.com.covid.info.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import my.com.covid.info.models.AllData
import my.com.covid.info.models.Countries
import my.com.covid.info.models.History
import org.json.JSONObject

class JsonHelper constructor(private val moshi: Moshi) {

    fun ilterateCountries(json: String): ArrayList<Countries> {
        val dynamicJson = JSONObject(json)
        val keys: Iterator<String> = dynamicJson.keys()
        val allAdapter: JsonAdapter<AllData> = moshi.adapter(AllData::class.java)
        val allCountries = ArrayList<Countries>()

        while (keys.hasNext()) {
            val currentKeys = keys.next()
            val currentValues = dynamicJson.getJSONObject(currentKeys).getString("All")
            val alljay = allAdapter.fromJson(currentValues)
            allCountries.add(Countries(currentKeys, alljay!!))
        }
        return allCountries
    }

    fun parseHistoryJson(json: String): History? {
        val jsonAdapter: JsonAdapter<History> = moshi.adapter(History::class.java)
        return jsonAdapter.fromJson(json)
    }

    fun parseCountryJson(json: String): Countries? {
        val jsonAdapter: JsonAdapter<Countries> = moshi.adapter(Countries::class.java)
        return jsonAdapter.fromJson(json)
    }

    fun toCountryJson(countries: Countries): String? {
        val jsonAdapter: JsonAdapter<Countries> = moshi.adapter(Countries::class.java)
        return jsonAdapter.toJson(countries)
    }
}