package com.example.weather.home

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.alert.Alert
import com.example.weather.alert.AlertReceiver
//import com.example.weather.alert.Alram_fragment
import com.example.weather.dataBase.LocalDataSource
import com.example.weather.dataBase.WeatherDatabase
import com.example.weather.dataBase.WeatherRepoImp
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.home.modeView.WeatherViewModel
import com.example.weather.home.modeView.WeatherViewModelFactory
import com.example.weather.model.Forecast
import com.example.weather.model.WeatherResponse
import com.example.weather.network.API
import com.example.weather.network.RemoteDataSource
import com.example.weather.network.WeatherState
import com.example.weather.setting.SharedPreferencesManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.log

const val key:String="137312c48108850ddb9ff6448da1156b"


class HomeFragment : Fragment(), LocationListener {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var hourlyAdapter: HourlyAdapter

    private lateinit var recyclerViewD: RecyclerView
    private lateinit var daily: DailyAdapter

    private lateinit var locationManager: LocationManager
    private lateinit var weatherViewModel: WeatherViewModel
    private val locationPermissionCode = 2
    private var lon: Double = 0.0
    private var lat: Double = 0.0

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val rootView = binding.root

        recyclerView=rootView.findViewById(R.id.RCHouly)
        recyclerViewD=rootView.findViewById(R.id.Rc)


        val productDao = WeatherDatabase.getInstance(requireContext().applicationContext).weatherDao()
        val alramDao = WeatherDatabase.getInstance(requireContext().applicationContext).alarmDao()
        val localDataSource = LocalDataSource(productDao,alramDao)
        val remoteDataSource = RemoteDataSource(API.retrofitService)
        val productRepo = WeatherRepoImp(remoteDataSource, localDataSource)

        sharedPreferencesManager = SharedPreferencesManager(requireContext())

