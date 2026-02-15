package com.avinash.myapsrtc.feature_live_tracking.ui.compnents

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import com.avinash.myapsrtc.R
import com.avinash.myapsrtc.core.domain.model.TrackingDetails
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun LiveTrackingMap(
    busDetails: Map<String, TrackingDetails>,
    onBusSelected:(TrackingDetails?)-> Unit
) {

    val cameraState = rememberCameraPositionState()
    Log.d("LiveTrackingMap", "Rendering map")

    var alreadyZoomed by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(busDetails) {
        if (busDetails.isEmpty() || alreadyZoomed) return@LaunchedEffect
        val bounds = LatLngBounds.Builder().apply {
            busDetails.forEach {
                include(LatLng(it.value.latitude, it.value.longitude))
            }
        }.build()
        cameraState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 120), 1000)
        alreadyZoomed = true
    }
    val context = LocalContext.current
    GoogleMap(
        modifier = Modifier.fillMaxSize()
    ) {
        busDetails.forEach {
            key(it.value.vehicleNumber) {
                BusMarker(it.value,onBusSelected={
                    onBusSelected(it.value)
                },onBusDeselected = {
                    onBusSelected(null)
                })
            }
        }
    }
}


@Composable
fun BusMarker(
    trackingDetails: TrackingDetails,
    onBusSelected: ()-> Unit,
    onBusDeselected:()-> Unit
) {
    val context = LocalContext.current

    // ✅ Keep previous tracking details safely
    val prevTrackingDetails = remember {
        mutableStateOf<TrackingDetails?>(null)
    }

    // ✅ MarkerState must NOT be saveable or updated-state
    val markerState = remember {
        MarkerState(
            position = LatLng(
                trackingDetails.latitude,
                trackingDetails.longitude
            )
        )
    }

    LaunchedEffect(trackingDetails.refreshedAtMillis) {

        val prev = prevTrackingDetails.value

        // First update → snap marker
        if (prev == null) {
            markerState.position =
                LatLng(trackingDetails.latitude, trackingDetails.longitude)
            prevTrackingDetails.value = trackingDetails
            return@LaunchedEffect
        }

        // ---- Interpolation setup ----
        val prevLat = prev.latitude
        val prevLng = prev.longitude

        val newLat = trackingDetails.latitude
        val newLng = trackingDetails.longitude

        val latDiff = newLat - prevLat
        val lngDiff = newLng - prevLng

        //Rounding it to 1.5 scale for better animation
        val updateIntervalMs =
            (trackingDetails.refreshedAtMillis!! - prev.refreshedAtMillis!!)
                .coerceAtLeast(500L) * 1.5.toLong()

        val frameDelayMs = 16L // ~60 FPS
        val startTime = System.currentTimeMillis()

        // ---- Smooth interpolation loop ----
        while (true) {
            val elapsed = System.currentTimeMillis() - startTime

            val progress = (elapsed.toFloat() / updateIntervalMs)
                .coerceIn(0f, 1f)

            val interpolatedLat = prevLat + latDiff * progress
            val interpolatedLng = prevLng + lngDiff * progress

            markerState.position =
                LatLng(interpolatedLat, interpolatedLng)

            if (progress >= 1f) break

            delay(frameDelayMs)
        }

        // Save current as previous for next update
        prevTrackingDetails.value = trackingDetails
    }

    Marker(
        state = markerState,
        rotation = (trackingDetails.bearing?.toFloat() ?: 0f) + 90f,
        flat = true,
        icon = getBitmapDescriptor(
            R.drawable.bus_icon,
            context
        ),
        title = trackingDetails.vehicleNumber,
        snippet = "Last updated at: ${formatTime(trackingDetails.refreshedAtMillis!!)}",
        onClick = {
            onBusSelected()
            false
        },
        onInfoWindowClose = {
            onBusDeselected()
        }
    )
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
