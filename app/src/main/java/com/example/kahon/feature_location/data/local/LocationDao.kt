package com.example.kahon.feature_location.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    suspend fun getLocations(): List<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location)

    // WARNING: Update this method later
    // Ensure that it won't be deleted as long as there is `container` child
    // DELETE if there is 0 `container` dependent on this `location`
    @Query("DELETE FROM locations WHERE id = :id")
    suspend fun deleteLocation(id: Long)
}