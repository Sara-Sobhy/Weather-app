package com.example.weather.favourit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.model.WeatherEntity
import kotlin.math.log

class FavoriteAdapter(var weatherList: List<WeatherEntity>,
                      private val onDeleteWeather: (WeatherEntity) -> Unit,
                      private val onFavoriteItemClick: (WeatherEntity) -> Unit) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fav, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.bind(weather)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityNameTextView: TextView = itemView.findViewById(R.id.textViewDay)
        private val temperatureTextView: TextView = itemView.findViewById(R.id.textViewtemp)
        private val maxTextView: TextView = itemView.findViewById(R.id.textViewDis)
        private val imageIcon:ImageView=itemView.findViewById(R.id.icon)
        private val btnDelete:ImageView=itemView.findViewById(R.id.delete)
        private val card: ConstraintLayout =itemView.findViewById(R.id.cardClick)

        fun bind(weather: WeatherEntity) {
            cityNameTextView.text = weather.cityName
            Log.i("city", "bind: "+cityNameTextView.text)
            temperatureTextView.text = weather.temp.toString()
            maxTextView.text=weather.tempMax.toString()

            val weatherIcon = getWeatherIcon(weather.weatherId)
            imageIcon.setImageResource(weatherIcon)

            btnDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val deletedWeather = weatherList[position]
                    onDeleteWeather(deletedWeather)
                }
            }
            card.setOnClickListener {
                onFavoriteItemClick(weather)
            }

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
}