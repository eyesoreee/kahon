package com.example.kahon.feature_room.presentation

sealed interface RoomAction {
    data class OnConfirmAddRoom(val name: String, val color: Long, val icon: String) : RoomAction
    data class OnDeleteRoom(val id: Long) : RoomAction
    data class OnConfirmEditRoom(
        val id: Long,
        val newName: String,
        val newColor: Long,
        val newIcon: String
    ) : RoomAction
}