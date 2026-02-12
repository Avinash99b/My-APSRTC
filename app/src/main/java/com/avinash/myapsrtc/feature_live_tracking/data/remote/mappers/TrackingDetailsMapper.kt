package com.avinash.myapsrtc.feature_live_tracking.data.remote.mappers

import com.avinash.myapsrtc.core.domain.model.TrackingDetails
import com.avinash.myapsrtc.feature_live_tracking.data.remote.dto.TrackingDetailsDto

fun TrackingDetailsDto.toDomain(): TrackingDetails? {

    val lat = latitude ?: return null
    val lng = longitude ?: return null
    val serviceId = serviceId ?: return null
    val docId = serviceDocId ?: return null
    val oprs = oprsNo ?: return null
    val trip = tripNumber?.toIntOrNull() ?: return null
    val depotId = depotId ?: return null
    val depotName = depotName ?: return null
    val vehicle = vehicleNumber ?: return null
    val bus = busNumber ?: return null
    val device = deviceSerialNumber ?: return null
    val locationTime = locationTime?.toLongOrNull() ?: return null
    val serviceType = serviceType ?: return null
    val journeyDate = journeyDate ?: return null

    return TrackingDetails(
        serviceId = serviceId,
        serviceDocId = docId,
        oprsNo = oprs,
        tripNumber = trip,

        serviceType = serviceType,
        depotId = depotId,
        depotName = depotName,

        vehicleNumber = vehicle,
        busNumber = bus,
        deviceSerialNumber = device,

        driverId = driverId ?: "UNKNOWN",
        driverName = driverName,
        loginName = loginName,

        latitude = lat,
        longitude = lng,
        altitude = locationAltitude,
        bearing = locationBearing,
        speedKmph = speed?.toDoubleOrNull(),

        isOnline = isOnline == "1",
        code = code,

        locationTimeMillis = locationTime,
        refreshedAtMillis = refreshedAt,
        ttlMillis = ttl,
        expireAtIso = expireAt,

        journeyDate = journeyDate
    )
}
