package com.avinash.myapsrtc.feature_live_tracking.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.avinash.myapsrtc.feature_live_tracking.ui.LiveTrackingHome

fun NavGraphBuilder.liveTrackingNavGraph(
    navController: NavController
) {
    composable(
        LiveTrackingRoutes.LIVE_TRACKING_HOME,
        arguments = listOf(
            navArgument("boarding_place_id") { type = NavType.StringType },
            navArgument("destination_place_id") {
                type =
                    NavType.StringType
            })
    ) { backstackEntry ->
        val fromId = backstackEntry.arguments?.getString("boarding_place_id")
        val toId = backstackEntry.arguments?.getString("destination_place_id")
        LiveTrackingHome(
            fromId ?: "",
            toId ?: ""
        )
    }
}