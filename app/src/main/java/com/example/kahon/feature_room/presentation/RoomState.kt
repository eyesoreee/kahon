package com.example.kahon.feature_room.presentation

import com.example.kahon.feature_room.domain.model.RoomWithCount

data class RoomState(
    val rooms: List<RoomWithCount> = emptyList(),
    val searchQuery: String = ""
)