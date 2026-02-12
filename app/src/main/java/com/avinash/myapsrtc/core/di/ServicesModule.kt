package com.avinash.myapsrtc.core.di

import com.avinash.myapsrtc.core.domain.repository.CacheRepository
import com.avinash.myapsrtc.core.network.RetrofitProvider
import com.avinash.myapsrtc.feature_live_tracking.data.remote.ServicesApi
import com.avinash.myapsrtc.feature_live_tracking.data.repository.ServicesRepository
import com.avinash.myapsrtc.feature_live_tracking.domain.repository.ServicesRepositoryImpl
import com.avinash.myapsrtc.feature_route_selection.data.remote.PlacesApi
import com.avinash.myapsrtc.feature_route_selection.data.repository.PlacesRepositoryImpl
import com.avinash.myapsrtc.feature_route_selection.domain.repository.PlacesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {
    @Provides
    fun provideServicesApi(retrofit: Retrofit): ServicesApi = retrofit.create(ServicesApi::class.java)

    @Provides
    fun providePlacesRepository(servicesApi: ServicesApi,cacheRepository: CacheRepository): ServicesRepository =
        ServicesRepositoryImpl(servicesApi,cacheRepository)
}