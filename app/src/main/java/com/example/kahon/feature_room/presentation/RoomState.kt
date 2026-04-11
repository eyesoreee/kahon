package com.example.kahon.feature_room.presentation

import com.example.kahon.feature_room.data.local.Room

data class RoomState(
    val rooms: List<Room> = emptyList(),
    val searchQuery: String = "",
    val isAddRoomDialogOpen: Boolean = false,
    val newRoomName: String = "",
    val isRoomOptionsDialogOpen: Boolean = false,
    val selectedRoomId: Long? = null,
    val isEditRoomDialogOpen: Boolean = false,
    val editRoomName: String = ""
)