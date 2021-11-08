package my.com.covid.info.core

import android.app.Application
import my.com.covid.info.di.appModule
import my.com.covid.info.di.databaseModule
import my.com.covid.info.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppCore: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AppCore)
            modules(listOf(appModule, databaseModule, viewModelModule))
        }
    }
}