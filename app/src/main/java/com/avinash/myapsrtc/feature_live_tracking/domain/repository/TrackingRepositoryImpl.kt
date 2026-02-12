package com.avinash.myapsrtc.feature_live_tracking.domain.repository

import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.TrackingDetails
import com.avinash.myapsrtc.feature_live_tracking.data.remote.TrackingApi
import com.avinash.myapsrtc.feature_live_tracking.data.remote.dto.TrackingDetailsDto
import com.avinash.myapsrtc.feature_live_tracking.data.remote.mappers.toDataClass
import com.avinash.myapsrtc.feature_live_tracking.data.remote.mappers.toDomain
import com.avinash.myapsrtc.feature_live_tracking.data.repository.TrackingRepository
import javax.inject.Inject


class TrackingRepositoryImpl @Inject constructor(
    val trackingApi: TrackingApi
) : TrackingRepository {
    override suspend fun getBusDetails(docId: String): ApiState<TrackingDetails> {
        try{

            val trackingDetails = trackingApi.getTrackingDetails(docId)
            val mappedTrackingDetails = trackingDetails.toDataClass<TrackingDetailsDto>().toDomain()

            return if(mappedTrackingDetails!=null) ApiState.Success(mappedTrackingDetails) else ApiState.Failure("Unknown Error")
        }catch (e: Exception){
            e.printStackTrace()
            return ApiState.Failure(e.stackTrace.toString())
        }
    }
}