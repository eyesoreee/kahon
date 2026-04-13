package com.example.kahon.feature_item.presentation

data class ItemState(
    // TODO: Add your screen-specific data here
    val isLoading: Boolean = false,
    val items: List<Any> = emptyList(),
    val error: String? = null,
)