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
            is RoomAction.OnAddRoomClick -> onAddRoomClick()
            is RoomAction.OnDismissAddRoomDialog -> onDismissAddRoomDialog()
            is RoomAction.OnNewRoomNameChange -> onNewRoomNameChange(action.name)
            is RoomAction.OnConfirmAddRoom -> onConfirmAddRoom()
            is RoomAction.OnRoomLongClick -> onRoomLongClick(action.id)
            is RoomAction.OnDismissRoomOptions -> onDismissRoomOptions()
            is RoomAction.OnEditRoomClick -> onEditRoomClick(action.id)
            is RoomAction.OnDeleteRoomClick -> onDeleteRoomClick(action.id)
            is RoomAction.OnDismissEditRoomDialog -> onDismissEditRoomDialog()
            is RoomAction.OnEditRoomNameChange -> onEditRoomNameChange(action.name)
            is RoomAction.OnConfirmEditRoom -> onConfirmEditRoom()
            is RoomAction.OnRoomClick -> TODO()
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

    private fun onAddRoomClick() {
        _uiState.updateWhenReady { it.copy(isAddRoomDialogOpen = true) }
    }

    private fun onDismissAddRoomDialog() {
        _uiState.updateWhenReady { it.copy(isAddRoomDialogOpen = false) }
    }

    private fun onNewRoomNameChange(name: String) {
        _uiState.updateWhenReady { it.copy(newRoomName = name) }
    }

    private fun onConfirmAddRoom() {
        val currentState = (uiState.value as? RoomUiState.Ready)?.roomState ?: return
        if (currentState.newRoomName.isBlank()) return

        viewModelScope.launch {
            roomRepository.insertRoom(Room(name = currentState.newRoomName))
            onAction(RoomAction.OnDismissAddRoomDialog)
            loadRooms()
        }
    }

    private fun onRoomLongClick(id: Long?) {
        _uiState.updateWhenReady {
            it.copy(
                isRoomOptionsDialogOpen = true,
                selectedRoomId = id
            )
        }
    }

    private fun onDismissRoomOptions() {
        _uiState.updateWhenReady {
            it.copy(
                isRoomOptionsDialogOpen = false,
                selectedRoomId = null
            )
        }
    }

    private fun onEditRoomClick(id: Long) {
        _uiState.updateWhenReady { roomState ->
            val room = roomState.rooms.find { it.id == id }
            roomState.copy(
                isRoomOptionsDialogOpen = false,
                isEditRoomDialogOpen = true,
                editRoomName = room?.name ?: ""
            )
        }
    }

    private fun onDeleteRoomClick(id: Long) {
        val currentState = (uiState.value as? RoomUiState.Ready)?.roomState ?: return
        val room = currentState.rooms.find { it.id == id }

        if (room != null && room.storageCount > 0) {
            onAction(RoomAction.OnDismissRoomOptions)
            return
        }

        viewModelScope.launch {
            roomRepository.deleteRoom(id)
            onAction(RoomAction.OnDismissRoomOptions)
            loadRooms()
        }
    }

    private fun onDismissEditRoomDialog() {
        _uiState.updateWhenReady {
            it.copy(
                isEditRoomDialogOpen = false,
                editRoomName = "",
                selectedRoomId = null
            )
        }
    }

    private fun onEditRoomNameChange(name: String) {
        _uiState.updateWhenReady { it.copy(editRoomName = name) }
    }

    private fun onConfirmEditRoom() {
        val currentState = (uiState.value as? RoomUiState.Ready)?.roomState ?: return
        val roomId = currentState.selectedRoomId ?: return
        if (currentState.editRoomName.isBlank()) return

        viewModelScope.launch {
            roomRepository.updateRoom(
                Room(
                    id = roomId,
                    name = currentState.editRoomName
                )
            )
            onAction(RoomAction.OnDismissEditRoomDialog)
            loadRooms()
        }
    }
}