package my.com.covid.info.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import my.com.covid.info.R
import my.com.covid.info.databinding.CountryListItemBinding
import my.com.covid.info.models.Countries

class CountryAdapter: PagingDataAdapter<Countries, BaseViewHolder<Countries>>(CountryDiff()) {

    class CountryDiff: DiffUtil.ItemCallback<Countries>() {
        override fun areItemsTheSame(oldItem: Countries, newItem: Countries): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Countries, newItem: Countries): Boolean {
            return oldItem.name == newItem.name
        }
    }

    interface AdapterEvent {
        fun onCountrySelected(countries: Countries)
    }

    private val data: MutableList<Countries>
    var listener: AdapterEvent? = null

    init {
        data = ArrayList()
    }

    suspend fun addData(newList: List<Countries>) {
        data.clear()
        data.addAll(newList)
        val paged = PagingData.from(data)
        submitData(paged)
    }

    suspend fun invalidated() {
        data.clear()
        val paged = PagingData.from(data)
        submitData(paged)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Countries>, position: Int) {
        val item = getItem(position)
        when(holder) {
            is ListHolder -> item?.let { holder.bindItem(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Countries> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.country_list_item, parent, false)
        return ListHolder(view, listener)
    }

    class ListHolder(val view: View, val listener: AdapterEvent?): BaseViewHolder<Countries>(view) {

        private val binding: CountryListItemBinding = CountryListItemBinding.bind(view)

        override fun bindItem(item: Countries) {
            binding.nameLabel.text = item.name
            itemView.setOnClickListener { v ->
                listener?.onCountrySelected(item)
            }
        }
    }
}