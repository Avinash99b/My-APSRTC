package com.avinash.myapsrtc.core.network

import com.avinash.myapsrtc.core.domain.model.FirebaseRetrofit
import com.avinash.myapsrtc.feature_live_tracking.data.remote.dto.FirestoreValue
import com.avinash.myapsrtc.feature_live_tracking.data.remote.mappers.FirestoreValueDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.NONE) })
        .build()
    fun create(): Retrofit=Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl("https://utsappapicached01.apsrtconline.in/uts-vts-api/")
        .build()


    val gson: Gson = GsonBuilder().apply {
        registerTypeAdapter(FirestoreValue::class.java, FirestoreValueDeserializer())
    }.create()

    fun createFirebase(): FirebaseRetrofit = FirebaseRetrofit(
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://firestore.googleapis.com/v1/projects/apsrtc-uts-prod/databases/(default)/documents/")
            .build()
    )
}