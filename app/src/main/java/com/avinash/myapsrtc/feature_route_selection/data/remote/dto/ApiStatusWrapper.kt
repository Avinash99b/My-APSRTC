package com.avinash.myapsrtc.feature_route_selection.data.remote.dto

data class ApiStatusWrapper<out T>(
    val status: String,
    val data: T
)