package com.example.weather.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.model.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): Flow<List<Alarm>>

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)
}