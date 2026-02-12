package com.avinash.myapsrtc.core.domain.model

data class TrackingDetails(

    // ---- Identity ----
    val serviceId: String,
    val serviceDocId: String,
    val oprsNo: String,
    val tripNumber: Int,

    // ---- Service ----
    val serviceType: String,
    val depotId: Long,
    val depotName: String,

    // ---- Vehicle ----
    val vehicleNumber: String,
    val busNumber: String,
    val deviceSerialNumber: String,

    // ---- Driver ----
    val driverId: String,
    val driverName: String?,
    val loginName: String?,

    // ---- Location ----
    val latitude: Double,
    val longitude: Double,
    val altitude: Double?,
    val bearing: Double?,
    val speedKmph: Double?,

    // ---- Status ----
    val isOnline: Boolean,
    val code: String?,

    // ---- Time ----
    val locationTimeMillis: Long,
    val refreshedAtMillis: Long?,
    val ttlMillis: Long?,
    val expireAtIso: String?,

    // ---- Meta ----
    val journeyDate: String
)
