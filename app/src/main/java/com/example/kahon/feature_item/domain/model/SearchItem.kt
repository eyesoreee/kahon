package com.example.kahon.feature_item.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class SearchItem(
    val id: Long,
    val name: String,
    val category: String,
    val quantity: Int,
    val imagePath: String?,
    val storageName: String,
    val roomName: String
)
