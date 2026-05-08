package com.example.kahon.feature_room.presentation

import androidx.compose.runtime.Immutable
import com.example.kahon.feature_room.domain.model.RoomWithCount

@Immutable
data class RoomState(
    val rooms: List<RoomWithCount> = emptyList(),
    val searchQuery: String = ""
)
