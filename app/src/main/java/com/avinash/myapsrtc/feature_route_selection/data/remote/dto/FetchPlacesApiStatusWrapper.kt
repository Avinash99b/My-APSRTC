package com.avinash.myapsrtc.feature_route_selection.data.remote.dto

data class FetchPlacesApiStatusWrapper<out T>(
    val status: String,
    val miniServicePlaces: T
)