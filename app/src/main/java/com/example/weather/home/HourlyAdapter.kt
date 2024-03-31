package com.example.weather.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.model.Forecast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class HourlyAdapter: RecyclerView.Adapter<HourlyAdapter.WeatherViewHolder>() {

    private val forecastList: MutableList<Forecast> = mutableListOf()

    fun setForecastList(forecastList: List<Forecast>) {
        this.forecastList.clear()
        this.forecastList.addAll(forecastList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hourly_items, parent, false)
        return WeatherViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.bind(forecast)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textDate: TextView = itemView.findViewById(R.id.textViewHour)
        //private val textTime: TextView = itemView.findViewById(R.id.textViewDis)
        private val imageIcon: ImageView = itemView.findViewById(R.id.icon)
        private val textTemperature: TextView = itemView.findViewById(R.id.tvTemp)

        fun bind(forecast: Forecast) {

            val hourFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val dateFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())

            val hour = try {
                val date = hourFormat.parse(forecast.dt_txt)
                date?.let { calendar ->
                    dateFormat.format(calendar)
                } ?: "N/A"
            } catch (e: ParseException) {
                e.printStackTrace()
                "N/A"
            }
            textDate.text = hour
            //textTime.text = forecast.weather[0].description

            val tempInCelsius = forecast.main.temp
            textTemperature.text = String.format(Locale.getDefault(), "%.2f°C", tempInCelsius)

            val iconResourceId = getWeatherIcon(forecast.weather[0].id)
            imageIcon.setImageResource(iconResourceId)

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