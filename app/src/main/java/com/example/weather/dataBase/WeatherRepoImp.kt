package com.example.weather.dataBase

import com.example.weather.model.Alarm
import com.example.weather.model.ForecastResponse
import com.example.weather.model.WeatherEntity
import com.example.weather.model.WeatherResponse
import com.example.weather.network.RemoteDataSource
import com.example.weather.network.RemoteDataSourceInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

class WeatherRepoImp(
    private val remoteDataSource: RemoteDataSourceInterface,
    private val localDataSource: LocalDataSourceInterface,
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

    override suspend fun getWeather(latitude: Double, longitude: Double, apiKey: String,unit:String,languge:String): Flow<WeatherResponse> {
        return remoteDataSource.getWeather(latitude, longitude, apiKey,unit,languge)
    }

    override suspend fun getForecast(latitude: Double, longitude: Double, apiKey: String,unit: String,languge: String): Flow<ForecastResponse> {
        return remoteDataSource.getForecast(latitude, longitude, apiKey,unit,languge)
    }

    override suspend fun searchWeatherByCity(cityName: String, apiKey: String): Flow<WeatherResponse> {
        return remoteDataSource.searchWeatherByCity(cityName, apiKey)
    }


    override fun getAllAlarms(): Flow<List<Alarm>> {
        return localDataSource.getAllAlarms()
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        localDataSource.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
       localDataSource.deleteAlarm(alarm)
    }

}