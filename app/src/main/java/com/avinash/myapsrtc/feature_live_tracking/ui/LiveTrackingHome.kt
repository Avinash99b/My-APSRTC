package com.avinash.myapsrtc.feature_live_tracking.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.avinash.myapsrtc.feature_live_tracking.viewmodel.LiveTrackingHomeViewModel
import com.avinash.myapsrtc.feature_route_selection.ui.compnents.LiveTrackingMap
import com.avinash.myapsrtc.feature_route_selection.ui.compnents.RefreshSlider
import com.avinash.myapsrtc.feature_route_selection.ui.compnents.ServiceBottomSheet
import com.avinash.myapsrtc.feature_route_selection.ui.compnents.TopStatusBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTrackingHome(
    startPlaceId: String,
    destinationPlaceId: String
) {
    val vm: LiveTrackingHomeViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        vm.initialize(startPlaceId, destinationPlaceId)
    }

    val servicesState by vm.availableServices.collectAsState()
    val busDetails by vm.uiBusDetails.collectAsState()
    val requestDelay by vm.requestDelay.collectAsState()

    val sheetState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 140.dp,
        sheetContent = {
            ServiceBottomSheet(
                servicesState = servicesState,
                busDetails = busDetails
            )
        }
    ) { padding ->

        Box(Modifier.fillMaxSize().padding(padding)) {

            LiveTrackingMap(busDetails)

            Column {
                TopStatusBar(
                    servicesState = servicesState,
                    busDetails = busDetails,
                    requestDelay
                )
                RefreshSlider(
                    requestDelay = requestDelay,
                    onDelayChange = vm::updateRequestDelay
                )
            }
        }
    }
}

