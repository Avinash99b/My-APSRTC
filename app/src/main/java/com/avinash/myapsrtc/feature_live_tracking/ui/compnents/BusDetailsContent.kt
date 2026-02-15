package com.avinash.myapsrtc.feature_live_tracking.ui.compnents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.avinash.myapsrtc.core.domain.model.TrackingDetails

@Composable
fun BusDetailsContent(
    details: TrackingDetails,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Bus ${details.busNumber}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        DetailItem("Service Type", details.serviceType)
        DetailItem("Depot", details.depotName)
        DetailItem("Driver", details.driverName ?: "Unknown")
        DetailItem("Speed", "${details.speedKmph ?: 0.0} km/h")
        DetailItem("Status", if (details.isOnline) "Online" else "Offline")
        DetailItem("Lat/Lng", "${details.latitude}, ${details.longitude}")

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = onBack) {
            Text("← Back to services")
        }
    }
}
@Composable
fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
@Composable
fun EmptyBusDetails() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Select a bus to view details")
    }
}
