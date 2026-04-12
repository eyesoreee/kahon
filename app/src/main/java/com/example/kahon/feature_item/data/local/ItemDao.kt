package com.example.kahon.feature_item.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDao {
    @Query("SELECT * FROM item WHERE storageId = :storageId")
    suspend fun getItems(storageId: Long): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Query("DELETE FROM item WHERE id = :id")
    suspend fun deleteItem(id: Long)

    @Update
    suspend fun updateItem(item: Item)
}