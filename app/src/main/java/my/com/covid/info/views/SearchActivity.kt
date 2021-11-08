package my.com.covid.info.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import my.com.covid.info.adapters.CountryAdapter
import my.com.covid.info.databinding.ActivitySearchBinding
import my.com.covid.info.models.Countries
import my.com.covid.info.utils.Status
import my.com.covid.info.vms.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecycler()
        setupEventListener()
    }

    private fun setupEventListener() {
        binding.searchSV.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isNotEmpty() == true) {
                    binding.searchSV.clearFocus()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length!! >= 2) {
                    searchData(newText)
                }
                return false
            }
        })
    }

    private fun searchData(query: String) {
        searchViewModel.search.observe(this, {
            when(it.status) {
                Status.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                    binding.infotxt.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    binding.infotxt.visibility = View.VISIBLE
                    binding.infotxt.text = it.message
                }
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    binding.infotxt.visibility = View.GONE
                }
            }
        })
        searchViewModel.fetchSearchData(query)
    }

    private fun setupRecycler() {
        searchViewModel.adapter.listener = adapterListener
        val lmanager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        binding.recycler.layoutManager = lmanager
        binding.recycler.adapter = searchViewModel.adapter
    }

    private val adapterListener = object: CountryAdapter.AdapterEvent{
        override fun onCountrySelected(countries: Countries) {
            val json = searchViewModel.convertToCountryJson(countries)
            json?.let {
                val intent = Intent(this@SearchActivity, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.COUNTRY_DATA, it)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}