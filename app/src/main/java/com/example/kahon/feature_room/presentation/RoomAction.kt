package com.example.kahon.feature_room.presentation

sealed interface RoomAction {
    data class OnRoomLongClick(val id: Long) : RoomAction
    data object OnAddRoomClick : RoomAction
    data object OnDismissAddRoomDialog : RoomAction
    data class OnNewRoomNameChange(val name: String) : RoomAction
    data object OnConfirmAddRoom : RoomAction
    
    data object OnDismissRoomOptions : RoomAction
    data class OnEditRoomClick(val id: Long) : RoomAction
    data class OnDeleteRoomClick(val id: Long) : RoomAction
    
    data object OnDismissEditRoomDialog : RoomAction
    data class OnEditRoomNameChange(val name: String) : RoomAction
    data object OnConfirmEditRoom : RoomAction
}