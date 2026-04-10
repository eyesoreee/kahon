package com.example.kahon.core.di

import com.example.kahon.feature_location.data.local.LocationDao
import com.example.kahon.feature_location.data.repository.LocationRepositoryImp
import com.example.kahon.feature_location.domain.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideLocationRepository(locationDao: LocationDao): LocationRepository {
        return LocationRepositoryImp(locationDao)
    }
}