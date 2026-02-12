package com.avinash.myapsrtc.feature_live_tracking.data.repository

import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.Place
import com.avinash.myapsrtc.core.domain.model.ServiceDetails

interface ServicesRepository {
    suspend fun getServicesForRoute(startPlace: Place,destinationPlace: Place): ApiState<List<ServiceDetails>>
}