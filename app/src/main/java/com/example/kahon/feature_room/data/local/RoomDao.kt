package com.example.kahon.feature_room.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kahon.feature_room.domain.model.RoomWithCount

@Dao
interface RoomDao {
    @Query(
        """
        SELECT 
            room.id AS id, 
            room.name AS name, 
            COUNT(storage.id) AS storageCount 
        FROM room 
        LEFT JOIN storage ON room.id = storage.roomId 
        GROUP BY room.id
    """
    )
    suspend fun getRoomsWithCount(): List<RoomWithCount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: Room)

    @Query("DELETE FROM room WHERE id = :id")
    suspend fun deleteRoom(id: Long)

    @androidx.room.Update
    suspend fun updateRoom(room: Room)

    @Query("SELECT EXISTS(SELECT 1 FROM room WHERE name = :roomName)")
    suspend fun doesRoomExist(roomName: String): Boolean
}