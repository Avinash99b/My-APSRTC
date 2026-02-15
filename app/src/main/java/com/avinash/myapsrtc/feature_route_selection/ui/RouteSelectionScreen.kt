package com.avinash.myapsrtc.feature_route_selection.ui

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.Place
import com.avinash.myapsrtc.feature_route_selection.viewmodel.RouteSelectionViewModel

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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        when (val state = placesState) {
            ApiState.Loading,
            ApiState.Pending -> {
                CircularProgressIndicator(color = Color(0xFF00C853))
            }

            is ApiState.Failure -> {
                Text(
                    text = "Failed to load places. Please check your connection.",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
            }

            is ApiState.Success<List<Place>> -> {
                RouteSelectionComponent(
                    viewModel = routeSelectionViewModel,
                    onRouteConfirmed = onRouteSelected
                )
            }
        }
    }
}

@Composable
fun RouteSelectionComponent(
    viewModel: RouteSelectionViewModel,
    onRouteConfirmed: (Place, Place) -> Unit
) {
    val unfilteredPlaces = viewModel.allPlaces
    val filteredPlaces by viewModel.filteredPlaces.collectAsState()
    
    var origin by remember { mutableStateOf<Place?>(null) }
    var destination by remember { mutableStateOf<Place?>(null) }

    LaunchedEffect(unfilteredPlaces) {
        if (unfilteredPlaces.isEmpty()) return@LaunchedEffect
        if (origin == null) origin = unfilteredPlaces.find { it.placeId == "4851" }
        if (destination == null) destination = unfilteredPlaces.find { it.placeId == "14701" }
    }

    var showOriginPicker by remember { mutableStateOf(false) }
    var showDestinationPicker by remember { mutableStateOf(false) }

    RouteSelectionContent(
        origin = origin,
        destination = destination,
        onOriginClick = { showOriginPicker = true },
        onDestinationClick = { showDestinationPicker = true },
        onSwapClick = {
            val temp = origin
            origin = destination
            destination = temp
        },
        onConfirmClick = {
            if (origin != null && destination != null) {
                onRouteConfirmed(origin!!, destination!!)
            }
        }
    )

    if (showOriginPicker) {
        PlacePickerBottomSheet(
            title = "Select Origin",
            viewModel = viewModel,
            places = filteredPlaces,
            onSelected = {
                origin = it
            },
            onDismiss = { showOriginPicker = false }
        )
    }

    if (showDestinationPicker) {
        PlacePickerBottomSheet(
            title = "Select Destination",
            viewModel = viewModel,
            places = filteredPlaces,
            onSelected = { destination = it },
            onDismiss = { showDestinationPicker = false }
        )
    }
}

@Composable
fun RouteSelectionContent(
    origin: Place?,
    destination: Place?,
    onOriginClick: () -> Unit,
    onDestinationClick: () -> Unit,
    onSwapClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E1E1E), Color(0xFF121212))
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Plan Your Route",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PlaceSelectorField(
                    label = "From",
                    selected = origin,
                    icon = Icons.Default.RadioButtonChecked,
                    iconColor = Color(0xFF00C853),
                    onClick = onOriginClick
                )

                PlaceSelectorField(
                    label = "To",
                    selected = destination,
                    icon = Icons.Default.LocationOn,
                    iconColor = Color.Red,
                    onClick = onDestinationClick
                )
            }

            // Swap Button
            IconButton(
                onClick = onSwapClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp, top = 40.dp) // Adjusted to be between the two fields
                    .size(44.dp)
                    .background(Color(0xFF2C2C2C), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap Locations",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        ConfirmButton(
            enabled = origin != null && destination != null,
            onClick = onConfirmClick
        )
    }
}

@Composable
fun PlaceSelectorField(
    label: String,
    selected: Place?,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF262626),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = selected?.placeName ?: "Select $label",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selected == null) Color.Gray else Color.White,
                    fontWeight = if (selected == null) FontWeight.Normal else FontWeight.SemiBold
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
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF00C853),
            contentColor = Color.Black,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = Color.Gray
        )
    ) {
        Text(
            text = "Confirm Route",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacePickerBottomSheet(
    title: String,
    viewModel: RouteSelectionViewModel,
    places: List<Place>,
    onSelected: (Place) -> Unit,
    onDismiss: () -> Unit
) {
    val query by viewModel.searchQuery.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF121212),
        contentColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onSearchQueryChange,
                placeholder = { Text("Search city, district, or pincode") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00C853),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFF00C853),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
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
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = place.placeName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = "${place.district} • ${place.pinCode}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

// Previews

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun RouteSelectionContentPreview() {
    val dummyPlace1 = Place("Guntur", "16.3", "1", "80.4", "Guntur", "522001", "4851", "Vijayawada", "AP")
    val dummyPlace2 = Place("Krishna", "16.5", "2", "80.6", "Vijayawada", "520001", "14701", "Guntur", "AP")

    RouteSelectionContent(
        origin = dummyPlace1,
        destination = dummyPlace2,
        onOriginClick = {},
        onDestinationClick = {},
        onSwapClick = {},
        onConfirmClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun PlaceRowPreview() {
    val dummyPlace = Place("District Name", "0.0", "1", "0.0", "Mandal Name", "500001", "123", "Example Place", "ST")
    PlaceRow(place = dummyPlace, onClick = {})
}
