package com.example.kahon.feature_room.domain.repository

import com.example.kahon.feature_room.data.local.Room
import com.example.kahon.feature_room.domain.model.RoomWithCount
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun getRooms(): Flow<List<RoomWithCount>>
    suspend fun insertRoom(room: Room)
    suspend fun deleteRoom(id: Long)
    suspend fun updateRoom(room: Room)
    suspend fun doesRoomExist(roomName: String): Boolean
}