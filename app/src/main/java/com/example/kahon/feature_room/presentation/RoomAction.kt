package com.example.kahon.feature_room.presentation

sealed interface RoomAction {
    data class OnConfirmAddRoom(val name: String) : RoomAction
    data class OnDeleteRoom(val id: Long) : RoomAction
    data class OnConfirmEditRoom(val id: Long, val newName: String) : RoomAction
}