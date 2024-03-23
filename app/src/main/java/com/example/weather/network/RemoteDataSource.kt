package com.example.weather.network

import com.example.weather.model.ForecastResponse
import com.example.weather.model.WeatherResponse

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getWeather(latitude: Double, longitude: Double, apiKey: String): WeatherResponse {
        return apiService.getWeather(latitude, longitude, apiKey)
    }

    suspend fun getForecast(latitude: Double, longitude: Double, apiKey: String): ForecastResponse {
        return apiService.getForecast(latitude, longitude, apiKey)
    }

    suspend fun searchWeatherByCity(cityName: String, apiKey: String): WeatherResponse {
        return apiService.searchWeather(cityName, apiKey)
    }
}