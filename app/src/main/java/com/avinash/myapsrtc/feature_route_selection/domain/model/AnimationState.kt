package com.avinash.myapsrtc.feature_route_selection.domain.model

import com.google.android.gms.maps.model.LatLng

private data class AnimationState(
    val start: LatLng,
    val end: LatLng,
    val startTime: Long,
    val durationMs: Long,
    val startBearing: Float,
    val endBearing: Float
)

private val animations =
    mutableMapOf<String, AnimationState>()
