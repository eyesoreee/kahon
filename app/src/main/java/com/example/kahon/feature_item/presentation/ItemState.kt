package com.example.kahon.feature_item.presentation

import com.example.kahon.feature_item.domain.model.Item

data class ItemState(
    val containerName: String = "",
    val roomName: String = "",
    val items: List<Item> = emptyList(),
)