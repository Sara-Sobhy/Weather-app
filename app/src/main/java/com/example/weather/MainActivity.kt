package com.example.weather

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.weather.alert.Alert
import com.example.weather.alert.AlertReceiver
import com.example.weather.dataBase.LocalDataSource
import com.example.weather.dataBase.WeatherDatabase
import com.example.weather.dataBase.WeatherRepoImp
import com.example.weather.favourit.search
import com.example.weather.home.HomeFragment
import com.example.weather.home.modeView.WeatherViewModel
import com.example.weather.home.modeView.WeatherViewModelFactory
import com.example.weather.network.API
import com.example.weather.network.RemoteDataSource
import com.example.weather.setting.setting_fragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar
import kotlin.math.log

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.activity_main)
        instance=this
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)

        drawerLayout=findViewById(R.id.drawer)
        val navigationView=findViewById<NavigationView>(R.id.nav)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle= ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.home)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.home-> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()
           R.id.favourit-> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,search()).commit()
           R.id.alert-> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Alert()).commit()
           R.id.setting->supportFragmentManager.beginTransaction().replace(R.id.fragment_container,setting_fragment()).commit()
       }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "android",
                "android",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "My Channel Description"

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            Log.i("ree", "createNotificationChanneljust: ")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
       }else{
           onBackPressedDispatcher.onBackPressed()
       }
    }
    companion object {
        lateinit var instance: MainActivity
            private set
    }
}