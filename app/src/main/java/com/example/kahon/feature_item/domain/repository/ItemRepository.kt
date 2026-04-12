package com.example.kahon.feature_item.domain.repository

import com.example.kahon.feature_item.data.local.Item

interface ItemRepository {
    suspend fun getItems(storageId: Long): List<Item>
    suspend fun insertItem(item: Item)
    suspend fun deleteItem(id: Long)
    suspend fun updateItem(item: Item)
}