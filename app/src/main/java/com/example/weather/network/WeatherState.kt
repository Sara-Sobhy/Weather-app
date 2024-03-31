package com.example.weather.network

import com.example.weather.model.ForecastResponse

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success<out T>(val data: T) : WeatherState()
    //data class WeatherSuccess(val weatherResponse: WeatherResponse) : WeatherState()
   data class ForecastSuccess(val forecastResponse: ForecastResponse) : WeatherState()
    data class Error(val message: String) :WeatherState()
}