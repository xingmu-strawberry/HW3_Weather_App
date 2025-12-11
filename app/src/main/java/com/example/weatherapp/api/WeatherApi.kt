package com.example.weatherapp.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.weatherapp.model.WeatherResponse

object WeatherApi {
    private const val API_KEY = "99aad57a269eecc7372709b37f32b77a"
    private const val BASE_URL = "https://restapi.amap.com/v3/weather/weatherInfo"
    private val client = OkHttpClient()
    private val gson = Gson()

    /**
     * 获取天气信息
     * @param cityCode 城市编码，默认为广州（440100）
     * @param extensions 预报类型：base-实时天气，all-预报天气
     */
    suspend fun getWeather(
        cityCode: String = "440100",
        extensions: String = "all"
    ): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val url = "$BASE_URL?city=$cityCode&extensions=$extensions&key=$API_KEY"
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val json = response.body?.string()
                    return@withContext gson.fromJson(json, WeatherResponse::class.java)
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * 获取城市列表（用于城市搜索，可选）
     */
    suspend fun searchCity(keyword: String): List<CityInfo>? {
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://restapi.amap.com/v3/config/district?keywords=$keyword&subdistrict=0&key=$API_KEY"
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val json = response.body?.string()
                    val data = gson.fromJson(json, CitySearchResponse::class.java)
                    return@withContext data.districts
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

// 城市搜索相关数据类（放在同一个文件中）
data class CitySearchResponse(
    val status: String,
    val info: String,
    val districts: List<CityInfo>
)

data class CityInfo(
    val citycode: String,
    val adcode: String,
    val name: String,
    val center: String,
    val level: String
)

