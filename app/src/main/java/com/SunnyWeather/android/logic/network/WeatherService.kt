package com.SunnyWeather.android.logic.network


import com.SunnyWeather.android.SunnyWeatherApplication
import com.SunnyWeather.android.logic.model.DailyResponse
import com.SunnyWeather.android.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    //*https://api.caiyunapp.com/v2.5/{Token}/{经度, 纬度}/realtime.json*//
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng:String, @Path("lat") lat:String): Call<RealtimeResponse>
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng")lng:String, @Path("lat") lat:String): Call<DailyResponse>
}