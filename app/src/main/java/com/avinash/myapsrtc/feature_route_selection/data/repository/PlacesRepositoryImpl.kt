package com.avinash.myapsrtc.feature_route_selection.data.repository

import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.Place
import com.avinash.myapsrtc.core.domain.model.Waypoint
import com.avinash.myapsrtc.core.domain.repository.CacheRepository
import com.avinash.myapsrtc.feature_route_selection.data.mapper.toDomain
import com.avinash.myapsrtc.feature_route_selection.data.remote.PlacesApi
import com.avinash.myapsrtc.feature_route_selection.domain.repository.PlacesRepository
import jakarta.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesApi: PlacesApi,
    private val cacheRepository: CacheRepository

): PlacesRepository {
    override suspend fun getAllPlaces(): ApiState<List<Place>> {
        try{
            //Check cache first
            val cachedPlaces = cacheRepository.getPlaces()
            if(cachedPlaces!=null){
                return ApiState.Success(cachedPlaces)
            }

            val places = placesApi.fetchAllUniquePlaces()

            val mappedPlaces =  places.miniServicePlaces.map { it.toDomain() }

            cacheRepository.cachePlaces(mappedPlaces)
            return ApiState.Success(mappedPlaces)
        }catch (e: Exception){
            e.printStackTrace()
            return ApiState.Failure(e.stackTrace.toString())
        }
    }
}