package com.example.kahon.feature_storage.data.repository

import com.example.kahon.feature_storage.data.local.Storage
import com.example.kahon.feature_storage.data.local.StorageDao
import com.example.kahon.feature_storage.domain.model.StorageWithCount
import com.example.kahon.feature_storage.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StorageRepositoryImp @Inject constructor(
    private val storageDao: StorageDao
) : StorageRepository {
    override fun getStorages(roomId: Long): Flow<List<StorageWithCount>> {
        return storageDao.getStoragesWithCount(roomId)
    }

    override suspend fun insertStorage(storage: Storage) {
        storageDao.insertStorage(storage)
    }

    override suspend fun deleteStorage(id: Long) {
        storageDao.deleteStorage(id)
    }

    override suspend fun updateStorage(storage: Storage) {
        storageDao.updateStorage(storage)
    }

    override suspend fun doesStorageExist(roomName: String, storageName: String): Boolean {
        return storageDao.doesStorageExist(roomName, storageName)
    }

    override suspend fun getStorageId(roomName: String, storageName: String): Long? {
        return storageDao.getStorageId(roomName, storageName)
    }

    override suspend fun getAllStoragesRaw(): List<Storage> = storageDao.getAllStoragesRaw()
    override suspend fun insertStorages(storages: List<Storage>) = storageDao.insertStorages(storages)
}
