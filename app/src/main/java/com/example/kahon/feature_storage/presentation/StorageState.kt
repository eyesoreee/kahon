package com.example.kahon.feature_storage.presentation

import com.example.kahon.feature_storage.data.local.Storage

data class StorageState(
    val items: List<Storage> = emptyList(),
    val searchQuery: String = "",
)