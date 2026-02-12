package com.avinash.myapsrtc.feature_live_tracking.data.remote.dto

import com.google.gson.*
import kotlin.reflect.*

// Entire Firestore document
data class FirestoreDocument(
    val name: String,
    val fields: Map<String, FirestoreValue> = emptyMap()
)

// Firestore value union
sealed class FirestoreValue {
    data class StringValue(val value: String) : FirestoreValue()
    data class IntegerValue(val value: Long) : FirestoreValue()
    data class DoubleValue(val value: Double) : FirestoreValue()
    data class BooleanValue(val value: Boolean) : FirestoreValue()
    data class TimestampValue(val value: String) : FirestoreValue()
    data class NullValue(val value: Nothing?) : FirestoreValue()
}
