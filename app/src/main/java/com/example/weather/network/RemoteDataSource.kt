package com.example.weather.network

import android.util.Log
import com.example.weather.model.ForecastResponse
import com.example.weather.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class RemoteDataSource(private val apiService: ApiService) : RemoteDataSourceInterface {
    override suspend fun getWeather(latitude: Double, longitude: Double, apiKey: String, units: String, languge:String): Flow<WeatherResponse> {
        return flow {
            val weatherResponse = apiService.getWeather(latitude, longitude, apiKey,units,languge)
            emit(weatherResponse)
        }
    }
    override suspend fun getForecast(latitude: Double, longitude: Double, apiKey: String, units: String, languge:String): Flow<ForecastResponse> {
        return flow {
            val forecastResponse=apiService.getForecast(latitude, longitude, apiKey,units,languge)
            emit(forecastResponse)
        }
    }

    /*suspend fun searchWeatherByCity(cityName: String, apiKey: String): Flow<WeatherResponse> {
        return flow {
            val searchemit=apiService.searchWeather(cityName, apiKey)
            emit(searchemit)
        }
    }*/
    override suspend fun searchWeatherByCity(cityName: String, apiKey: String): SharedFlow<WeatherResponse> {
        val sharedFlow = MutableSharedFlow<WeatherResponse>()

        flow {
            val searchResponse = apiService.searchWeather(cityName, apiKey)
            emit(searchResponse)
        }.catch { exception ->
            Log.i("error", "searchWeatherByCity: ")
        }.collect { response ->
            sharedFlow.emit(response)
        }

        return sharedFlow
    }
}