        val viewModelFactory = WeatherViewModelFactory(productRepo,sharedPreferencesManager)
        weatherViewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        recyclerViewD.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        daily=DailyAdapter()
        recyclerViewD.adapter=daily

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        hourlyAdapter = HourlyAdapter()
        recyclerView.adapter = hourlyAdapter


        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { cityName ->
                    weatherViewModel.searchWeatherByCity(cityName, key)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return false
            }

        })
        lifecycleScope.launch {
            weatherViewModel.searchResults.collectLatest { weatherState ->
                when (weatherState) {
                    is WeatherState.Success<*> -> {
                        val weatherResponse = weatherState.data as WeatherResponse
                        binding.main.text = weatherResponse.weather[0].description
                        binding.temp.text = (weatherResponse.main.temp - 273.15).toString()
                        binding.textView10.text = weatherResponse.timezone.toString()

                        binding.textView1.text=getString(R.string.hourly)
                        binding.textView4.text=getString(R.string.daily)

                        val maxTemp = (weatherResponse.main.temp_max - 273.15).toInt()
                        val minTemp = (weatherResponse.main.temp_min - 273.15).toInt()
                        binding.maxmin.text = "$maxTemp°C $minTemp°C"

                        val unixTimestampSeconds: Long = weatherResponse.dt
                        val date = Date(unixTimestampSeconds * 1000L)
                        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                        val dayOfWeek = dayFormat.format(date)
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                        val formattedDate = sdf.format(date)
                        binding.textView10.text = "$dayOfWeek: $formattedDate"

                        val iconResourceId = getWeatherIcon(weatherResponse.weather[0].id)
                        binding.imageView.setImageResource(iconResourceId)
                    }

                    is WeatherState.Error -> {
                        Toast.makeText(requireContext(), "Error searching weather data", Toast.LENGTH_SHORT).show()
                    }

                    WeatherState.Loading -> {
                        Log.d("Weather", "Searching")
                    }

                    else -> {}
                }
            }
        }

        binding.location.setOnClickListener(){
            fetchWeatherData(lat,lon)
        }

        val cityName = arguments?.getString("city_name")
        val temperature = arguments?.getDouble("temperature")
        val weatherDescription = arguments?.getString("weather_description")
        val icon=arguments?.getInt("icon")

        if (cityName != null && temperature != null && weatherDescription != null && icon != null) {
            rootView.apply {
                findViewById<TextView>(R.id.main).text = cityName
                findViewById<TextView>(R.id.temp).text = temperature.toString()
                findViewById<TextView>(R.id.maxmin).text = weatherDescription
                findViewById<ImageView>(R.id.imageView).setImageResource(icon)

            }
        } else {
            getLocation()
        }

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu,menu)
    }


    private fun getLocation() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        lat = location.latitude
        lon = location.longitude

        fetchWeatherData(lat, lon)
        Log.i("latt", "onLocationChanged: "+lat)

        locationManager.removeUpdates(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double) {


        weatherViewModel.getCurrentWeather(latitude, longitude, key)
        weatherViewModel.getForecast(latitude,longitude, key)

        lifecycleScope.launch {
            weatherViewModel.weatherData.collectLatest { weatherState ->
                when (weatherState) {
                    is WeatherState.Success<*> -> {
                        val weatherResponse = weatherState.data as WeatherResponse
                        Log.i("Weather", "Success: $weatherResponse")
                        binding.main.text = weatherResponse.weather[0].description
                        binding.temp.text = (weatherResponse.main.temp).toString()
                        binding.textView10.text = weatherResponse.timezone.toString()

                        binding.textView1.text=getString(R.string.hourly)
                        binding.textView4.text=getString(R.string.daily)

                        val maxTemp = (weatherResponse.main.temp_max).toInt()
                        val minTemp = (weatherResponse.main.temp_min).toInt()
                        binding.maxmin.text = "$maxTemp°C $minTemp°C"

                        // Convert the Unix timestamp to a human-readable date
                        val unixTimestampSeconds: Long = weatherResponse.dt
                        val date = Date(unixTimestampSeconds * 1000L)
                        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                        val dayOfWeek = dayFormat.format(date)
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                        val formattedDate = sdf.format(date)
                        binding.textView10.text = "$dayOfWeek: $formattedDate"

                        val iconResourceId = getWeatherIcon(weatherResponse.weather[0].id)
                        binding.imageView.setImageResource(iconResourceId)

                    }

                    is WeatherState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Error fetching weather data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    WeatherState.Loading -> {
                        Log.d("Weather", "Loading")
                    }

                    else -> {}
                }
            }
        }


        lifecycleScope.launch {
            weatherViewModel.forecastData.collectLatest { forecastState ->
                when (forecastState) {
                    is WeatherState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Error fetching forecast data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is WeatherState.Loading -> {
                        Log.d("inn", "Loading")
                    }

                    is WeatherState.ForecastSuccess -> {
                        Log.i("inn", "fetchWeatherData: ")
                        val forecastList = forecastState.forecastResponse.list
                        val uniqueDaysForecast = getUniqueDaysForecast(forecastList)
                        Log.i("inn", "fetchWeatherData:oooo "+uniqueDaysForecast)
                        hourlyAdapter.setForecastList(forecastList)
                        daily.setForecastList(uniqueDaysForecast)
                    }

                    else -> {}
                }
            }
        }

        try {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address: Address? = addresses?.get(0)
                    val country = address?.countryName
                    val city = address?.locality
                    val addressString = "$city, $country"
                    binding.location.text = addressString
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val alertFragment = Alert.newInstance(lat, lon)
        Log.i("latt", "fetchWeatherData: "+lat)

        //passing the lat and lon to the alert
        val alarmManager =  requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertReceiver::class.java).apply {
            putExtra("latitude", lat)
            putExtra("longitude", lon)
            Log.i("latt", "onCreateView: "+lat+lon)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)



    }
    private fun getUniqueDaysForecast(forecastList: List<Forecast>): List<Forecast> {
        val uniqueDays = mutableSetOf<String>()
        val uniqueDaysForecast = mutableListOf<Forecast>()

        for (forecast in forecastList) {
            val day = getDayFromDate(forecast.dt_txt)

            if (day !in uniqueDays) {
                uniqueDays.add(day)
                uniqueDaysForecast.add(forecast)
            }
        }

        return uniqueDaysForecast
    }
    private fun getDayFromDate(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dayFormat.format(date)
    }
    private fun getWeatherIcon(weatherId: Int): Int {
        return when (weatherId) {
            in 200..232 -> R.drawable.cloudy_rainy_svgrepo_com
            in 300..321 -> R.drawable.cloudy_svgrepo_com
            in 500..531 -> R.drawable.cloudy_rainy_svgrepo_com
            in 600..622 -> R.drawable.snow_showers_svgrepo_com
            in 701..781 -> R.drawable.sun_svgrepo_com
            800 -> R.drawable.sun_svgrepo_com
            in 801..804 -> R.drawable.cloudy_svgrepo_com
            else -> R.drawable.sun_svgrepo_com
        }
    }


}