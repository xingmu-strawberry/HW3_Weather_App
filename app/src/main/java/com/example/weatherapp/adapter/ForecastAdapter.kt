// ForecastAdapter.kt
package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.Cast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ForecastAdapter(private var forecasts: List<Cast>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDayLabel: TextView = itemView.findViewById(R.id.tv_day_label)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvIcon: TextView = itemView.findViewById(R.id.tv_icon)
        val tvWeather: TextView = itemView.findViewById(R.id.tv_weather)
        val tvMaxTemp: TextView = itemView.findViewById(R.id.tv_max_temp)
        val tvMinTemp: TextView = itemView.findViewById(R.id.tv_min_temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position >= forecasts.size) return

        val forecast = forecasts[position]

        // 设置日期显示 (MM-DD格式)
        val monthDay = formatMonthDay(forecast.date)
        holder.tvDate.text = monthDay

        // 设置日期标签
        val dayLabel = getDayLabel(position)
        holder.tvDayLabel.text = dayLabel

        // 设置天气图标（使用白天天气）
        holder.tvIcon.text = getWeatherIcon(forecast.dayweather)

        // 设置天气状态
        holder.tvWeather.text = forecast.dayweather

        // 设置温度
        holder.tvMaxTemp.text = "${forecast.daytemp}°"
        holder.tvMinTemp.text = "${forecast.nighttemp}°"
    }

    override fun getItemCount(): Int = forecasts.size

    fun updateData(newForecasts: List<Cast>) {
        // 确保只显示7天的数据
        val limitedForecasts = if (newForecasts.size > 7) {
            newForecasts.take(7)
        } else {
            newForecasts
        }
        this.forecasts = limitedForecasts
        notifyDataSetChanged()
    }

    private fun formatMonthDay(dateStr: String): String {
        return try {
            // 假设日期格式是 "yyyy-MM-dd"
            dateStr.substring(5) // 直接截取 MM-DD
        } catch (e: Exception) {
            dateStr
        }
    }

    private fun getDayLabel(position: Int): String {
        // 获取今天日期
        val calendar = Calendar.getInstance()

        return when (position) {
            0 -> "今天"
            1 -> "明天"

            else -> {
                // 计算未来的日期
                calendar.add(Calendar.DAY_OF_YEAR, position)
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                val dayNames = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")
                dayNames[dayOfWeek - 1]
            }
        }
    }

    private fun getWeatherIcon(weather: String): String {
        return when {
            weather.contains("晴") -> "☀️"
            weather.contains("多云") -> "⛅"
            weather.contains("阴") -> "☁️"
            weather.contains("雨") -> "🌧️"
            weather.contains("小雨") -> "🌦️"
            weather.contains("中雨") -> "🌧️"
            weather.contains("大雨") -> "🌧️☔"
            weather.contains("暴雨") -> "⛈️"
            weather.contains("雪") -> "❄️"
            weather.contains("小雪") -> "🌨️"
            weather.contains("中雪") -> "❄️"
            weather.contains("大雪") -> "❄️☃️"
            weather.contains("雷") -> "⛈️"
            weather.contains("雾") -> "🌫️"
            weather.contains("霾") -> "😷"
            weather.contains("风") -> "💨"
            weather.contains("扬沙") -> "🌪️"
            else -> "🌤️"
        }
    }
}