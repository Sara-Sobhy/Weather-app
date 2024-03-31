package com.example.weather.alert.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather.dataBase.WeatherRepo
import com.example.weather.model.Alarm
import com.example.weather.model.WeatherEntity
import com.example.weather.network.WeatherState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException

class AlarmViewModel (private val weatherRepo: WeatherRepo) : ViewModel() {
    fun insertAlram(alarm: Alarm) {
        viewModelScope.launch {
            weatherRepo.insertAlarm(alarm)
        }
    }
    fun getAllAlrams(): Flow<List<Alarm>> {
        return weatherRepo.getAllAlarms()
    }

    fun deleteAlram(alarm: Alarm) {
        viewModelScope.launch {
            weatherRepo.deleteAlarm(alarm)
        }
    }

}
class AlarmViewModelFactory(private val weatherRepo: WeatherRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmViewModel(weatherRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}