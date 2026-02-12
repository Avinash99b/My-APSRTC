package com.avinash.myapsrtc.feature_live_tracking.data.remote

import com.avinash.myapsrtc.core.domain.model.ServiceDetails
import com.avinash.myapsrtc.feature_live_tracking.data.remote.dto.ApiStatusWrapper
import com.avinash.myapsrtc.feature_live_tracking.data.remote.dto.GetServicesForRouteDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ServicesApi {
    @POST("services/all")
    suspend fun getServicesForRoute(@Body getServicesForRouteDto: GetServicesForRouteDto): ApiStatusWrapper<List<ServiceDetails>>
}