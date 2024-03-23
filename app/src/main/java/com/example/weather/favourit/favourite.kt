package com.example.weather.favourit

import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.dataBase.LocalDataSource
import com.example.weather.dataBase.WeatherDatabase
import com.example.weather.dataBase.WeatherRepoImp
import com.example.weather.home.HomeFragment
import com.example.weather.map.mapFragment
import com.example.weather.modeView.WeatherViewModel
import com.example.weather.modeView.WeatherViewModelFactory
import com.example.weather.model.WeatherEntity
import com.example.weather.network.API
import com.example.weather.network.RemoteDataSource
import com.google.android.material.floatingactionbutton.FloatingActionButton

class search : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fcb:FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_fav, container, false)

        recyclerView = rootView.findViewById(R.id.RCFav)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        favoriteAdapter = FavoriteAdapter(emptyList(),
            onDeleteWeather = { deletedWeather ->
                weatherViewModel.deleteWeatherData(deletedWeather)
            },
            onFavoriteItemClick = { clickedWeather ->
                navigateToHomeFragment(clickedWeather.cityName,clickedWeather.temp,clickedWeather.description)
            }
        )
        recyclerView.adapter = favoriteAdapter
        fcb=rootView.findViewById(R.id.fabOpenMap)

        fcb.setOnClickListener{
            val anotherFragment = mapFragment() // Replace AnotherFragment with the actual fragment you want to navigate to
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, anotherFragment)
                .addToBackStack(null)
                .commit()
        }


        val productDao = WeatherDatabase.getInstance(requireContext().applicationContext).weatherDao()
        val localDataSource = LocalDataSource(productDao)
        val remoteDataSource = RemoteDataSource(API.retrofitService)
        val productRepo = WeatherRepoImp(remoteDataSource, localDataSource)
        val viewModelFactory = WeatherViewModelFactory(productRepo)
        weatherViewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        lifecycleScope.launchWhenStarted {
            weatherViewModel.getAllWeather().collect { weatherList ->
                favoriteAdapter.weatherList = weatherList
                favoriteAdapter.notifyDataSetChanged()
            }
        }

        return rootView
    }
    private fun navigateToHomeFragment(cityName: String, temperature: Double, weatherDescription: String) {
        val homeFragment = HomeFragment().apply {
            arguments = Bundle().apply {
                putString("city_name", cityName)
                putDouble("temperature", temperature)
                putString("weather_description", weatherDescription)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .addToBackStack(null)
            .commit()
    }
}