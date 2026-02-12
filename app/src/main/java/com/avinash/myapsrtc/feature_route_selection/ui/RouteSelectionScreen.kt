package com.avinash.myapsrtc.feature_route_selection.ui

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asLiveData
import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.Place
import com.avinash.myapsrtc.feature_route_selection.viewmodel.RouteSelectionViewModel
import javax.inject.Inject

@Composable
fun RouteSelectionScreen(
    routeSelectionViewModel: RouteSelectionViewModel = hiltViewModel(),
    onRouteSelected: (Place, Place) -> Unit
) {
    val placesState by routeSelectionViewModel.placesApiState.collectAsState()

    LaunchedEffect(Unit) {
        routeSelectionViewModel.initialize()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = placesState) {
            ApiState.Loading,
            ApiState.Pending -> {
                Text(
                    text = "Loading places...",
                    fontWeight = FontWeight.Medium
                )
            }

            is ApiState.Failure -> {
                Text(
                    text = "Failed to load places",
                    color = Color.Red
                )
            }

            is ApiState.Success<List<Place>> -> {
                RouteSelectionComponent(
                    routeSelectionViewModel,
                    onRouteConfirmed = { from, to ->
                        onRouteSelected(from, to)
                    }
                )
            }
        }
    }
}

@Composable
fun ConfirmButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = if (enabled) Color(0xFF00C853) else Color.DarkGray,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Confirm Route",
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacePickerBottomSheet(
    title: String,
    viewModel: RouteSelectionViewModel,
    onSelected: (Place) -> Unit,
    onDismiss: () -> Unit,
    places: List<Place>
) {
    val query by viewModel.searchQuery.collectAsState()

    LaunchedEffect(places) {
        Log.d("Places", "Places Size:- " + places.size)
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column {

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onSearchQueryChange,
                placeholder = { Text("Type place name / district / pincode") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            LazyColumn {
                items(
                    items = places,
                    key = { it.placeId }
                ) { place ->
                    PlaceRow(place) {
                        onSelected(place)
                        viewModel.onSearchQueryChange("")
                        onDismiss()
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceRow(
    place: Place,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(place.placeName, fontWeight = FontWeight.SemiBold)
        Text(
            "${place.district} • ${place.pinCode}",
            color = Color.Gray,
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        )
    }
}

@Composable
fun PlaceSelectorField(
    label: String,
    selected: Place?,
    onClick: () -> Unit
) {
    Column {
        Text(label, color = Color.Gray)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF1C1C1C), RoundedCornerShape(14.dp))
                .clickable { onClick() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = selected?.placeName ?: "Search $label",
                color = if (selected == null) Color.Gray else Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RouteSelectionComponent(
    viewModel: RouteSelectionViewModel,
    onRouteConfirmed: (Place, Place) -> Unit
) {
    val unfilteredPlaces = viewModel.allPlaces
    val places by viewModel.filteredPlaces.collectAsState()
    var origin by remember { mutableStateOf<Place?>(null) }
    var destination by remember { mutableStateOf<Place?>(null) }

    LaunchedEffect(Unit) {
        if(unfilteredPlaces.isEmpty())return@LaunchedEffect
        val defaultOrigin = unfilteredPlaces.find { it.placeId == "4851" }
        val defaultDestination = unfilteredPlaces.find{it.placeId == "14701"}

        if(defaultDestination==null||defaultOrigin==null)return@LaunchedEffect
        origin=defaultOrigin
        destination=defaultDestination
    }

    var showOriginPicker by remember { mutableStateOf(false) }
    var showDestinationPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.65f)
            .background(Color(0xFF111111), RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(
            "Plan Your Route",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        PlaceSelectorField("Origin", origin) {
            showOriginPicker = true
        }

        PlaceSelectorField("Destination", destination) {
            showDestinationPicker = true
        }

        Spacer(modifier = Modifier.weight(1f))

        ConfirmButton(
            enabled = origin != null && destination != null,
            onClick = { onRouteConfirmed(origin!!, destination!!) }
        )
    }

    if (showOriginPicker) {
        PlacePickerBottomSheet(
            title = "Select Origin",
            viewModel = viewModel,
            onSelected = {
                origin = it
                destination = null
            },
            onDismiss = { showOriginPicker = false },
            places = places
        )
    }

    if (showDestinationPicker) {
        PlacePickerBottomSheet(
            title = "Select Destination",
            viewModel = viewModel,
            onSelected = { destination = it },
            onDismiss = { showDestinationPicker = false },
            places = places
        )
    }
}
