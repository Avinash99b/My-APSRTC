package com.avinash.myapsrtc.feature_live_tracking.ui

import android.util.Log
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.avinash.myapsrtc.core.domain.model.TrackingDetails
import com.avinash.myapsrtc.feature_live_tracking.viewmodel.LiveTrackingHomeViewModel
import com.avinash.myapsrtc.feature_live_tracking.ui.compnents.LiveTrackingMap
import com.avinash.myapsrtc.feature_live_tracking.ui.compnents.RefreshSlider
import com.avinash.myapsrtc.feature_live_tracking.ui.compnents.ServiceBottomSheet
import com.avinash.myapsrtc.feature_live_tracking.ui.compnents.TopStatusBar

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
    val filteredBusDetails = remember {
        mutableStateMapOf<String, TrackingDetails>()
    }

    LaunchedEffect(busDetails) {
        filteredBusDetails.clear()

        busDetails.forEach { (key, value) ->
            if (vm.isBusStale(value)) return@forEach

            filteredBusDetails[key] = value
        }
    }

    Log.d("LiveTrackingHome", "LiveTrackingHome: Rerendering")
    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 140.dp,
        sheetContent = {
            ServiceBottomSheet(
                servicesState = servicesState,
                busDetails = filteredBusDetails
            )
        }
    ) { padding ->

        Box(Modifier.fillMaxSize().padding(padding)) {

            LiveTrackingMap(filteredBusDetails)

            Column {
                TopStatusBar(
                    servicesState = servicesState,
                    busDetails = filteredBusDetails,
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

