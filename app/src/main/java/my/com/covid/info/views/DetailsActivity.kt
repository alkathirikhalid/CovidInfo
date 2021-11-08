package my.com.covid.info.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import my.com.covid.info.R
import my.com.covid.info.adapters.BaseTabAdapter
import my.com.covid.info.databinding.ActivityDetailsBinding
import my.com.covid.info.models.Countries
import my.com.covid.info.views.fragments.ConfirmedFragment
import my.com.covid.info.views.fragments.DeathsFragment
import my.com.covid.info.vms.DetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val detailsViewModel: DetailsViewModel by viewModel()

    private var country: Countries? = null
    private var jsonData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        getExtras()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (jsonData.isNotEmpty()) {
            //Log.e("json", "data: $jsonData")
            country = detailsViewModel.convertFromCountryJson(jsonData)
            supportActionBar?.title = country?.name
        }
        setupPager()
        populateData()
    }

    private fun populateData() {
        country?.let {
            val population = it.All.population?.toInt() ?: 100000 // fallback if no population data from api, to avoid crash
            val deathsPer100k = (it.All.deaths.toDouble().div(population)).times(100000)
            val activePer100k = (it.All.confirmed.toDouble().div(population)).times(100000)

            binding.activeCases.text = it.All.confirmed.toString()
            binding.deathsCases.text = it.All.deaths.toString()
            binding.deaths100k.text = String.format("%.2f", deathsPer100k)
            binding.active100k.text = String.format("%.2f", activePer100k)
            binding.udpateDt.text = if(it.All.updated?.isNotEmpty() == true) detailsViewModel.convertTimeStamp(it.All.updated) else ""
        }
    }

    private fun setupPager() {
        val pagerAdapter = BaseTabAdapter(supportFragmentManager)
        country?.let {
            pagerAdapter.addFragment(DeathsFragment.create(it.name), getString(R.string.deaths_lbl))
            pagerAdapter.addFragment(ConfirmedFragment.create(it.name), getString(R.string.active_cases_lbl))
        }
        binding.viewpager.adapter = pagerAdapter
        binding.tabs.setupWithViewPager(binding.viewpager)
    }

    private fun getExtras() {
        jsonData = intent.getStringExtra(COUNTRY_DATA) ?: ""
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val COUNTRY_DATA = "COUNTRY_DATA"
    }
}