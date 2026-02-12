package com.avinash.myapsrtc.feature_route_selection.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.avinash.myapsrtc.core.domain.model.ApiState
import com.avinash.myapsrtc.core.domain.model.Place
import com.avinash.myapsrtc.feature_route_selection.domain.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RouteSelectionViewModel @Inject constructor(
    val placesRepository: PlacesRepository
) : ViewModel(){

    private val _placesApiState = MutableStateFlow<ApiState<List<Place>>>(ApiState.Pending)
    val placesApiState = _placesApiState.asStateFlow()


    fun initialize(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _placesApiState.value = ApiState.Loading

                val placesResponse = placesRepository.getAllPlaces()

                _placesApiState.value = placesResponse

                if(placesResponse is ApiState.Success){
                    _allPlaces.value = placesResponse.data
                }
            }
        }
    }

    private val _allPlaces = mutableStateOf<List<Place>>(emptyList())
    val allPlaces: List<Place> get() = _allPlaces.value

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredPlaces = _searchQuery
        .debounce(300)
        .mapLatest { query ->
            if (query.isBlank()) allPlaces
            else {
                allPlaces.filter {
                    it.placeName.contains(query, true)
                }
            }
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )


    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

}