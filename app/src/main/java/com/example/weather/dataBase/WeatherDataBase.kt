package com.example.weather.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weather.alert.AlertReceiver
import com.example.weather.model.Alarm
import com.example.weather.model.WeatherEntity

//@Database(entities = [WeatherEntity::class], version = 1)
@Database(entities = [WeatherEntity::class, Alarm::class], version = 2)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): WeatherDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "weather.db"
            ).addMigrations(MIGRATION_1_2).build()
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS alarms " +
                            "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "timeInMillis INTEGER NOT NULL, " +
                            "isAlarm INTEGER NOT NULL, " +
                            "latitude REAL NOT NULL, " +
                            "longitude REAL NOT NULL)"
                )
            }
        }
    }
}