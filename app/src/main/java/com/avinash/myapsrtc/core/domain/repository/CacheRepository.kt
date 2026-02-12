package com.avinash.myapsrtc.core.domain.repository

import android.content.Context
import com.avinash.myapsrtc.core.domain.model.Place
import com.avinash.myapsrtc.core.domain.model.ServiceDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CacheRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val sharedPrefs = context.getSharedPreferences("tmp", Context.MODE_PRIVATE)

    val gson = Gson()
    fun getPlaces(): List<Place>?{
        val placesJson = sharedPrefs.getString("placesJson",null)
        placesJson?.let {
            if(it.isEmpty()){
               return null
            }

            val type = object : TypeToken<List<Place>>() {}.type
            val places: List<Place> = gson.fromJson(it, type)
            return places
        }
        return null
    }

    fun cachePlaces(places: List<Place>){
        val encodedJson = gson.toJson(places)
        sharedPrefs.edit().apply {
            putString("placesJson",encodedJson)
            apply()
        }
    }

    fun cacheServices(services: List<ServiceDetails>){
        val encodedJson = gson.toJson(services)
        sharedPrefs.edit().apply {
            putString("servicesJson",encodedJson)
            putLong("servicesCachedTime", System.currentTimeMillis())
            apply()
        }
    }

    val Service_Cache_Grace_Hours = 1

    fun getServices(route:Pair<String, String>): List<ServiceDetails>?{
        val cachedTime = sharedPrefs.getLong("servicesCachedTime-${route.first}-${route.second}",0)
        val currentTime = System.currentTimeMillis()

        if(currentTime - cachedTime > Service_Cache_Grace_Hours * 60 * 60 * 1000){
            return null
        }


        val servicesJson = sharedPrefs.getString("servicesJson-${route.first}-${route.second}",null)
        servicesJson?.let {
            if(it.isEmpty()){
                return null
            }

            val type = object : TypeToken<List<ServiceDetails>>() {}.type
            val services: List<ServiceDetails> = gson.fromJson(it, type)
            return services
        }
        return null

        }


}