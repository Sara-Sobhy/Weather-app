package com.example.weather.dataBase

import androidx.lifecycle.LiveData
import com.example.weather.model.Alarm
import com.example.weather.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val weatherDao: WeatherDao,private val alramDao: AlarmDao) {

    fun getAllWeather(): Flow<List<WeatherEntity>> {
        return weatherDao.getAllWeather()
    }

    suspend fun insertWeather(weather: WeatherEntity) {
        weatherDao.insertWeather(weather)
    }

     suspend fun deleteWeather(weather: WeatherEntity) {
        weatherDao.deleteWeather(weather)
    }

    fun getAllAlarms():Flow<List<Alarm>>{
        return alramDao.getAllAlarms()
    }

    suspend fun insertAlarm(alram:Alarm){
        alramDao.insertAlarm(alram)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alramDao.deleteAlarm(alarm)
    }

}