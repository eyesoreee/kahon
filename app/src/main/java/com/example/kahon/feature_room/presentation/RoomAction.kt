package com.example.kahon.feature_room.presentation

sealed interface RoomAction {
    data class OnRoomClick(val id: Long, val name: String) : RoomAction
    data object OnAddRoomClick : RoomAction
    data object OnDismissAddRoomDialog : RoomAction
    data class OnNewRoomNameChange(val name: String) : RoomAction
    data object OnConfirmAddRoom : RoomAction
}