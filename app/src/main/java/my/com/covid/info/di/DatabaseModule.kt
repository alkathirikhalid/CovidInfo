package my.com.covid.info.di

import android.content.Context
import androidx.room.Room
import my.com.covid.info.dbs.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}

private fun provideDatabase(context: Context): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
    .fallbackToDestructiveMigration()
    .build()

private fun provideDao(database: AppDatabase) = database.dao()