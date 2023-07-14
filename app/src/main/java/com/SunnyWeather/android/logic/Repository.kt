package com.SunnyWeather.android.logic

import androidx.lifecycle.liveData
import com.SunnyWeather.android.logic.dao.PlaceDao
import com.SunnyWeather.android.logic.model.Place
import com.SunnyWeather.android.logic.model.Weather
import com.SunnyWeather.android.logic.network.SunnyWeatherNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {
    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavePlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
    fun refreshWeather(lng:String, lat:String) = fire(Dispatchers.IO) {
            coroutineScope {
                val deferredRealtime = async {
                    SunnyWeatherNetWork.getRealtimeWeather(lng,lat)
                }
                val deferredDaily = async {
                    SunnyWeatherNetWork.getDailyWeather(lng,lat)
                }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok"){
                    val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status}"+
                                    "daily response is ${dailyResponse.status}"
                        )
                    )
                }
            }
    }

    fun searchPlaces(query:String) = fire(Dispatchers.IO){
            val placeResponse = SunnyWeatherNetWork.searchPlaces(query)
            if (placeResponse.status == "ok"){
                val places = placeResponse.places
                Result.success(places)
            }else   {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
    }
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}