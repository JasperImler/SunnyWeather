package com.SunnyWeather.android.ui.place

import androidx.lifecycle.*
import com.SunnyWeather.android.logic.Repository
import com.SunnyWeather.android.logic.model.Place

class PlaceViewModel:ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    val placeList = ArrayList<Place>()
    val placeLiveData = Transformations.switchMap(searchLiveData){
        query->Repository.searchPlaces(query)
    }
    fun searPlaces(query:String){
        searchLiveData.value = query
    }
}