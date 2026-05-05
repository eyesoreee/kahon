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

    @Query("SELECT DISTINCT category FROM item")
    suspend fun getAllCategories(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Query("DELETE FROM item WHERE id = :id")
    suspend fun deleteItem(id: Long)

    @Update
    suspend fun updateItem(item: Item)

    @Query("UPDATE item SET category = :newCategory WHERE category = :oldCategory")
    suspend fun updateCategoryName(oldCategory: String, newCategory: String)

    @Query("UPDATE item SET category = 'Uncategorized' WHERE category = :category")
    suspend fun clearCategory(category: String)
}