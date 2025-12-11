package com.example.weatherapp

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.model.Forecast

class MainActivity : AppCompatActivity() {

    private lateinit var currentCity: String
    private lateinit var currentCityCode: String
    private lateinit var tvCityNavText: TextView
    private lateinit var tvForecastNavText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 从Intent获取城市信息（如果有的话）
        val intentCityName = intent.getStringExtra("cityName")
        val intentCityCode = intent.getStringExtra("cityCode")

        if (intentCityName != null && intentCityCode != null) {
            currentCity = intentCityName
            currentCityCode = intentCityCode
        } else {
            currentCity = "广州"
            currentCityCode = "440100"
        }

        // 初始化视图组件
        val btnRefresh = findViewById<ImageView>(R.id.btnRefresh)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // 天气显示相关组件
        val tvCity = findViewById<TextView>(R.id.tv_city)
        val tvTemp = findViewById<TextView>(R.id.tv_temp)
        val tvTempRange = findViewById<TextView>(R.id.tv_temp_range)
        val tvCurrentWeather = findViewById<TextView>(R.id.tv_current_weather)
        val tvDayWeather = findViewById<TextView>(R.id.tv_day_weather)
        val tvDayTemp = findViewById<TextView>(R.id.tv_day_temp)
        val tvDayWind = findViewById<TextView>(R.id.tv_day_wind)
        val tvNightWeather = findViewById<TextView>(R.id.tv_night_weather)
        val tvNightTemp = findViewById<TextView>(R.id.tv_night_temp)
        val tvNightWind = findViewById<TextView>(R.id.tv_night_wind)

        // 底部导航组件
        val btnCityNav = findViewById<LinearLayout>(R.id.btn_city)
        val btnForecastNav = findViewById<LinearLayout>(R.id.btn_forecast)

        // 正确获取导航文本视图的方法
        tvCityNavText = btnCityNav.getChildAt(1) as TextView
        tvForecastNavText = btnForecastNav.getChildAt(1) as TextView

        // 初始化城市标签
        initializeCityTags(
            tvCity = tvCity,
            tvCurrentWeather = tvCurrentWeather,
            tvTemp = tvTemp,
            tvTempRange = tvTempRange,
            tvDayWeather = tvDayWeather,
            tvDayTemp = tvDayTemp,
            tvDayWind = tvDayWind,
            tvNightWeather = tvNightWeather,
            tvNightTemp = tvNightTemp,
            tvNightWind = tvNightWind
        )

        // 刷新按钮点击事件
        btnRefresh.setOnClickListener {
            loadWeatherForCity(
                cityName = currentCity,
                cityCode = currentCityCode,
                progressBar = progressBar,
                tvCity = tvCity,
                tvCurrentWeather = tvCurrentWeather,
                tvTemp = tvTemp,
                tvTempRange = tvTempRange,
                tvDayWeather = tvDayWeather,
                tvDayTemp = tvDayTemp,
                tvDayWind = tvDayWind,
                tvNightWeather = tvNightWeather,
                tvNightTemp = tvNightTemp,
                tvNightWind = tvNightWind
            )
        }

        // 底部导航点击事件
        btnCityNav.setOnClickListener {
            // 城市页面已激活，无需处理
        }

        btnForecastNav.setOnClickListener {
            // 切换到预报页面
            switchToForecastView(currentCity, currentCityCode)
        }

        // 设置导航选中状态
        updateNavSelection(isCitySelected = true)

