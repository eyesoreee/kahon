package com.example.kahon.feature_room.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDao {
    @Query("SELECT * FROM room")
    suspend fun getRooms(): List<Room>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: Room)

    // WARNING: Update this method later
    // Ensure that it won't be deleted as long as there is `container` child
    // DELETE if there is 0 `container` dependent on this `room`
    @Query("DELETE FROM room WHERE id = :id")
    suspend fun deleteRoom(id: Long)

    @androidx.room.Update
    suspend fun updateRoom(room: Room)
}