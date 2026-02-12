package com.avinash.myapsrtc.feature_route_selection.data.remote

import com.avinash.myapsrtc.feature_route_selection.data.remote.dto.ApiStatusWrapper
import com.avinash.myapsrtc.feature_route_selection.data.remote.dto.FetchPlacesApiStatusWrapper
import com.avinash.myapsrtc.feature_route_selection.data.remote.dto.PlaceDto
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PlacesApi {
    @POST("mappedplaces/fetchAllUniquePlaces")
    suspend fun fetchAllUniquePlaces(@Body empty: JsonObject= JsonObject()): FetchPlacesApiStatusWrapper<List<PlaceDto>>
}