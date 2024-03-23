package com.example.weather.dataBase

import androidx.lifecycle.LiveData
import com.example.weather.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val weatherDao: WeatherDao) {

    fun getAllWeather(): Flow<List<WeatherEntity>> {
        return weatherDao.getAllWeather()
    }

    suspend fun insertWeather(weather: WeatherEntity) {
        weatherDao.insertWeather(weather)
    }

     suspend fun deleteWeather(weather: WeatherEntity) {
        weatherDao.deleteWeather(weather)
    }
}