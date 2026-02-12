package com.avinash.myapsrtc.feature_route_selection.data.remote.dto

data class PlaceDto(
    val district: String?,
    val latitude: String?,
    val linkPlaceId: String,
    val longitude: String?,
    val mandalName: String?,
    val pinCode: String?,
    val placeId: String,
    val placeName: String,
    val stateCode: String
)