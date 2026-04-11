package com.example.kahon.feature_room.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kahon.feature_room.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<RoomUiState>(RoomUiState.Loading)
    val uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    init {
        loadRooms()
    }

    fun onAction(action: RoomAction) {
        when (action) {
            is RoomAction.OnAddRoomClick -> {
                _uiState.value = (uiState.value as? RoomUiState.Ready)?.let { readyState ->
                    readyState.copy(roomState = readyState.roomState.copy(isAddRoomDialogOpen = true))
                } ?: uiState.value
            }

            is RoomAction.OnDismissAddRoomDialog -> {
                _uiState.value = (uiState.value as? RoomUiState.Ready)?.let { readyState ->
                    readyState.copy(
                        roomState = readyState.roomState.copy(
                            isAddRoomDialogOpen = false,
                            newRoomName = ""
                        )
                    )
                } ?: uiState.value
            }

            is RoomAction.OnNewRoomNameChange -> {
                _uiState.value = (uiState.value as? RoomUiState.Ready)?.let { readyState ->
                    readyState.copy(roomState = readyState.roomState.copy(newRoomName = action.name))
                } ?: uiState.value
            }

            is RoomAction.OnConfirmAddRoom -> {
                val currentState = (uiState.value as? RoomUiState.Ready)?.roomState ?: return
                if (currentState.newRoomName.isBlank()) return

                viewModelScope.launch {
                    roomRepository.insertRoom(
                        com.example.kahon.feature_room.data.local.Room(name = currentState.newRoomName)
                    )
                    loadRooms()
                }
            }

            else -> {}
        }
    }

    fun loadRooms() {
        _uiState.value = RoomUiState.Loading
        viewModelScope.launch {
            try {
                val rooms = roomRepository.getRooms()
                _uiState.value = RoomUiState.Ready(
                    roomState = RoomState(
                        rooms = rooms
                    )
                )
            } catch (e: Exception) {
                _uiState.value = RoomUiState.Error(e.message)
            }
        }
    }
}