        // 首次加载天气数据
        loadWeatherForCity(
            cityName = currentCity,
            cityCode = currentCityCode,
            progressBar = progressBar,
            tvCity = tvCity,
            tvCurrentWeather = tvCurrentWeather,
            tvTemp = tvTemp,
            tvTempRange = tvTempRange,
            tvDayWeather = tvDayWeather,
            tvDayTemp = tvDayTemp,
            tvDayWind = tvDayWind,
            tvNightWeather = tvNightWeather,
            tvNightTemp = tvNightTemp,
            tvNightWind = tvNightWind
        )
    }

    private fun initializeCityTags(
        tvCity: TextView,
        tvCurrentWeather: TextView,
        tvTemp: TextView,
        tvTempRange: TextView,
        tvDayWeather: TextView,
        tvDayTemp: TextView,
        tvDayWind: TextView,
        tvNightWeather: TextView,
        tvNightTemp: TextView,
        tvNightWind: TextView
    ) {
        val cityTags = mapOf(
            R.id.tag_beijing to CityInfo("北京", "110000"),
            R.id.tag_shanghai to CityInfo("上海", "310000"),
            R.id.tag_guangzhou to CityInfo("广州", "440100"),
            R.id.tag_shenzhen to CityInfo("深圳", "440300")
        )

        cityTags.forEach { (viewId, cityInfo) ->
            findViewById<TextView>(viewId).setOnClickListener {
                // 更新当前选中的城市
                currentCity = cityInfo.name
                currentCityCode = cityInfo.code

                // 更新所有城市标签的选中状态
                updateCityTagsSelection(cityInfo.name)

                // 加载新城市的天气数据
                loadWeatherForCity(
                    cityName = cityInfo.name,
                    cityCode = cityInfo.code,
                    progressBar = findViewById(R.id.progressBar),
                    tvCity = tvCity,
                    tvCurrentWeather = tvCurrentWeather,
                    tvTemp = tvTemp,
                    tvTempRange = tvTempRange,
                    tvDayWeather = tvDayWeather,
                    tvDayTemp = tvDayTemp,
                    tvDayWind = tvDayWind,
                    tvNightWeather = tvNightWeather,
                    tvNightTemp = tvNightTemp,
                    tvNightWind = tvNightWind
                )
            }
        }

        // 初始化广州为选中状态
        updateCityTagsSelection(currentCity)
    }

    private fun updateCityTagsSelection(selectedCityName: String) {
        val cityTags = mapOf(
            R.id.tag_beijing to "北京",
            R.id.tag_shanghai to "上海",
            R.id.tag_guangzhou to "广州",
            R.id.tag_shenzhen to "深圳"
        )

        cityTags.forEach { (viewId, cityName) ->
            val cityTag = findViewById<TextView>(viewId)
            if (cityName == selectedCityName) {
                cityTag.setBackgroundResource(R.drawable.city_selected)
                cityTag.setTextColor(resources.getColor(R.color.selected_city_text, theme))
            } else {
                cityTag.setBackgroundResource(R.drawable.city_unselected)
                cityTag.setTextColor(resources.getColor(android.R.color.white, theme))
            }
        }
    }

    private fun updateNavSelection(isCitySelected: Boolean) {
        if (isCitySelected) {
            tvCityNavText.setTextColor(resources.getColor(android.R.color.white, theme))
            tvCityNavText.setTypeface(tvCityNavText.typeface, Typeface.BOLD)

            tvForecastNavText.setTextColor(resources.getColor(R.color.unselected_nav_text, theme))
            tvForecastNavText.setTypeface(tvForecastNavText.typeface, Typeface.NORMAL)
        } else {
            tvCityNavText.setTextColor(resources.getColor(R.color.unselected_nav_text, theme))
            tvCityNavText.setTypeface(tvCityNavText.typeface, Typeface.NORMAL)

            tvForecastNavText.setTextColor(resources.getColor(android.R.color.white, theme))
            tvForecastNavText.setTypeface(tvForecastNavText.typeface, Typeface.BOLD)
        }
    }

    private fun loadWeatherForCity(
        cityName: String,
        cityCode: String,
        progressBar: ProgressBar,
        tvCity: TextView,
        tvCurrentWeather: TextView,
        tvTemp: TextView,
        tvTempRange: TextView,
        tvDayWeather: TextView,
        tvDayTemp: TextView,
        tvDayWind: TextView,
        tvNightWeather: TextView,
        tvNightTemp: TextView,
        tvNightWind: TextView
    ) {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val weather = WeatherApi.getWeather(cityCode)

                if (weather?.status == "1" && weather.forecasts.isNotEmpty()) {
                    runOnUiThread {
                        updateWeatherUI(
                            forecast = weather.forecasts[0],
                            cityName = cityName,
                            tvCity = tvCity,
                            tvCurrentWeather = tvCurrentWeather,
                            tvTemp = tvTemp,
                            tvTempRange = tvTempRange,
                            tvDayWeather = tvDayWeather,
                            tvDayTemp = tvDayTemp,
                            tvDayWind = tvDayWind,
                            tvNightWeather = tvNightWeather,
                            tvNightTemp = tvNightTemp,
                            tvNightWind = tvNightWind
                        )
                    }
                } else {
                    showToast("获取天气失败: ${weather?.info ?: "未知错误"}")
                }
            } catch (e: Exception) {
                showToast("网络请求失败: ${e.message}")
            } finally {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun updateWeatherUI(
        forecast: Forecast,
        cityName: String,
        tvCity: TextView,
        tvCurrentWeather: TextView,
        tvTemp: TextView,
        tvTempRange: TextView,
        tvDayWeather: TextView,
        tvDayTemp: TextView,
        tvDayWind: TextView,
        tvNightWeather: TextView,
        tvNightTemp: TextView,
        tvNightWind: TextView
    ) {
        if (forecast.casts.isNotEmpty()) {
            val today = forecast.casts[0]

            // 更新UI
            tvCity.text = cityName
            tvCurrentWeather.text = today.dayweather

            // 计算平均温度
            val dayTemp = today.daytemp.toFloatOrNull() ?: 0f
            val nightTemp = today.nighttemp.toFloatOrNull() ?: 0f
            val avgTemp = (dayTemp + nightTemp) / 2

            tvTemp.text = "${avgTemp.toInt()}°"
            tvTempRange.text = "最高: ${today.daytemp}°  最低: ${today.nighttemp}°"
            tvDayWeather.text = today.dayweather
            tvDayTemp.text = "${today.daytemp}°"
            tvDayWind.text = "${today.daywind} ${today.daypower}级"
            tvNightWeather.text = today.nightweather
            tvNightTemp.text = "${today.nighttemp}°"
            tvNightWind.text = "${today.nightwind} ${today.nightpower}级"
        }
    }

    private fun switchToForecastView(cityName: String, cityCode: String) {
        // 更新导航选择状态
        updateNavSelection(isCitySelected = false)

        val intent = Intent(this, ForecastActivity::class.java).apply {
            putExtra("cityName", cityName)
            putExtra("cityCode", cityCode)
        }
        startActivity(intent)
        finish() // 关闭当前Activity
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    // 数据类用于存储城市信息
    data class CityInfo(val name: String, val code: String)
}