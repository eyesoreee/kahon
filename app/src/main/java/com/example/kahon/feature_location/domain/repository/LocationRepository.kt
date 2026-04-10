package com.example.kahon.feature_location.domain.repository

import com.example.kahon.feature_location.data.local.Location

interface LocationRepository {
    suspend fun getLocations(): List<Location>
    suspend fun insertLocation(location: Location)
    suspend fun deleteLocation(id: Long)
}