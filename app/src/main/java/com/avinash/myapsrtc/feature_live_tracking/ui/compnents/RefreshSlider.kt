package com.avinash.myapsrtc.feature_live_tracking.ui.compnents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun RefreshSlider(
    requestDelay: Int,
    onDelayChange: (Int) -> Unit
) {
    Surface(
        Modifier.padding(12.dp),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 4.dp
    ) {
        Column(Modifier.padding(12.dp)) {

            Text("Refresh Interval: ${requestDelay}s")

            Slider(
                value = requestDelay.toFloat(),
                onValueChange = {
                    onDelayChange(it.roundToInt())
                },
                valueRange = 5f..30f,
                steps = 4
            )
        }
    }
}
