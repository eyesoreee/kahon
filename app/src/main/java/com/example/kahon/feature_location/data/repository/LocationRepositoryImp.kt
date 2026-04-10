package com.example.kahon.feature_location.data.repository

import com.example.kahon.feature_location.data.local.Location
import com.example.kahon.feature_location.data.local.LocationDao
import com.example.kahon.feature_location.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImp @Inject constructor(
    private val locationDao: LocationDao
): LocationRepository {
    override suspend fun getLocations() = locationDao.getLocations()
    override suspend fun insertLocation(location: Location) = locationDao.insertLocation(location)
    override suspend fun deleteLocation(id: Long) = locationDao.deleteLocation(id)
}