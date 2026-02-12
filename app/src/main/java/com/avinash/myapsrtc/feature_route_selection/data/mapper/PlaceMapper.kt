package com.avinash.myapsrtc.feature_route_selection.data.mapper

import com.avinash.myapsrtc.core.domain.model.Place
import com.avinash.myapsrtc.feature_route_selection.data.remote.dto.PlaceDto

fun Place.toDto()= PlaceDto(
    district = district,
    latitude = latitude,
    linkPlaceId = linkPlaceId,
    longitude = longitude,
    mandalName = mandalName,
    pinCode = pinCode,
    placeId = placeId,
    placeName = placeName,
    stateCode = stateCode
)

fun PlaceDto.toDomain()= Place(
    district = district?:"N/A",
    latitude = latitude?:"",
    linkPlaceId = linkPlaceId,
    longitude = longitude?:"",
    mandalName = mandalName?:"N/A",
    pinCode = pinCode?:"N/A",
    placeId = placeId,
    placeName = placeName,
    stateCode = stateCode
)