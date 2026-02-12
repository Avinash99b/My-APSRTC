package com.avinash.myapsrtc.feature_live_tracking.data.remote.dto

data class ApiStatusWrapper<out T>(
    val status: String,
    val data: T
)