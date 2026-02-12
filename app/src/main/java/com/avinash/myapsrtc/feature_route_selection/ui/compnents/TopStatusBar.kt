package com.avinash.myapsrtc.feature_route_selection.ui.compnents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.ServiceDetails
import com.avinash.myapsrtc.core.domain.model.TrackingDetails
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
@Composable
fun TopStatusBar(
    servicesState: ApiState<List<ServiceDetails>>,
    busDetails: Map<String, TrackingDetails>,
    requestDelay: Int
) {
    val total = (servicesState as? ApiState.Success)?.data?.size ?: 0
    val live = busDetails.size

    Surface(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (live > 0) "🟢 Live" else "🟡 Connecting",
                fontWeight = FontWeight.Bold
            )

            Text("Buses: $live / $total")

            Text("Refresh: ${requestDelay}s")
        }
    }
}
