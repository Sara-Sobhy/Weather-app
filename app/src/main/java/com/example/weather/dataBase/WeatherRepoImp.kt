package com.example.weather.dataBase

import com.example.weather.model.ForecastResponse
import com.example.weather.model.WeatherEntity
import com.example.weather.model.WeatherResponse
import com.example.weather.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepoImp(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : WeatherRepo {
    override fun getAllWeather(): Flow<List<WeatherEntity>> {
        return localDataSource.getAllWeather()
    }

    override suspend fun deleteWeather(weather: WeatherEntity) {
        localDataSource.deleteWeather(weather)
    }

    override suspend fun insertWeather(weather: WeatherEntity) {
        localDataSource.insertWeather(weather)
    }

    override suspend fun getWeather(latitude: Double, longitude: Double, apiKey: String): WeatherResponse {
        return remoteDataSource.getWeather(latitude, longitude, apiKey)
    }

    override suspend fun getForecast(latitude: Double, longitude: Double, apiKey: String): ForecastResponse {
        return remoteDataSource.getForecast(latitude, longitude, apiKey)
    }

    override suspend fun searchWeatherByCity(cityName: String, apiKey: String): WeatherResponse {
        return remoteDataSource.searchWeatherByCity(cityName, apiKey)
    }
}