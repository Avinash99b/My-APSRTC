package com.avinash.myapsrtc.feature_live_tracking.ui.compnents

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.ServiceDetails
import com.avinash.myapsrtc.core.domain.model.TrackingDetails
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServiceBottomSheet(
    servicesState: ApiState<List<ServiceDetails>>,
    busDetails: Map<String, TrackingDetails>,
    selectedBusDetails: TrackingDetails?,
    onBusSelected: (TrackingDetails) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0){
        busDetails.size
    }
    val scope = rememberCoroutineScope() // ✅ REQUIRED

    Column(
        modifier = Modifier.fillMaxWidth().animateContentSize()
    ) {

        // ---------------- HEADER ----------------
        Text(
            text = if (pagerState.currentPage == 0)
                "Live Services"
            else
                "Bus Details",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        // ---------------- PAGER ----------------
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->

            when (page) {

                // -------- PAGE 0 : SERVICES LIST --------
                0 -> {
                    when (servicesState) {

                        is ApiState.Loading -> {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        is ApiState.Success -> {
                            LazyColumn {
                                items(
                                    items = servicesState.data,
                                    key = { it.serviceDocId } // ✅ stable key
                                ) { service ->

                                    val tracking =
                                        busDetails[service.serviceDocId]

                                    ServiceRow(
                                        service = service,
                                        tracking = tracking,
                                        onClick = {
                                            if (tracking != null) {
                                                // Update selected bus
                                                onBusSelected(tracking)

                                                // ✅ CORRECT: launch coroutine
                                                scope.launch {
                                                    pagerState.animateScrollToPage(1)
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        else -> Unit
                    }
                }

                // -------- PAGE 1 : BUS DETAILS --------
                1 -> {
                    if (selectedBusDetails == null) {
                        EmptyBusDetails()
                    } else {
                        BusDetailsContent(
                            details = selectedBusDetails,
                            onBack = {
                                // ✅ CORRECT: launch coroutine
                                scope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
