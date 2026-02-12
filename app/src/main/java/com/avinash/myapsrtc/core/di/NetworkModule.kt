package com.avinash.myapsrtc.core.di

import com.avinash.myapsrtc.core.domain.model.FirebaseRetrofit
import com.avinash.myapsrtc.core.network.RetrofitProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit = RetrofitProvider.create()

    @Provides
    fun provideFirebaseRetrofit(): FirebaseRetrofit = RetrofitProvider.createFirebase()
}