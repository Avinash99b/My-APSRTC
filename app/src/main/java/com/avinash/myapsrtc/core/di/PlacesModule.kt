package com.avinash.myapsrtc.core.di

import com.avinash.myapsrtc.core.domain.repository.CacheRepository
import com.avinash.myapsrtc.core.network.RetrofitProvider
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
class PlacesModule {
    @Provides
    fun providePlacesApi(retrofit: Retrofit): PlacesApi = retrofit.create(PlacesApi::class.java)

    @Provides
    fun providePlacesRepository(placesApi: PlacesApi,cacheRepository: CacheRepository): PlacesRepository = PlacesRepositoryImpl(placesApi,cacheRepository)
}