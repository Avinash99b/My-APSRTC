package com.avinash.myapsrtc.core.domain.model

data class ServiceDetails(
    val dcpNo: String,
    val depotName: String,
    val destinationName: String,
    val endCityPlaceId: Int,
    val exactMatch: Boolean,
    val isCancel: Boolean,
    val isClose: Boolean,
    val isDirect: Boolean,
    val isOprsService: String,
    val journeyDate: String,
    val oprsNo: String,
    val serviceDocId: String,
    val serviceEndTime: String,
    val serviceId: Long,
    val serviceStartTime: String,
    val serviceType: String,
    val sourceName: String,
    val startCityPlaceId: Int,
    val tripNo: Int
)