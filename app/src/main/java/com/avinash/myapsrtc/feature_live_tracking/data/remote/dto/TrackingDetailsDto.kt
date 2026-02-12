package com.avinash.myapsrtc.feature_live_tracking.data.remote.dto

/**
 * Represents Firestore document:
 * trackingDetailsV2/{serviceDocId}
 */
data class TrackingDetailsDto(

    // ---- Location ----
    val latitude: Double?,
    val longitude: Double?,
    val locationAltitude: Double?,
    val locationBearing: Double?,

    // speed is a STRING in Firestore (do NOT assume numeric)
    val speed: String?,

    // ---- Service & Trip ----
    val serviceId: String?,
    val serviceNumber: String?,
    val serviceDocId: String?,
    val serviceType: String?,
    val oprsNo: String?,
    val oprsServiceType: String?,
    val tripNumber: String?,
    val dutyId: String?,
    val dcp: String?,

    // ---- Depot ----
    val depotId: Long?,
    val depotName: String?,

    // ---- Driver ----
    val driverId: String?,
    val secondDriverId: String?,
    val driverName: String?,
    val loginName: String?,

    // ---- Vehicle ----
    val vehicleNumber: String?,
    val busNumber: String?,
    val deviceSerialNumber: String?,
    val androidId: String?,

    // ---- Status ----
    val isOnline: String?, // "0" / "1" (string!)
    val code: String?,

    // ---- Time ----
    val journeyDate: String?,
    val locationTime: String?,
    val refreshedAt: Long?,
    val ttl: Long?,
    val expireAt: String? // Firestore timestampValue (ISO-8601)

)
val TrackingDetailsDto.speedAsDouble: Double?
    get() = speed?.toDoubleOrNull()

val TrackingDetailsDto.isBusOnline: Boolean
    get() = isOnline == "1"

val TrackingDetailsDto.locationEpochMillis: Long?
    get() = locationTime?.toLongOrNull()
