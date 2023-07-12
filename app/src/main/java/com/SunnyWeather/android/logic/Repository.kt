package com.SunnyWeather.android.logic

import androidx.lifecycle.liveData
import com.SunnyWeather.android.logic.model.Place
import com.SunnyWeather.android.logic.network.SunnyWeatherNetWork
import kotlinx.coroutines.Dispatchers

object Repository {
    fun searchPlaces(query:String) = liveData(Dispatchers.IO){
        val  result = try {
            val placeResponse = SunnyWeatherNetWork.searchPlaces(query)
            if (placeResponse.status == "OK"){
                val places = placeResponse.places
                Result.success(places)
            }else   {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e:Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
}