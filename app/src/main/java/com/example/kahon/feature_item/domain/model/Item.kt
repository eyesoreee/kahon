package com.example.kahon.feature_item.domain.model

data class Item(
    val id: Long,
    val name: String,
    val category: String,
    val imageUrl: String? = null
)