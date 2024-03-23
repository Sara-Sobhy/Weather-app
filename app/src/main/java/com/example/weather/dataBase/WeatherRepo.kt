package com.example.weather.dataBase

import com.example.weather.model.ForecastResponse
import com.example.weather.model.WeatherEntity
import com.example.weather.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {
    fun getAllWeather(): Flow<List<WeatherEntity>>
    suspend fun deleteWeather(weather: WeatherEntity)
    suspend fun insertWeather(weather: WeatherEntity)
    suspend fun getWeather(latitude: Double, longitude: Double, apiKey: String): WeatherResponse
    suspend fun getForecast(latitude: Double, longitude: Double, apiKey: String): ForecastResponse
    suspend fun searchWeatherByCity(cityName: String, apiKey: String): WeatherResponse
}