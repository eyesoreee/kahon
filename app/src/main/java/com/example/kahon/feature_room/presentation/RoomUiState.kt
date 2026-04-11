package com.example.kahon.feature_room.presentation

sealed class RoomUiState {
    data object Loading : RoomUiState()
    data class Ready(val roomState: RoomState) : RoomUiState()
    data class Error(val errorMessage: String?) : RoomUiState()
}