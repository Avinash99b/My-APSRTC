package com.avinash.myapsrtc.feature_route_selection.domain.repository

import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.Place
import com.avinash.myapsrtc.core.domain.model.Waypoint

interface PlacesRepository {
    suspend fun getAllPlaces(): ApiState<List<Place>>
}