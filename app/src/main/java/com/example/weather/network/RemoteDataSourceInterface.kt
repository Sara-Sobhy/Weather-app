package com.example.weather.network

import com.example.weather.model.ForecastResponse
import com.example.weather.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface RemoteDataSourceInterface {
    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String,
        languge: String
    ): Flow<WeatherResponse>

    suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String,
        languge: String
    ): Flow<ForecastResponse>

    /*suspend fun searchWeatherByCity(cityName: String, apiKey: String): Flow<WeatherResponse> {
           return flow {
               val searchemit=apiService.searchWeather(cityName, apiKey)
               emit(searchemit)
           }
       }*/
    suspend fun searchWeatherByCity(cityName: String, apiKey: String): Flow<WeatherResponse>
}