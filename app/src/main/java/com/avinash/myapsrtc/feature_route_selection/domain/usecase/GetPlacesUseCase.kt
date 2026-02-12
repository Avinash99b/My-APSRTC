package com.avinash.myapsrtc.feature_route_selection.domain.usecase

import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.Place
import com.avinash.myapsrtc.core.domain.model.Waypoint
import com.avinash.myapsrtc.feature_route_selection.domain.repository.PlacesRepository
import javax.inject.Inject

class GetPlacesUseCase @Inject constructor(
    val repository: PlacesRepository
){
    suspend operator fun invoke(): ApiState<List<Place>> = repository.getAllPlaces()
}