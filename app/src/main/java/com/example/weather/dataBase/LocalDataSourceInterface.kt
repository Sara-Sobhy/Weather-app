package com.example.weather.dataBase

import com.example.weather.model.Alarm
import com.example.weather.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInterface {
    fun getAllWeather(): Flow<List<WeatherEntity>>

    suspend fun insertWeather(weather: WeatherEntity)

    suspend fun deleteWeather(weather: WeatherEntity)
    fun getAllAlarms(): Flow<List<Alarm>>
    suspend fun insertAlarm(alram: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
}