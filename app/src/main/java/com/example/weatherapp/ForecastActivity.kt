// ForecastActivity.kt
package com.example.weatherapp

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.adapter.ForecastAdapter

class ForecastActivity : AppCompatActivity() {

    private lateinit var currentCity: String
    private lateinit var currentCityCode: String
    private lateinit var tvCityNavText: TextView
    private lateinit var tvForecastNavText: TextView
    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        // 从Intent获取城市信息
        currentCity = intent.getStringExtra("cityName") ?: "广州"
        currentCityCode = intent.getStringExtra("cityCode") ?: "440100"

        // 初始化视图组件
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvCity = findViewById<TextView>(R.id.tv_city)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_forecast)

        // 底部导航组件
        val btnCityNav = findViewById<LinearLayout>(R.id.btn_city)
        val btnForecastNav = findViewById<LinearLayout>(R.id.btn_forecast)

        // 正确获取导航文本视图
        tvCityNavText = btnCityNav.getChildAt(1) as TextView
        tvForecastNavText = btnForecastNav.getChildAt(1) as TextView

        // 更新城市显示
        tvCity.text = currentCity

        // 设置RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        forecastAdapter = ForecastAdapter(emptyList())
        recyclerView.adapter = forecastAdapter

        // 底部导航点击事件
        btnCityNav.setOnClickListener {
            // 返回到MainActivity，并传递当前城市信息
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("cityName", currentCity)
                putExtra("cityCode", currentCityCode)
            }
            startActivity(intent)
            finish()
        }

        btnForecastNav.setOnClickListener {
            // 当前已在预报页面，刷新数据
            loadForecastData()
        }

        // 设置导航选中状态
        updateNavSelection(isCitySelected = false)

        // 加载天气预报数据
        loadForecastData()
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

    private fun loadForecastData() {
        lifecycleScope.launch {
            try {
                val weather = WeatherApi.getWeather(currentCityCode)

                if (weather?.status == "1" && weather.forecasts.isNotEmpty()) {
                    val forecast = weather.forecasts[0]

                    runOnUiThread {
                        // 更新RecyclerView数据
                        forecastAdapter.updateData(forecast.casts)
                    }
                } else {
                    runOnUiThread {
                        showToast("获取天气预报失败: ${weather?.info ?: "未知错误"}")
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    showToast("网络请求失败: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}