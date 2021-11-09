package my.com.covid.info.vms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import my.com.covid.info.adapters.CountryAdapter
import my.com.covid.info.dbs.AppDao
import my.com.covid.info.models.Countries
import my.com.covid.info.utils.*

class MainViewModel(
    private val networkHelper: NetworkHelper,
    private val apiRepository: ApiRepository,
    private val jsonHelper: JsonHelper,
    private val dao: AppDao
) : ViewModel() {

    private var _info = MutableLiveData<Resource<List<Countries>>>()
    val info: LiveData<Resource<List<Countries>>>
        get() = _info

    val adapter = CountryAdapter()

    fun fetchCovidInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            _info.postValue(Resource.loading(null))
            try {
                var countryLst = dao.getAllCountries()
                if (countryLst.isEmpty()) {
                    if (networkHelper.isNetworkConnected()) {
                        when (val dataRequest = apiRepository.fetchCovidData()) {
                            is Result.Success<String> -> {
                                val infodata = dataRequest.data
                                val listData = jsonHelper.ilterateCountries(infodata).toList()
                                countryLst =
                                    listData.filter { it.name != "Global" && it.name != "Summer Olympics 2020" && it.name != "Taiwan*" && it.All.population != 0L }
                                dao.insertCountries(countryLst)
                                _info.postValue(Resource.success(countryLst))
                                adapter.addData(countryLst)
                            }
                            is Result.Error<String> -> {
                                _info.postValue(Resource.error(dataRequest.exception, null))
                            }
                        }
                    } else {
                        _info.postValue(Resource.error("No Internet Connection", null))
                    }
                } else {
                    _info.postValue(Resource.success(countryLst))
                    adapter.addData(countryLst)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _info.postValue(Resource.error("Unknown: ${e.localizedMessage}", null))
            }
        }
    }

    fun sortByActiveCases() {
        viewModelScope.launch {
            val countries = dao.getAllCountries()
            val sort = countries.sortedByDescending { it.All.confirmed }
            adapter.invalidated()
            adapter.addData(sort)
        }
    }

    fun sortByDeaths() {
        viewModelScope.launch {
            val countries = dao.getAllCountries()
            val sort = countries.sortedByDescending { it.All.deaths }
            adapter.invalidated()
            adapter.addData(sort)
        }
    }

    fun sortActiveCasesPer100k() {
        viewModelScope.launch {
            val countries = dao.getAllCountries()
            val sort = countries.sortedByDescending {
                if (it.All.population?.toInt() == 0) {
                    (it.All.confirmed.toDouble() / 1) * 100000
                } else {
                    (it.All.confirmed.toDouble() / it.All.population!!.toInt()) * 100000
                }
            }
            adapter.invalidated()
            adapter.addData(sort)
        }
    }

    fun sortDeathsPer100k() {
        viewModelScope.launch {
            val countries = dao.getAllCountries()
            val sort = countries.sortedByDescending {
                //some population data return 0 from api - so need to checker
                if (it.All.population?.toInt() == 0) {
                    (it.All.deaths.toDouble() / 1) * 100000
                } else {
                    (it.All.deaths.toDouble() / it.All.population!!.toInt()) * 100000
                }
            }
            adapter.invalidated()
            adapter.addData(sort)
        }
    }

    fun onRefreshData() {
        viewModelScope.launch {
            dao.deleteAllData()
            adapter.invalidated()
        }
    }

    fun convertToCountryJson(countries: Countries) = jsonHelper.toCountryJson(countries)
}