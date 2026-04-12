package com.example.kahon.feature_storage.presentation

import com.example.kahon.feature_storage.domain.model.StorageWithCount

data class StorageState(
    val storages: List<StorageWithCount> = emptyList(),
    val searchQuery: String = "",
    val isAddStorageDialogOpen: Boolean = false,
    val newStorageName: String = "",
    val isStorageOptionsDialogOpen: Boolean = false,
    val selectedStorageId: Long? = null,
    val isEditStorageDialogOpen: Boolean = false,
    val editStorageName: String = ""
)