package com.example.weather.modeView

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather.dataBase.WeatherRepo
import com.example.weather.model.ForecastResponse
import com.example.weather.model.WeatherEntity
import com.example.weather.model.WeatherResponse
import com.example.weather.network.API
import com.example.weather.network.WeatherState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class WeatherViewModel (private val weatherRepo: WeatherRepo) : ViewModel() {

    private val _weatherData = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherData: StateFlow<WeatherState> = _weatherData

    private val _forecastData = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val forecastData: StateFlow<WeatherState> = _forecastData

    private val _searchResults = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val searchResults: SharedFlow<WeatherState> = _searchResults

    fun getCurrentWeather(latitude: Double, longitude: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = API.retrofitService.getWeather(latitude, longitude, apiKey)
                _weatherData.value = WeatherState.Success(response)
            } catch (e: IOException) {
                _weatherData.value = WeatherState.Error("errorrrr")
                e.printStackTrace()
            }
        }
    }

    fun getForecast(latitude: Double, longitude: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = API.retrofitService.getForecast(latitude, longitude, apiKey)
                _forecastData.value = WeatherState.ForecastSuccess(response)
                Log.i("hifor", "getForecast: "+_forecastData.value)
            } catch (e: IOException) {
                _forecastData.value = WeatherState.Error("Error fetching forecast data")
                e.printStackTrace()
            }
        }
    }

    fun searchWeatherByCity(cityName: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = API.retrofitService.searchWeather(cityName, apiKey)
                _searchResults.value = WeatherState.Success(response)
                Log.i("searchh", "searchWeatherByCity: "+_searchResults.value)
            } catch (e: IOException) {
                _searchResults.value = WeatherState.Error("Error searching weather data")
                e.printStackTrace()
            }
        }
    }

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
class WeatherViewModelFactory(private val weatherRepo: WeatherRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(weatherRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}