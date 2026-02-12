package com.avinash.myapsrtc.feature_live_tracking.data.remote

import com.avinash.myapsrtc.feature_live_tracking.data.remote.dto.FirestoreDocument
import retrofit2.http.GET
import retrofit2.http.Path

interface TrackingApi {
    @GET("trackingDetailsV2/{serviceDocId}")
    suspend fun getTrackingDetails(@Path("serviceDocId") serviceDocId: String): FirestoreDocument
}