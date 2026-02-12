package com.avinash.myapsrtc.feature_route_selection.ui.compnents

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.avinash.myapsrtc.R
import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.ServiceDetails
import com.avinash.myapsrtc.core.domain.model.TrackingDetails
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.core.graphics.createBitmap
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun LiveTrackingMap(
    busDetails: Map<String, TrackingDetails>
) {
    val cameraState = rememberCameraPositionState()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraState
    ) {
        val now = System.currentTimeMillis()
        val maxAgeMillis = 10 * 60 * 1000 // 10 minutes

        busDetails.values
            .filter { tracking ->
                val refreshedAt = tracking.refreshedAtMillis
                refreshedAt != null && (now - refreshedAt) <= maxAgeMillis
            }
            .forEach { tracking ->
                key(tracking.serviceDocId) {
                    Marker(
                        state = MarkerState(
                            LatLng(tracking.latitude, tracking.longitude)
                        ),
                        rotation = (tracking.bearing?.toFloat() ?: 0f) + 90f,
                        flat = true,
                        icon = getBitmapDescriptor(
                            R.drawable.bus_icon,
                            LocalContext.current
                        ),
                        title = tracking.vehicleNumber,
                        snippet = "Last updated at: ${formatTime(tracking.refreshedAtMillis!!)}"
                    )
                }
            }

    }
}


fun formatTime(millis: Long): String {
    val formatter = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
    return formatter.format(Date(millis))
}

private fun getBitmapDescriptor(
    @DrawableRes id: Int,
    context: Context,
    heightDp: Float = 32f   // ideal for map markers
): BitmapDescriptor {

    val drawable = requireNotNull(
        ContextCompat.getDrawable(context, id)
    )

    val density = context.resources.displayMetrics.density

    // Intrinsic aspect ratio from vector
    val intrinsicWidth = drawable.intrinsicWidth
    val intrinsicHeight = drawable.intrinsicHeight
    val aspectRatio = intrinsicWidth.toFloat() / intrinsicHeight.toFloat()

    // Convert dp → px
    val heightPx = (heightDp * density).toInt()
    val widthPx = (heightPx * aspectRatio).toInt()

    drawable.setBounds(0, 0, widthPx, heightPx)

    val bitmap = createBitmap(widthPx, heightPx)

    val canvas = Canvas(bitmap)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
