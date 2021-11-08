package my.com.covid.info.vms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.com.covid.info.adapters.CountryAdapter
import my.com.covid.info.dbs.AppDao
import my.com.covid.info.models.Countries
import my.com.covid.info.utils.JsonHelper
import my.com.covid.info.utils.Resource

class SearchViewModel(private val dao: AppDao, private val jsonHelper: JsonHelper): ViewModel() {

    val adapter = CountryAdapter()

    private val _search = MutableLiveData<Resource<List<Countries>>>()
    val search: LiveData<Resource<List<Countries>>>
        get() = _search

    fun fetchSearchData(keyword: String) {
        viewModelScope.launch {
            _search.postValue(Resource.loading(null))
            try {
                val countryLst = dao.searchCountryByName(keyword)
                if (countryLst.isNotEmpty()) {
                    adapter.invalidated()
                    adapter.addData(countryLst)
                    _search.postValue(Resource.success(countryLst))
                } else {
                    _search.postValue(Resource.error("Result not found.", null))
                }
            } catch (e: Exception) {
                _search.postValue(Resource.error("Search Error", null))
            }
        }
    }

    fun convertToCountryJson(countries: Countries) = jsonHelper.toCountryJson(countries)
}