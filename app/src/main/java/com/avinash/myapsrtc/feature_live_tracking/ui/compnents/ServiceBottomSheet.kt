package com.avinash.myapsrtc.feature_live_tracking.ui.compnents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.ServiceDetails
import com.avinash.myapsrtc.core.domain.model.TrackingDetails

@Composable
fun ServiceBottomSheet(
    servicesState: ApiState<List<ServiceDetails>>,
    busDetails: Map<String, TrackingDetails>
) {
    Column(Modifier.fillMaxWidth()) {

        Text(
            text = "Live Services",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        when (servicesState) {
            is ApiState.Loading -> {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }

            is ApiState.Success -> {
                LazyColumn {
                    items(servicesState.data) { service ->
                        val tracking = busDetails[service.serviceDocId]
                        ServiceRow(service, tracking)
                    }
                }
            }

            else -> Unit
        }
    }
}