package com.example.kahon.feature_room.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kahon.feature_room.data.local.Room
import com.example.kahon.feature_room.domain.model.RoomWithCount
import com.example.kahon.feature_room.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    val uiState: StateFlow<RoomUiState> = roomRepository.getRooms()
        .map<List<RoomWithCount>, RoomUiState> { rooms ->
            RoomUiState.Ready(
                roomState = RoomState(rooms = rooms)
            )
        }
        .catch { e ->
            emit(RoomUiState.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RoomUiState.Loading
        )

    fun onAction(action: RoomAction) {
        when (action) {
            is RoomAction.OnConfirmAddRoom -> onConfirmAddRoom(
                action.name,
                action.color,
                action.icon
            )

            is RoomAction.OnDeleteRoom -> onDeleteRoom(action.id)
            is RoomAction.OnConfirmEditRoom -> onConfirmEditRoom(
                action.id,
                action.newName,
                action.newColor,
                action.newIcon
            )
        }
    }

    private fun onConfirmAddRoom(name: String, color: Long, icon: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            roomRepository.insertRoom(
                Room(name = name, color = color, icon = icon)
            )
        }
    }

    private fun onDeleteRoom(id: Long) {
        viewModelScope.launch {
            roomRepository.deleteRoom(id)
        }
    }

    private fun onConfirmEditRoom(id: Long, newName: String, newColor: Long, newIcon: String) {
        if (newName.isBlank()) return
        viewModelScope.launch {
            roomRepository.updateRoom(
                Room(id = id, name = newName, color = newColor, icon = newIcon)
            )
        }
    }
}
