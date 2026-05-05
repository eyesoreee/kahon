package com.example.kahon.feature_item.domain.repository

import com.example.kahon.feature_item.data.local.Item

interface ItemRepository {
    suspend fun getItems(storageId: Long): List<Item>
    suspend fun getAllCategories(): List<String>
    suspend fun insertItem(item: Item)
    suspend fun deleteItem(id: Long)
    suspend fun updateItem(item: Item)
    suspend fun updateCategoryName(oldCategory: String, newCategory: String)
    suspend fun clearCategory(category: String)
}