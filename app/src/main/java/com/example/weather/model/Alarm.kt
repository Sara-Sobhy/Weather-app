package com.example.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val timeInMillis: Long,
    val isAlarm: Boolean,
    val latitude: Double,
    val longitude: Double
)