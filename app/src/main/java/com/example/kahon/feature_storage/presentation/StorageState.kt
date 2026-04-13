package com.example.kahon.feature_storage.presentation

import com.example.kahon.feature_storage.domain.model.StorageWithCount

data class StorageState(
    val storages: List<StorageWithCount> = emptyList(),
    val searchQuery: String = ""
)