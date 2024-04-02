package com.example.weather.database

import com.example.weather.dataBase.WeatherRepo
import com.example.weather.model.Alarm
import com.example.weather.model.ForecastResponse
import com.example.weather.model.WeatherEntity
import com.example.weather.model.WeatherResponse
import com.example.weather.network.FakeRemoteDataSource
import kotlinx.coroutines.flow.Flow

class FakeRepoImpl:WeatherRepo {
    private val fakeLocalDataSource = FakeLocalDataSource()
    private val fakeRemoteDataSource = FakeRemoteDataSource()
    override fun getAllWeather(): Flow<List<WeatherEntity>> {
        return fakeLocalDataSource.getAllWeather()
    }

    override suspend fun deleteWeather(weather: WeatherEntity) {
        fakeLocalDataSource.deleteWeather(weather)
    }

    override suspend fun insertWeather(weather: WeatherEntity) {
        fakeLocalDataSource.insertWeather(weather)
    }

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        unit: String,
        languge: String
    ): Flow<WeatherResponse> {
        return fakeRemoteDataSource.getWeather(latitude,longitude,apiKey,unit,languge)
    }

    override suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        unit: String,
        languge: String
    ): Flow<ForecastResponse> {
        return fakeRemoteDataSource.getForecast(latitude,longitude,apiKey,unit,languge)
    }

    override suspend fun searchWeatherByCity(
        cityName: String,
        apiKey: String
    ): Flow<WeatherResponse> {
        return fakeRemoteDataSource.searchWeatherByCity(cityName,apiKey)
    }

    override fun getAllAlarms(): Flow<List<Alarm>> {
        return fakeLocalDataSource.getAllAlarms()
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        fakeLocalDataSource.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        fakeLocalDataSource.deleteAlarm(alarm)
    }
}