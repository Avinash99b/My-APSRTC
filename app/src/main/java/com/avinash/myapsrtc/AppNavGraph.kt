package com.avinash.myapsrtc

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.avinash.myapsrtc.feature_live_tracking.navigation.liveTrackingNavGraph
import com.avinash.myapsrtc.feature_route_selection.navigation.BusRouteSelectionRoutes
import com.avinash.myapsrtc.feature_route_selection.navigation.routeSelectionGraph

@Composable
fun AppNavGraph(
    navController: NavHostController
){
    NavHost(navController,
        startDestination = BusRouteSelectionRoutes.SELECT_ROUTE){

        routeSelectionGraph(navController)

        liveTrackingNavGraph(navController)
    }
}