package my.com.covid.info.utils

import my.com.covid.info.BuildConfig
import java.io.BufferedReader
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ApiRepository {

    fun fetchCovidData(): Result<String> {
        val inputStream: InputStream
        val url = URL(BuildConfig.CASES_URL) //for security purpose it will compile at runtime
        return try {
            val conn = url.openConnection() as HttpsURLConnection
            conn.connect()
            inputStream = conn.inputStream
            if (inputStream != null) {
                val infodata = inputStream.bufferedReader().use(BufferedReader::readText)
                Result.Success(infodata)
            } else {
                Result.Error("inputstream is Null")
            }
        } catch (e: Error) {
            Result.Error("Cannot open HttpURLConnection")
        }
    }

    fun fetchHistoryData(country: String, statusType: String): Result<String> {
        val inputStream: InputStream
        val pathUrl = BuildConfig.HISTORY_URL + "?country=$country&status=$statusType"
        val url = URL(pathUrl)
        return try {
            val conn = url.openConnection() as HttpsURLConnection
            conn.connect()
            inputStream = conn.inputStream
            if (inputStream != null) {
                val infodata = inputStream.bufferedReader().use(BufferedReader::readText)
                Result.Success(infodata)
            } else {
                Result.Error("inputstream is Null")
            }
        } catch (e: Error) {
            Result.Error("Cannot open HttpURLConnection")
        }
    }

    companion object {
        const val TYPE_CONFIRMED = "confirmed"
        const val TYPE_DEATHS = "deaths"
    }
}

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val exception: T) : Result<T>()
}
