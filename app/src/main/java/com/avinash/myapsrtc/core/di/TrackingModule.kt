package com.avinash.myapsrtc.core.di

import com.avinash.myapsrtc.core.domain.model.FirebaseRetrofit
import com.avinash.myapsrtc.core.domain.repository.CacheRepository
import com.avinash.myapsrtc.feature_live_tracking.data.remote.TrackingApi
import com.avinash.myapsrtc.feature_live_tracking.data.repository.TrackingRepository
import com.avinash.myapsrtc.feature_live_tracking.domain.repository.ServicesRepositoryImpl
import com.avinash.myapsrtc.feature_live_tracking.domain.repository.TrackingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class TrackingModule {
    @Provides
    fun provideTrackingApi(retrofit: FirebaseRetrofit): TrackingApi = retrofit.retrofit.create(TrackingApi::class.java)

    @Provides
    fun provideTrackingRepository(trackingApi: TrackingApi): TrackingRepository =
        TrackingRepositoryImpl(trackingApi)
}