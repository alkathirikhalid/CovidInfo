package my.com.covid.info.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.enums.HoverMode
import com.anychart.enums.TooltipPositionMode
import my.com.covid.info.R
import my.com.covid.info.databinding.DeathsFragmentBinding
import my.com.covid.info.models.History
import my.com.covid.info.utils.ApiRepository
import my.com.covid.info.utils.Resource
import my.com.covid.info.utils.Status
import my.com.covid.info.vms.DetailsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeathsFragment: Fragment() {

    companion object {
        const val COUNTRY_NAME = "COUNTRY_NAME"

        @JvmStatic
        fun create(countryName: String) = DeathsFragment().apply {
            arguments = Bundle().apply {
                putString(ConfirmedFragment.COUNTRY_NAME, countryName)
            }
        }
    }

    private var countryName = ""
    private lateinit var binding: DeathsFragmentBinding

    private val viewModel: DetailsViewModel by sharedViewModel()
    private lateinit var cartesian: Cartesian
    private val data = ArrayList<DataEntry>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeathsFragmentBinding.inflate(layoutInflater, container, false)
        val rootView = binding.root
        setupCharts()
        fetchData()
        return rootView
    }

    private fun fetchData() {
        viewModel.history.observe(viewLifecycleOwner, getDataObserver)
        viewModel.fetchHistoryData(countryName, ApiRepository.TYPE_DEATHS)
    }

    private val getDataObserver = Observer<Resource<History>> {
        setupObserver(it)
    }

    private fun setupObserver(resource: Resource<History>) {
        when(resource.status) {
            Status.LOADING -> {
                binding.status.visibility = View.VISIBLE
                binding.anyChart.visibility = View.GONE
                binding.status.text = getString(R.string.loading_label)
            }
            Status.ERROR -> {
                binding.status.visibility = View.VISIBLE
                binding.anyChart.visibility = View.GONE
                binding.status.text = resource.message
            }
            Status.SUCCESS -> {
                viewModel.history.removeObserver(getDataObserver)
                binding.status.visibility = View.GONE
                binding.anyChart.visibility = View.VISIBLE
                parseHistoryData(resource.data)
            }
        }
    }

    private fun parseHistoryData(history: History?) {
        if (history == null) {
            binding.status.visibility = View.VISIBLE
            binding.anyChart.visibility = View.GONE
            binding.status.text = getString(R.string.wrong_lbl)
            return
        }

        for ((key, value) in history.All.dates) {
            data.add(ValueDataEntry(key, value.toInt()))
        }
        data.reverse()
        cartesian.column(data)
        binding.anyChart.invalidate()
    }

    private fun setupCharts() {
        val chartstitle = "Covid Deaths Cases"
        cartesian = AnyChart.column()
        cartesian.animation(true)
        cartesian.title(chartstitle)
        cartesian.yScale().minimum(0)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)
        cartesian.xAxis(0).title("Dates")
        cartesian.yAxis(0).title("Cases")
        binding.anyChart.setChart(cartesian)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString(COUNTRY_NAME)?.let { countryName = it }
    }
}