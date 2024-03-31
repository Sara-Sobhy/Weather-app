package com.example.weather.dataBase

import com.example.weather.model.Alarm
import com.example.weather.model.ForecastResponse
import com.example.weather.model.WeatherEntity
import com.example.weather.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {
    fun getAllWeather(): Flow<List<WeatherEntity>>
    suspend fun deleteWeather(weather: WeatherEntity)
    suspend fun insertWeather(weather: WeatherEntity)
    suspend fun getWeather(latitude: Double, longitude: Double, apiKey: String,unit:String,languge:String):Flow<WeatherResponse>
    suspend fun getForecast(latitude: Double, longitude: Double, apiKey: String,unit: String,languge: String): Flow<ForecastResponse>
    suspend fun searchWeatherByCity(cityName: String, apiKey: String): Flow<WeatherResponse>
    fun getAllAlarms(): Flow<List<Alarm>>
    suspend fun insertAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)

}