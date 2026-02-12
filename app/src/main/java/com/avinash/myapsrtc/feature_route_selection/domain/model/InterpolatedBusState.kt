package com.avinash.myapsrtc.feature_route_selection.domain.model

import com.avinash.myapsrtc.core.domain.model.TrackingDetails
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.MarkerState

data class InterpolatedBusState(
    val serviceDocId: String,
    val markerState: MarkerState,
    val bearing: Float,
    val title: String,
    val snippet: String,
    val tracking: TrackingDetails
)
