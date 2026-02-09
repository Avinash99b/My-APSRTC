package com.avinash.myapsrtc.core.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {
    fun create(): Retrofit=Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}