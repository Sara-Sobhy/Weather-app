package com.example.weather.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.weather.R

class setting_fragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView= inflater.inflate(R.layout.fragment_setting_fragment, container, false)

        // Retrieve the current unit preference from SharedPreferences
        val currentUnit = sharedPreferences.getString("unit", "metric")

        // Retrieve the current language preference from SharedPreferences
        val currentLanguage = sharedPreferences.getString("lang", "en")

        // Find RadioGroup and set the checked radio button based on the current unit preference
        val radioGroup = rootView.findViewById<RadioGroup>(R.id.radioGroupUnit)
        when (currentUnit) {
            "metric" -> radioGroup.check(R.id.radioButtonMetric)
            "imperial" -> radioGroup.check(R.id.radioButtonImperial)
            "standard" -> radioGroup.check(R.id.radioButtonStandard)
        }

        // Set listener for radio group to handle unit selection changes
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = rootView.findViewById<RadioButton>(checkedId)
            val selectedUnit = radioButton.text.toString()
            // Save selected unit preference to SharedPreferences
            with(sharedPreferences.edit()) {
                putString("unit", selectedUnit)
                apply()
            }
            sharedPreferencesManager.saveSelectedUnit(selectedUnit)
        }

         //for langugue
        val radioGroupLanguage = rootView.findViewById<RadioGroup>(R.id.radioGroupLanguage)
        when (currentLanguage) {
            "en" -> radioGroupLanguage.check(R.id.radioButtonEnglish)
            "ar"->radioGroupLanguage.check(R.id.radioButtonArabic)
            //"fr" -> radioGroupLanguage.check(R.id.radioButtonFrench)
            //"es" -> radioGroupLanguage.check(R.id.radioButtonSpanish)
        }

        // Set listener for language selection changes
        radioGroupLanguage.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = rootView.findViewById<RadioButton>(checkedId)
            val selectedLanguage = when (radioButton.id) {
                R.id.radioButtonEnglish -> "en"
                R.id.radioButtonArabic -> "ar"
                else -> "en" // Default to English if none selected
            }
            // Save selected language preference to SharedPreferences
            with(sharedPreferences.edit()) {
                putString("lang", selectedLanguage)
                apply()
            }
            sharedPreferencesManager.saveSelectedLanguage(selectedLanguage)
        }

        return rootView
    }

    companion object {

    }
}