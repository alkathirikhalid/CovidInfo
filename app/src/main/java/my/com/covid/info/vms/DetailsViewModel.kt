package my.com.covid.info.vms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import my.com.covid.info.models.History
import my.com.covid.info.utils.*
import java.text.SimpleDateFormat
import java.util.*

class DetailsViewModel(
    private val networkHelper: NetworkHelper,
    private val apiRepository: ApiRepository,
    private val jsonHelper: JsonHelper
) : ViewModel() {

    private val fromFormat = SimpleDateFormat("yyyyy/MM/dd hh:mm:ss+00", Locale.getDefault())
    private val toFormat = SimpleDateFormat("dd MMM yyyy hh:mm", Locale.getDefault())

    private var _history = MutableLiveData<Resource<History>>()
    val history: LiveData<Resource<History>>
        get() = _history

    fun fetchHistoryData(country: String, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _history.postValue(Resource.loading(null))
            try {
                if (networkHelper.isNetworkConnected()) {
                    when (val request = apiRepository.fetchHistoryData(country, type)) {
                        is Result.Success<String> -> {
                            val json = request.data
                            val item = jsonHelper.parseHistoryJson(json)
                            if (item != null) {
                                _history.postValue(Resource.success(item))
                            } else {
                                _history.postValue(Resource.error("item is empty", null))
                            }
                        }
                        is Result.Error<String> -> {
                            _history.postValue(Resource.error(request.exception, null))
                        }
                    }
                } else {
                    _history.postValue(Resource.error("No Internet Connection", null))
                }
            } catch (e: Exception) {
                _history.postValue(Resource.error("Error1: ${e.localizedMessage}", null))
            }
        }
    }

    fun convertFromCountryJson(json: String) = jsonHelper.parseCountryJson(json)
    fun convertTimeStamp(dateString: String): String? {
        val dt = fromFormat.parse(dateString)
        return dt?.let { toFormat.format(it) }
    }
}