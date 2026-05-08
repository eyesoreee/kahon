package com.example.kahon.feature_storage.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class StorageWithCount(
    val id: Long,
    val name: String,
    val color: Long,
    val itemCount: Int
)
