package com.example.kahon.feature_room.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class RoomWithCount(
    val id: Long,
    val name: String,
    val color: Long,
    val icon: String,
    val storageCount: Int
)
