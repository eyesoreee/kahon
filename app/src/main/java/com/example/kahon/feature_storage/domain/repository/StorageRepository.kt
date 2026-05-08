package com.example.kahon.feature_storage.domain.repository

import com.example.kahon.feature_storage.data.local.Storage
import com.example.kahon.feature_storage.domain.model.StorageWithCount
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    fun getStorages(roomId: Long): Flow<List<StorageWithCount>>
    suspend fun insertStorage(storage: Storage)
    suspend fun deleteStorage(id: Long)
    suspend fun updateStorage(storage: Storage)
    suspend fun doesStorageExist(roomName: String, storageName: String): Boolean
    suspend fun getStorageId(roomName: String, storageName: String): Long?
}
