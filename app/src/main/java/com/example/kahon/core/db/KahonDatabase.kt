package com.example.kahon.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kahon.feature_location.data.local.Location
import com.example.kahon.feature_location.data.local.LocationDao

@Database(
    entities = [Location::class],
    version = 1
)
abstract class KahonDatabase : RoomDatabase() {
    abstract val locationDao: LocationDao
}
