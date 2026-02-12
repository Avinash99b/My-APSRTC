package com.avinash.myapsrtc.feature_live_tracking.data.repository

import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.TrackingDetails

interface TrackingRepository {
    suspend fun getBusDetails(docId: String): ApiState<TrackingDetails>
}