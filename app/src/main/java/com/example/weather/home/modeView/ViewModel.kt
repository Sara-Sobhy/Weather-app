package com.example.weather.home.modeView

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
import com.example.weather.setting.SharedPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException

class WeatherViewModel (private val weatherRepo: WeatherRepo,
                        private val sharedPreferencesManager: SharedPreferencesManager) : ViewModel() {

    private val _weatherData = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherData: StateFlow<WeatherState> = _weatherData

    private val _forecastData = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val forecastData: StateFlow<WeatherState> = _forecastData

    private val _searchResults = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val searchResults: SharedFlow<WeatherState> = _searchResults

    private fun getSelectedUnit(): String {
        return sharedPreferencesManager.getSelectedUnit()
    }
    private fun getSelectedLanguage(): String {
        return sharedPreferencesManager.getSelectedLanguage()
    }

    fun getCurrentWeather(latitude: Double, longitude: Double, apiKey: String) {
        val selectedUnit = getSelectedUnit()
        val selectedLanguage = getSelectedLanguage()
        Log.i("languge", "getCurrentWeather: "+selectedLanguage)
        viewModelScope.launch {
            try {
                weatherRepo.getWeather(latitude, longitude, apiKey,selectedUnit,selectedLanguage).catch { exception ->
                    _weatherData.value = WeatherState.Error(exception.message ?: "Unknown error")
                }.collect { response ->
                    _weatherData.value = WeatherState.Success(response)
                    Log.i("curr", "getCurrentWeather: ${_weatherData.value}")
                }
            } catch (e: IOException) {
                _weatherData.value = WeatherState.Error("Network error")
                e.printStackTrace()
            }
        }
    }


    fun getForecast(latitude: Double, longitude: Double, apiKey: String) {
        val selectedUnit = getSelectedUnit()
        val selectedLanguage = getSelectedLanguage()

        viewModelScope.launch {
            try {
                weatherRepo.getForecast(latitude, longitude, apiKey,selectedUnit,selectedLanguage).catch { exception ->
                    _forecastData.value = WeatherState.Error(exception.message ?: "Unknown error")
                }.collect { response ->
                    _forecastData.value = WeatherState.ForecastSuccess(response)
                    Log.i("hifor", "getForecast: $response")
                }
            } catch (e: IOException) {
                _forecastData.value = WeatherState.Error("Error fetching forecast data")
                e.printStackTrace()
            }
        }
    }

    fun searchWeatherByCity(cityName: String, apiKey: String) {
        viewModelScope.launch {
            try {
               weatherRepo.searchWeatherByCity(cityName,apiKey).catch { exception ->
                   _searchResults.value = WeatherState.Error(exception.message ?: "Unknown error")
               }.collect { response ->
                   _searchResults.value = WeatherState.Success(response)
                   Log.i("hifor", "getForecast: $response")
               }
            } catch (e: IOException) {
                _searchResults.value = WeatherState.Error("Error fetching forecast data")
                e.printStackTrace()
            }
        }
    }

}
class WeatherViewModelFactory(private val weatherRepo: WeatherRepo, private val sharedPreferencesManager: SharedPreferencesManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(weatherRepo,sharedPreferencesManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}