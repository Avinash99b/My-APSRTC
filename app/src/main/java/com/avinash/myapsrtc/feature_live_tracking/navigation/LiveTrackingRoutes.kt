package com.avinash.myapsrtc.feature_live_tracking.navigation

import com.avinash.myapsrtc.core.domain.model.Place

object LiveTrackingRoutes {
    val LIVE_TRACKING_HOME="LIVE_TRACKING_HOME/{boarding_place_id}/{destination_place_id}"
    fun liveTrackingHome(place1: Place,place2: Place): String= "LIVE_TRACKING_HOME/${place1.placeId}/${place2.placeId}"
}