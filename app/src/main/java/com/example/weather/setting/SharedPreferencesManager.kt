package com.example.weather.setting

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)

    fun saveSelectedUnit(unit: String) {
        sharedPreferences.edit().putString("selected_unit", unit).apply()
    }

    fun getSelectedUnit(): String {
        return sharedPreferences.getString("selected_unit", "metric") ?: "metric"
    }

    fun saveSelectedLanguage(language: String) {
        sharedPreferences.edit().putString("selected_language", language).apply()
    }

    fun getSelectedLanguage(): String {
        return sharedPreferences.getString("selected_language", "en") ?: "en"
    }
}