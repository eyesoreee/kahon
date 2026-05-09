package com.example.kahon.feature_item.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kahon.feature_item.domain.model.SearchItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("""
        SELECT 
            item.id, 
            item.name, 
            item.category, 
            item.quantity, 
            item.imagePath, 
            storage.name AS storageName, 
            room.name AS roomName 
        FROM item 
        JOIN storage ON item.storageId = storage.id 
        JOIN room ON storage.roomId = room.id 
        WHERE item.name LIKE '%' || :query || '%'
    """)
    fun searchItems(query: String): Flow<List<SearchItem>>

    @Query("SELECT * FROM item WHERE storageId = :storageId")
    fun getItems(storageId: Long): Flow<List<Item>>

    @Query("SELECT DISTINCT category FROM item")
    fun getAllCategories(): Flow<List<String>>

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
    @Query("SELECT * FROM item")
    suspend fun getAllItemsRaw(): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<Item>)
}