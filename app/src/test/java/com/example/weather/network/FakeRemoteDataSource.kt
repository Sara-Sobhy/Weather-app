package com.example.weather.network

import com.example.weather.model.ForecastResponse
import com.example.weather.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource: RemoteDataSourceInterface {
    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String,
        languge: String
    ): Flow<WeatherResponse> {
         return flow {
         }
    }

    override suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String,
        languge: String
    ): Flow<ForecastResponse> {
        return flow {

        }
    }

    override suspend fun searchWeatherByCity(
        cityName: String,
        apiKey: String
    ): Flow<WeatherResponse> {
        return flow {

        }
    }
}