package com.example.kahon.feature_room.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kahon.feature_room.data.local.Room
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
            is RoomAction.OnConfirmAddRoom -> onConfirmAddRoom(action.name, action.color, action.icon)
            is RoomAction.OnDeleteRoom -> onDeleteRoom(action.id)
            is RoomAction.OnConfirmEditRoom -> onConfirmEditRoom(action.id, action.newName, action.newColor, action.newIcon)
        }
    }

    private fun loadRooms() {
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

    private fun onConfirmAddRoom(name: String, color: Long, icon: String) {
        if (name.isBlank()) return

        viewModelScope.launch {
            roomRepository.insertRoom(
                Room(
                    name = name,
                    color = color,
                    icon = icon
                )
            )
            loadRooms()
        }
    }

    private fun onDeleteRoom(id: Long) {
        viewModelScope.launch {
            roomRepository.deleteRoom(id)
            loadRooms()
        }
    }

    private fun onConfirmEditRoom(id: Long, newName: String, newColor: Long, newIcon: String) {
        if (newName.isBlank()) return

        viewModelScope.launch {
            roomRepository.updateRoom(
                Room(
                    id = id,
                    name = newName,
                    color = newColor,
                    icon = newIcon
                )
            )
            loadRooms()
        }
    }
}
