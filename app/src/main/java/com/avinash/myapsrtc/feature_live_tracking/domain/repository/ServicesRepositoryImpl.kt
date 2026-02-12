package com.avinash.myapsrtc.feature_live_tracking.domain.repository

import android.util.Log
import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.Place
import com.avinash.myapsrtc.core.domain.model.ServiceDetails
import com.avinash.myapsrtc.core.domain.repository.CacheRepository
import com.avinash.myapsrtc.feature_live_tracking.data.remote.ServicesApi
import com.avinash.myapsrtc.feature_live_tracking.data.remote.dto.GetServicesForRouteDto
import com.avinash.myapsrtc.feature_live_tracking.data.repository.ServicesRepository
import jakarta.inject.Inject
import java.lang.Exception

class ServicesRepositoryImpl @Inject constructor(
    val servicesApi: ServicesApi,
    val cacheRepository: CacheRepository
): ServicesRepository {
    override suspend fun getServicesForRoute(
        startPlace: Place,
        destinationPlace: Place
    ): ApiState<List<ServiceDetails>> {
        try{
            val cachedServices = cacheRepository.getServices(Pair(startPlace.placeId,destinationPlace.placeId))
            if(cachedServices != null){
                Log.d("ServiceRepository","Using cached services")
                return ApiState.Success(cachedServices)
            }

            val services = servicesApi.getServicesForRoute(GetServicesForRouteDto(
                destinationLinkId = destinationPlace.linkPlaceId.toInt(),
                destinationPlaceId = destinationPlace.placeId.toInt(),
                sourceLinkId = startPlace.linkPlaceId.toInt(),
                sourcePlaceId = startPlace.placeId.toInt()
            ))

            cacheRepository.cacheServices(services.data)

            return ApiState.Success(services.data)
        }catch (e: Exception){
            e.printStackTrace()
            return ApiState.Failure(e.stackTrace.toString())
        }
    }
}