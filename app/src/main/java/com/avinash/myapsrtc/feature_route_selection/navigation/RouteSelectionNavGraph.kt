package com.avinash.myapsrtc.feature_route_selection.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable
import com.avinash.myapsrtc.feature_live_tracking.navigation.LiveTrackingRoutes
import com.avinash.myapsrtc.feature_route_selection.ui.RouteSelectionScreen

fun NavGraphBuilder.routeSelectionGraph(
    navController: NavController
){
    composable(BusRouteSelectionRoutes.SELECT_ROUTE){
        RouteSelectionScreen { place, place1 ->
            navController.navigate(LiveTrackingRoutes.liveTrackingHome(place,place1))
        }
    }
}