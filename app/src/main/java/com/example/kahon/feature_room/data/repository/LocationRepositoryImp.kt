package com.example.kahon.feature_room.data.repository

import com.example.kahon.feature_room.data.local.Room
import com.example.kahon.feature_room.data.local.RoomDao
import com.example.kahon.feature_room.domain.repository.RoomRepository
import javax.inject.Inject

class RoomRepositoryImp @Inject constructor(
    private val roomDao: RoomDao
) : RoomRepository {
    override suspend fun getRooms() = roomDao.getRooms()
    override suspend fun insertRoom(room: Room) = roomDao.insertRoom(room)
    override suspend fun deleteRoom(id: Long) = roomDao.deleteRoom(id)
}