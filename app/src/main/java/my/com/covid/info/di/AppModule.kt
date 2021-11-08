package my.com.covid.info.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import my.com.covid.info.utils.ApiRepository
import my.com.covid.info.utils.JsonHelper
import my.com.covid.info.utils.NetworkHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { provideMoshi() }
    single { provideNetworkHelper(androidContext()) }
    single { provideApiRepository() }
    single { provideJsonHelper(get()) }
}

private fun provideNetworkHelper(context: Context) = NetworkHelper(context)
private fun provideApiRepository() = ApiRepository()

private fun provideMoshi(): Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private fun provideJsonHelper(moshi: Moshi) = JsonHelper(moshi)