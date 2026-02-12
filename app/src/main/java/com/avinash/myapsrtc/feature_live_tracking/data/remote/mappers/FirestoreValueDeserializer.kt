package com.avinash.myapsrtc.feature_live_tracking.data.remote.mappers

import com.avinash.myapsrtc.feature_live_tracking.data.remote.dto.FirestoreDocument
import com.avinash.myapsrtc.feature_live_tracking.data.remote.dto.FirestoreValue
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

class FirestoreValueDeserializer : JsonDeserializer<FirestoreValue> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): FirestoreValue {

        val obj = json.asJsonObject

        return when {
            obj.has("stringValue") ->
                FirestoreValue.StringValue(obj["stringValue"].asString)

            obj.has("integerValue") ->
                FirestoreValue.IntegerValue(obj["integerValue"].asLong)

            obj.has("doubleValue") ->
                FirestoreValue.DoubleValue(obj["doubleValue"].asDouble)

            obj.has("booleanValue") ->
                FirestoreValue.BooleanValue(obj["booleanValue"].asBoolean)

            obj.has("timestampValue") ->
                FirestoreValue.TimestampValue(obj["timestampValue"].asString)

            obj.has("nullValue") ->
                FirestoreValue.NullValue(null)

            else -> error("Unsupported Firestore value: $obj")
        }
    }
}

inline fun <reified T : Any> FirestoreDocument.toDataClass(): T {
    val kClass = T::class
    val ctor = kClass.primaryConstructor
        ?: error("Class ${kClass.simpleName} must have a primary constructor")

    val args = ctor.parameters.associateWith { param ->
        val fsValue = fields[param.name]
        fsValue?.toKotlinValue(param.type)
    }

    return ctor.callBy(args)
}
fun FirestoreValue.toKotlinValue(type: KType): Any? {
    return when (this) {
        is FirestoreValue.StringValue -> value
        is FirestoreValue.IntegerValue -> when (type.classifier) {
            Int::class -> value.toInt()
            Long::class -> value
            else -> value
        }
        is FirestoreValue.DoubleValue -> value
        is FirestoreValue.BooleanValue -> value
        is FirestoreValue.TimestampValue -> value
        is FirestoreValue.NullValue -> null
    }
}
