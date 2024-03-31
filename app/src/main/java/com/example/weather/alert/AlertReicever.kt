package com.example.weather.alert

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.util.Log
import android.view.WindowManager
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.example.weather.MainActivity
import com.example.weather.R
import com.example.weather.dataBase.LocalDataSource
import com.example.weather.dataBase.WeatherDatabase
import com.example.weather.dataBase.WeatherRepo
import com.example.weather.dataBase.WeatherRepoImp
import com.example.weather.home.key
import com.example.weather.home.modeView.WeatherViewModel
import com.example.weather.home.modeView.WeatherViewModelFactory
import com.example.weather.model.Main
import com.example.weather.model.WeatherResponse
import com.example.weather.network.API
import com.example.weather.network.RemoteDataSource
import com.example.weather.network.WeatherState
import com.example.weather.setting.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class AlertReceiver : BroadcastReceiver() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    companion object {
        const val ACTION_STOP_ALARM = "com.example.weather.alert.ACTION_STOP_ALARM"
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("hi", "onReceivehiiiiiiiiii: ")
        if (context != null&& intent != null) {
            val isAlarm = intent.getBooleanExtra("isAlarm", false)
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i("hi", "onReceive:afterrr ")

                val latitude = intent?.getDoubleExtra("latitude", 0.0)
                val longitude = intent?.getDoubleExtra("longitude", 0.0)

                val productDao = WeatherDatabase.getInstance(context.applicationContext).weatherDao()
                val alramDao = WeatherDatabase.getInstance(context.applicationContext).alarmDao()
                val localDataSource = LocalDataSource(productDao,alramDao)
                val remoteDataSource = RemoteDataSource(API.retrofitService)
                val weatherRepo = WeatherRepoImp(remoteDataSource, localDataSource)

                sharedPreferencesManager = SharedPreferencesManager(context)

                val viewModelFactory = WeatherViewModelFactory(weatherRepo,sharedPreferencesManager)

                val mainActivityIntent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    mainActivityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                GlobalScope.launch {
                    val weatherViewModel = ViewModelProvider(
                        MainActivity.instance,
                        viewModelFactory
                    ).get(WeatherViewModel::class.java)

                    if (latitude != null && longitude != null) {
                        weatherViewModel.getCurrentWeather(latitude, longitude, key)
                    }

                    weatherViewModel.weatherData.collectLatest { weatherState ->
                        when (weatherState) {
                            is WeatherState.Success<*> -> {
                                val weatherData = weatherState.data as WeatherResponse

                                val currentWeather = weatherData.weather[0]
                                val temperature = currentWeather.description
                                val weatherDescription = currentWeather.main

                                val notificationContent =
                                    "Current temperature: $temperature°C, Weather: $weatherDescription"

                                Log.i("alarmmm", "onReceive:alrammmm "+isAlarm)

                                val builder = if (isAlarm) {
                                    Log.i("alarmmm", "onReceive:alrammmm ")
                                    // Alarm-style notification with sound
                                    mediaPlayer = MediaPlayer.create(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                                    mediaPlayer?.isLooping = true
                                    mediaPlayer?.start()

                                    // Action to stop the alarm sound
                                    val stopIntent = Intent(context, AlarmStopReceiver::class.java).apply {
                                        action = AlertReceiver.ACTION_STOP_ALARM
                                    }
                                    val stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


                                    NotificationCompat.Builder(context, "android")
                                        .setSmallIcon(R.drawable.alert_svgrepo_com)
                                        .setContentTitle("Alarm")
                                        .setContentText(notificationContent)
                                        .setAutoCancel(true)
                                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .addAction(R.drawable.delete_svgrepo_com, "Stop Alarm", stopPendingIntent)

                                } else {
                                    // Regular notification
                                    Log.i("alrammm", "onReceive:regular ")
                                    NotificationCompat.Builder(context, "android")
                                        .setSmallIcon(R.drawable.alert_svgrepo_com)
                                        .setContentTitle("Weather Update")
                                        .setContentText(notificationContent)
                                        .setAutoCancel(true)
                                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setContentIntent(pendingIntent)
                                }

                                val notificationManager = NotificationManagerCompat.from(context)
                                notificationManager.notify(123, builder.build())
                            }

                            is WeatherState.Error -> {
                                // Handle error case
                            }

                            else -> {
                                // Handle other states if needed
                            }
                        }
                    }
                }
            } else {
                // Permission is not granted, request it from the user
                // Handle the permission request in your activity or fragment
                // You can use ActivityCompat.requestPermissions() method
            }

        }
    }
    fun stopAlarmSound() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
    }
}


