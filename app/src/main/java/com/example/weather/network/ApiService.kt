package com.example.weather.network



import com.example.weather.model.ForecastResponse
import com.example.weather.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") languge: String
    ): WeatherResponse

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") languge: String
    ): ForecastResponse

    @GET("weather")
    suspend fun searchWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): WeatherResponse


}




