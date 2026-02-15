package com.avinash.myapsrtc.feature_live_tracking.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.Place
import com.avinash.myapsrtc.core.domain.model.ServiceDetails
import com.avinash.myapsrtc.core.domain.model.TrackingDetails
import com.avinash.myapsrtc.feature_live_tracking.data.repository.ServicesRepository
import com.avinash.myapsrtc.feature_live_tracking.data.repository.TrackingRepository
import com.avinash.myapsrtc.feature_route_selection.domain.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class LiveTrackingHomeViewModel @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val servicesRepository: ServicesRepository,
    private val trackingRepository: TrackingRepository
) : ViewModel() {

    /* ---------------- Route ---------------- */

    var route: Pair<Place, Place>? = null
        private set

    /* ---------------- Services ---------------- */

    private val _availableServices =
        MutableStateFlow<ApiState<List<ServiceDetails>>>(ApiState.Pending)
    val availableServices = _availableServices.asStateFlow()

    /* ---------------- Refresh Interval ---------------- */

    private val _requestDelay = MutableStateFlow(30) // seconds
    val requestDelay = _requestDelay.asStateFlow()

    fun updateRequestDelay(seconds: Int) {
        _requestDelay.value = seconds.coerceIn(5, 30)
    }

    /* ---------------- Tracking (FAST) ---------------- */

    private val _busDetails =
        MutableStateFlow<Map<String, TrackingDetails>>(mutableStateMapOf())
    val busDetails = _busDetails.asStateFlow()

    /* ---------------- Tracking (UI-SAFE) ---------------- */

    /**
     * 🔥 Throttled flow for Compose & Map
     * Prevents UI freezes when many buses update
     */
    val uiBusDetails: StateFlow<Map<String, TrackingDetails>> =
        busDetails
            .sample(500)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyMap()
            )

    /* ---------------- Jobs ---------------- */

    private var monitoringJob: Job? = null

    /* ---------------- Initialization ---------------- */

    fun initialize(startPlaceId: String, destinationPlaceId: String) {
        viewModelScope.launch {
            val placesState = placesRepository.getAllPlaces()
            if (placesState !is ApiState.Success) return@launch

            val start = placesState.data.first { it.placeId == startPlaceId }
            val dest = placesState.data.first { it.placeId == destinationPlaceId }

            route = start to dest

            _availableServices.value = ApiState.Loading
            _availableServices.value =
                servicesRepository.getServicesForRoute(start, dest)

            startMonitoring()
        }
    }

    /* ---------------- Monitoring ---------------- */

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startMonitoring() {
        monitoringJob?.cancel()

        //Set initial delay for 5 seconds for first 20 seconds to collect data for interpolations
        _requestDelay.value = 5
        viewModelScope.launch {
            delay(20_000)
            _requestDelay.value = 30
        }
        val servicesState = _availableServices.value
        if (servicesState !is ApiState.Success) return

        val services = servicesState.data

        monitoringJob = viewModelScope.launch {
            requestDelay
                .flatMapLatest { delaySeconds ->
                    tickerFlow(delaySeconds)
                }
                .collect {
                    fetchIndependently(services)
                }
        }
    }

    /**
     * 🚍 Each bus request runs independently
     * UI updates as soon as a single bus returns
     */
    private suspend fun fetchIndependently(
        services: List<ServiceDetails>
    ) = coroutineScope {
        services.forEach { service ->
            launch(Dispatchers.IO) {
                val result =
                    trackingRepository.getBusDetails(service.serviceDocId)

                if (result is ApiState.Success) {
                    _busDetails.update { old ->
                        old + (service.serviceDocId to result.data)
                    }
                }
            }
        }
    }

    /* ---------------- Utilities ---------------- */

    private fun tickerFlow(delaySeconds: Int) = flow {
        while (true) {
            emit(Unit)
            delay(delaySeconds * 1_000L)
        }
    }

    /* ---------------- Derived helpers (UI) ---------------- */

    /*
    Returns true if bus last refreshed time is greater than 5 mins
     */
    fun isBusStale(tracking: TrackingDetails): Boolean {
        val now = System.currentTimeMillis()
        return now - (tracking.refreshedAtMillis?:0) >= 5 * 60 * 1000
    }

    fun lastUpdatedSeconds(tracking: TrackingDetails): Long {
        return (System.currentTimeMillis() - tracking.locationTimeMillis) / 1000
    }

    override fun onCleared() {
        monitoringJob?.cancel()
        super.onCleared()
    }
}
