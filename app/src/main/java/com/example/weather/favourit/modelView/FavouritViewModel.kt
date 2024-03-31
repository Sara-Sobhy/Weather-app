package com.example.weather.favourit.modelView

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather.dataBase.WeatherRepo
import com.example.weather.model.WeatherEntity
import com.example.weather.network.WeatherState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException

class FavouritViewModel (private val weatherRepo: WeatherRepo) : ViewModel() {

    fun saveWeatherData(weatherEntity: WeatherEntity) {
        viewModelScope.launch {
            weatherRepo.insertWeather(weatherEntity)
        }
    }
    fun getAllWeather(): Flow<List<WeatherEntity>> {
        return weatherRepo.getAllWeather()
    }

    fun deleteWeatherData(weatherEntity: WeatherEntity) {
        viewModelScope.launch {
            weatherRepo.deleteWeather(weatherEntity)
        }
    }

}
class FavouriteViewModelFactory(private val weatherRepo: WeatherRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouritViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavouritViewModel(weatherRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}