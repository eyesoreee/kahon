package com.example.kahon.feature_storage.presentation

import androidx.compose.runtime.Immutable
import com.example.kahon.feature_storage.domain.model.StorageWithCount

@Immutable
data class StorageState(
    val storages: List<StorageWithCount> = emptyList(),
    val searchQuery: String = ""
)