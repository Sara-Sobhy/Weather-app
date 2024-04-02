package com.example.weather.dataBase

import androidx.lifecycle.LiveData
import com.example.weather.model.Alarm
import com.example.weather.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val weatherDao: WeatherDao,private val alramDao: AlarmDao) :
    LocalDataSourceInterface {

    override fun getAllWeather(): Flow<List<WeatherEntity>> {
        return weatherDao.getAllWeather()
    }

    override suspend fun insertWeather(weather: WeatherEntity) {
        weatherDao.insertWeather(weather)
    }

     override suspend fun deleteWeather(weather: WeatherEntity) {
        weatherDao.deleteWeather(weather)
    }

    override fun getAllAlarms():Flow<List<Alarm>>{
        return alramDao.getAllAlarms()
    }

    override suspend fun insertAlarm(alram:Alarm){
        alramDao.insertAlarm(alram)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alramDao.deleteAlarm(alarm)
    }

}