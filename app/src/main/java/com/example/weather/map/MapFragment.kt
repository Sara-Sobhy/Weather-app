package com.example.weather.map

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.dataBase.LocalDataSource
import com.example.weather.dataBase.WeatherDatabase
import com.example.weather.dataBase.WeatherRepoImp
import com.example.weather.favourit.search
import com.example.weather.home.key
import com.example.weather.modeView.WeatherViewModel
import com.example.weather.modeView.WeatherViewModelFactory
import com.example.weather.model.WeatherEntity
import com.example.weather.model.WeatherResponse
import com.example.weather.network.API
import com.example.weather.network.RemoteDataSource
import com.example.weather.network.WeatherState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class mapFragment : Fragment() ,OnMapReadyCallback{
    lateinit var supportMapFragment:SupportMapFragment
    lateinit var button:Button
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root= inflater.inflate(R.layout.fragment_map, container, false)
        supportMapFragment = childFragmentManager.findFragmentById(R.id.My_Map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        val productDao = WeatherDatabase.getInstance(requireContext().applicationContext).weatherDao()
        val localDataSource = LocalDataSource(productDao)

        val remoteDataSource = RemoteDataSource(API.retrofitService)
        val productRepo = WeatherRepoImp(remoteDataSource, localDataSource)
        val viewModelFactory = WeatherViewModelFactory(productRepo)
        weatherViewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        button=root.findViewById(R.id.add)
        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setOnMapClickListener { latLng ->
            val latitude = latLng.latitude
            val longitude = latLng.longitude
            val location = LatLng(latitude, longitude)

            googleMap.clear()

            val markerOptions = MarkerOptions().position(location)
            googleMap.addMarker(markerOptions)

            val snippet = "Latitude: $latitude, Longitude: $longitude"
            googleMap.addMarker(markerOptions.snippet(snippet))
            Log.i("lon", "onMapReady: "+snippet)

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

            button.setOnClickListener(){
                getWeatherData(latitude, longitude)
            }
        }
    }

    private fun getWeatherData(latitude: Double, longitude: Double) {

        weatherViewModel.getCurrentWeather(latitude, longitude,key)
        weatherViewModel.weatherData.onEach { weatherState ->
            when (weatherState) {
                is WeatherState.Success<*> -> {
                    val weatherResponse = weatherState.data as WeatherResponse
                    val weatherEntity = WeatherEntity(
                        lon = longitude,
                        lat = latitude,
                        weatherId = weatherResponse.weather[0].id,
                        main = weatherResponse.weather[0].main,
                        description = weatherResponse.weather[0].description,
                        icon = weatherResponse.weather[0].icon,
                        cityName = weatherResponse.name,
                        country = weatherResponse.sys.country ?: "",
                        temp = weatherResponse.main.temp,
                        feelsLike = weatherResponse.main.feels_like,
                        tempMin = weatherResponse.main.temp_min,
                        tempMax = weatherResponse.main.temp_max,
                        pressure = weatherResponse.main.pressure,
                        humidity = weatherResponse.main.humidity,
                        windSpeed = weatherResponse.wind.speed,
                        windDegree = weatherResponse.wind.deg,
                        rain1h = 0.0, // Adjust according to your weather response
                        cloudiness = weatherResponse.clouds.all,
                        sunrise = weatherResponse.sys.sunrise,
                        sunset = weatherResponse.sys.sunset
                    )
                    weatherViewModel.saveWeatherData(weatherEntity)
                    navigateToFavoriteFragment()
                }
                is WeatherState.Error -> {
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
    }
    private fun navigateToFavoriteFragment() {
        val destinationFragment = search()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, destinationFragment)
        fragmentTransaction.commit()
    }

}