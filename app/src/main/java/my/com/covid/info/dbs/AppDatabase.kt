package my.com.covid.info.dbs

import androidx.room.Database
import androidx.room.RoomDatabase
import my.com.covid.info.models.AllData
import my.com.covid.info.models.Countries

@Database(entities = [Countries::class, AllData::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dao(): AppDao
}