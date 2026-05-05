package com.example.kahon.feature_item.presentation

import com.example.kahon.feature_item.data.local.Item

data class ItemState(
    val storageName: String = "",
    val roomName: String = "",
    val items: List<Item> = emptyList(),
    val categories: List<String> = listOf("Electronics", "Clothing", "Kitchen", "Tools", "Office"),
    val isAddingItem: Boolean = false,
    val editingItem: Item? = null
)