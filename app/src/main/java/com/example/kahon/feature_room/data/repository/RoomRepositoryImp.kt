package com.example.kahon.feature_room.data.repository

import com.example.kahon.feature_room.data.local.Room
import com.example.kahon.feature_room.data.local.RoomDao
import com.example.kahon.feature_room.domain.repository.RoomRepository
import javax.inject.Inject

class RoomRepositoryImp @Inject constructor(
    private val roomDao: RoomDao
) : RoomRepository {
    override suspend fun getRooms() = roomDao.getRoomsWithCount()
    override suspend fun insertRoom(room: Room) = roomDao.insertRoom(room)
    override suspend fun deleteRoom(id: Long) = roomDao.deleteRoom(id)
    override suspend fun updateRoom(room: Room) = roomDao.updateRoom(room)
    override suspend fun doesRoomExist(roomName: String): Boolean = roomDao.doesRoomExist(roomName)
}