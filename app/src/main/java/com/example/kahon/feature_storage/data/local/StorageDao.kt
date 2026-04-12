package com.example.kahon.feature_storage.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kahon.feature_storage.domain.model.StorageWithCount

@Dao
interface StorageDao {
    @Query("""
        SELECT 
            storage.id AS id, 
            storage.name AS name, 
            COUNT(item.id) AS itemCount 
        FROM storage 
        LEFT JOIN item ON storage.id = item.storageId 
        WHERE storage.roomId = :roomId
        GROUP BY storage.id
    """)
    suspend fun getStoragesWithCount(roomId: Long): List<StorageWithCount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStorage(storage: Storage)

    @Query("DELETE FROM storage WHERE id = :id")
    suspend fun deleteStorage(id: Long)

    @Update
    suspend fun updateStorage(storage: Storage)
}
