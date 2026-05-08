package com.example.kahon.feature_item.domain.repository

import com.example.kahon.feature_item.data.local.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getItems(storageId: Long): Flow<List<Item>>
    fun getAllCategories(): Flow<List<String>>
    suspend fun insertItem(item: Item)
    suspend fun deleteItem(id: Long)
    suspend fun updateItem(item: Item)
    suspend fun updateCategoryName(oldCategory: String, newCategory: String)
    suspend fun clearCategory(category: String)
}