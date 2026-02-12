package com.avinash.myapsrtc.feature_live_tracking.data.remote.dto

data class GetServicesForRouteDto(
    val destinationLinkId: Int,
    val destinationPlaceId: Int,
    val sourceLinkId: Int,
    val sourcePlaceId: Int,
    val apiVersion: Int=1,
)