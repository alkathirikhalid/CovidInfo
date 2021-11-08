package my.com.covid.info.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import my.com.covid.info.R
import my.com.covid.info.adapters.CountryAdapter
import my.com.covid.info.databinding.ActivityMainBinding
import my.com.covid.info.models.Countries
import my.com.covid.info.utils.Status
import my.com.covid.info.vms.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.title = "Countries"
        binding.swipeRefresh.setOnRefreshListener(this)

        setupRecycler()
        fetchData()
    }

    private fun setupRecycler() {
        mainViewModel.adapter.listener = adapterListener
        val linearLayout = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        binding.recycler.layoutManager = linearLayout
        binding.recycler.adapter = mainViewModel.adapter
    }

    private val adapterListener = object: CountryAdapter.AdapterEvent{
        override fun onCountrySelected(countries: Countries) {
            val json = mainViewModel.convertToCountryJson(countries)
            json?.let {
                val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.COUNTRY_DATA, it)
                startActivity(intent)
            }
        }
    }

    private fun fetchData() {
        mainViewModel.info.observe(this, {
            when(it.status) {
                Status.SUCCESS ->{
                    binding.swipeRefresh.isRefreshing = false
                }
                Status.ERROR ->{
                    binding.swipeRefresh.isRefreshing = false
                    Log.e("Error", "Error Message: ${it.message}")
                }
                Status.LOADING -> binding.swipeRefresh.isRefreshing = true
            }
        })
        mainViewModel.fetchCovidInfo()
    }

    override fun onRefresh() {
        mainViewModel.onRefreshData()
        fetchData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.maction_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_search -> {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
            R.id.sort_active_cases -> {
                mainViewModel.sortByActiveCases()
            }
            R.id.sort_death -> {
                mainViewModel.sortByDeaths()
            }
            R.id.cases_100k -> {
                mainViewModel.sortActiveCasesPer100k()
            }
            R.id.deaths_100k -> {
                mainViewModel.sortDeathsPer100k()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}