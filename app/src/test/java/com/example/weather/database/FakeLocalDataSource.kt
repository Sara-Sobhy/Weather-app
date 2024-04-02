package com.example.weather.database

import com.example.weather.alert.Alert
import com.example.weather.dataBase.LocalDataSource
import com.example.weather.dataBase.LocalDataSourceInterface
import com.example.weather.model.Alarm
import com.example.weather.model.WeatherEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource(private val favoriteLocations : MutableList<WeatherEntity>?=mutableListOf(),
                          private val alerts : MutableList<Alarm>? = mutableListOf()) : LocalDataSourceInterface {
    override fun getAllWeather(): Flow<List<WeatherEntity>> {
        return flow {
            val favLocation = favoriteLocations?.toList()
            if(favLocation.isNullOrEmpty()){
                emit(emptyList())
            }else{
                emit(favLocation)
            }

        }
    }
    override suspend fun insertWeather(weather: WeatherEntity) {
       favoriteLocations?.add(weather)
    }

    override suspend fun deleteWeather(weather: WeatherEntity) {
        favoriteLocations?.remove(weather)
    }

    override fun getAllAlarms(): Flow<List<Alarm>> {
        return flow {
            val alert = alerts?.toList()
            if(alert.isNullOrEmpty()){
                emit(emptyList())
            }else{
                emit(alert)
            }

        }
    }

    override suspend fun insertAlarm(alram: Alarm) {
       alerts?.add(alram)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
             alerts?.remove(alarm)
    }
}