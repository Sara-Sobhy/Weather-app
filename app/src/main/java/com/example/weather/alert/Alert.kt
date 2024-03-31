package com.example.weather.alert

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.alert.viewmodel.AlarmViewModel
import com.example.weather.alert.viewmodel.AlarmViewModelFactory
import com.example.weather.dataBase.LocalDataSource
import com.example.weather.dataBase.WeatherDatabase
import com.example.weather.dataBase.WeatherRepoImp
import com.example.weather.map.mapFragment
import com.example.weather.model.Alarm
import com.example.weather.network.API
import com.example.weather.network.RemoteDataSource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar


class Alert : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedday = 0
    var savedmonth = 0
    var savedyear = 0
    var savedhour = 0
    var savedminute = 0

    lateinit var btn_set: Button
    lateinit var alarmCheckBox: CheckBox
    lateinit var alramsFloatbutton:FloatingActionButton
    lateinit var  viewModel: AlarmViewModel
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_alert, container, false)
        btn_set = root.findViewById(R.id.set)
        alarmCheckBox = root.findViewById(R.id.alarmCheckBox)
        alramsFloatbutton=root.findViewById(R.id.floatingActionButton)

        arguments?.let { args ->
            // Retrieve latitude and longitude from arguments bundle
            latitude = args.getDouble("latitude", 0.0)
            longitude = args.getDouble("longitude", 0.0)
            Log.i("latt", "onCreateView: "+latitude)
        }
        btn_set.setOnClickListener {
            pickDate()
        }

        alramsFloatbutton.setOnClickListener{
            val anotherFragment = AlarmFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, anotherFragment)
                .addToBackStack(null)
                .commit()
        }

        return root
    }

    private fun pickDate() {
        getDateTimeFrom()
        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    private fun getDateTimeFrom() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR_OF_DAY) // Use HOUR_OF_DAY to get 24-hour format
        minute = cal.get(Calendar.MINUTE)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedday = dayOfMonth
        savedmonth = month
        savedyear = year

        getDateTimeFrom()
        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedhour = hourOfDay
        savedminute = minute
        scheduleNotification()
    }

    private fun scheduleNotification() {
        val calendar = Calendar.getInstance()
        calendar.set(savedyear, savedmonth, savedday, savedhour, savedminute)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlertReceiver::class.java)


        intent.putExtra("isAlarm", alarmCheckBox.isChecked)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            123,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (alarmCheckBox.isChecked) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Log.i("alert", "scheduleNotification: $calendar")

            Log.i("latt", "Latitude value: $latitude")

            val alarm = Alarm(
                timeInMillis = calendar.timeInMillis,
                isAlarm = true,
                latitude = latitude,
                longitude = longitude
            )
            Log.i("latt", "scheduleNotification: "+alarm+latitude)
            val productDao = WeatherDatabase.getInstance(requireContext().applicationContext).weatherDao()
            val alramDao = WeatherDatabase.getInstance(requireContext().applicationContext).alarmDao()
            val localDataSource = LocalDataSource(productDao,alramDao)
            val remoteDataSource = RemoteDataSource(API.retrofitService)
            val productRepo = WeatherRepoImp(remoteDataSource, localDataSource)
            val viewModelFactory = AlarmViewModelFactory(productRepo)
            viewModel = ViewModelProvider(this, viewModelFactory).get(AlarmViewModel::class.java)

            lifecycleScope.launch {
                viewModel.insertAlram(alarm)
            }


        } else {
            // If notification is selected, schedule the notification
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Log.i("alert", "scheduleNotification: $calendar")

        }
    }
    companion object {
        fun newInstance(latitude: Double, longitude: Double): Alert {
            val fragment = Alert()
            val bundle = Bundle().apply {
                putDouble("latitude", latitude)
                putDouble("longitude", longitude)
            }
            Log.i("latt", "newInstance: "+longitude)
            fragment.arguments = bundle
            return fragment
        }
    }
}