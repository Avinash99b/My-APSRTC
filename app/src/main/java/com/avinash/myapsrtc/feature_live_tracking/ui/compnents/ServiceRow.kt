package com.avinash.myapsrtc.feature_live_tracking.ui.compnents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.avinash.myapsrtc.core.domain.model.ServiceDetails
import com.avinash.myapsrtc.core.domain.model.TrackingDetails
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ServiceRow(
    service: ServiceDetails,
    tracking: TrackingDetails?
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = service.serviceType,
                fontWeight = FontWeight.Bold
            )
            Text(" - ${tracking?.busNumber ?: service.serviceStartTime}")
        }

        Text(
            text = "${service.sourceName} → ${service.destinationName}",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(4.dp))

        if (tracking != null) {
            val secondsAgo =
                (System.currentTimeMillis() - tracking.locationTimeMillis) / 1000

            Text(
                text = "🟢 ${tracking.speedKmph ?: 0.0} km/h • ${secondsAgo}s ago",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            Text(
                text = "⏳ Waiting for location...",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
