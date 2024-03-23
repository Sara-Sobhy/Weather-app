package com.example.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val lon: Double,
    val lat: Double,
    val weatherId: Int,
    val main: String,
    val description: String,
    val icon: String,
    val cityName: String,
    val country: String,
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double,
    val windDegree: Int,
    val rain1h: Double,
    val cloudiness: Int,
    val sunrise: Long,
    val sunset: Long
)