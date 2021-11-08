package my.com.covid.info.di

import my.com.covid.info.vms.DetailsViewModel
import my.com.covid.info.vms.MainViewModel
import my.com.covid.info.vms.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(get(), get(), get(), get())
    }
    viewModel {
        SearchViewModel(get(), get())
    }
    viewModel {
        DetailsViewModel(get(), get(), get())
    }
}