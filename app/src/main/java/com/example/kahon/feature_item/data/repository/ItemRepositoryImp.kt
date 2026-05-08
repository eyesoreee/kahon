package com.example.kahon.feature_item.data.repository

import com.example.kahon.feature_item.data.local.Item
import com.example.kahon.feature_item.data.local.ItemDao
import com.example.kahon.feature_item.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemRepositoryImp @Inject constructor(
    private val itemDao: ItemDao
) : ItemRepository {
    override fun getItems(storageId: Long): Flow<List<Item>> {
        return itemDao.getItems(storageId)
    }

    override fun getAllCategories(): Flow<List<String>> {
        return itemDao.getAllCategories()
    }

    override suspend fun insertItem(item: Item) {
        itemDao.insertItem(item)
    }

    override suspend fun deleteItem(id: Long) {
        itemDao.deleteItem(id)
    }

    override suspend fun updateItem(item: Item) {
        itemDao.updateItem(item)
    }

    override suspend fun updateCategoryName(oldCategory: String, newCategory: String) {
        itemDao.updateCategoryName(oldCategory, newCategory)
    }

    override suspend fun clearCategory(category: String) {
        itemDao.clearCategory(category)
    }
}