package com.example.kahon.feature_room.domain.repository

import com.example.kahon.feature_room.data.local.Room

interface RoomRepository {
    suspend fun getRooms(): List<Room>
    suspend fun insertRoom(room: Room)
    suspend fun deleteRoom(id: Long)
    suspend fun updateRoom(room: Room)
}