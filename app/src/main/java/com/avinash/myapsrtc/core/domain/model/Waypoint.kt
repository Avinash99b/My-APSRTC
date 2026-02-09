package com.avinash.myapsrtc.core.domain.model

data class Waypoint(
    val departureDay: String,
    val latitude: String,
    val longitude: String,
    val placeCode: String,
    val placeId: String,
    val placeName: String,
    val placeTime: String,
    val scheduleArrTime: String,
    val scheduleDepTime: String,
    val seqNo: Int,
    val stationDay: String,
    val stationId: String,
    val stationName: String,
    val wayPointName: String
)