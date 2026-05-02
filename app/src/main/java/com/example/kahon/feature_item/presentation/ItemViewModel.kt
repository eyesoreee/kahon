package com.example.kahon.feature_item.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.kahon.core.navigation.ItemRoute
import com.example.kahon.feature_item.domain.model.Item
import com.example.kahon.feature_room.domain.repository.RoomRepository
import com.example.kahon.feature_storage.domain.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storageRepository: StorageRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {
    private val routeData = savedStateHandle.toRoute<ItemRoute>()
    val roomName = routeData.roomName
    val storageName = routeData.storageName

    private val _uiState = MutableStateFlow<ItemUiState>(ItemUiState.Loading)
    val uiState: StateFlow<ItemUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    fun onAction(action: ItemAction) {
        // Handle actions
    }

    private fun loadItems() {
        viewModelScope.launch {
            _uiState.value = ItemUiState.Loading
            delay(300)

            val roomExists = roomRepository.doesRoomExist(roomName)
            if (!roomExists) {
                _uiState.value = ItemUiState.Error(
                    "The room '$roomName' was not found. It may have been renamed or deleted."
                )
                return@launch
            }

            val storageExists = storageRepository.doesStorageExist(roomName, storageName)
            if (!storageExists) {
                _uiState.value = ItemUiState.Error(
                    "The storage '$storageName' was not found in '$roomName'. It may have been moved or deleted."
                )
                return@launch
            }

            _uiState.value = ItemUiState.Ready(
                state = ItemState(
                    containerName = storageName,
                    roomName = roomName,
                    items = listOf(
                        Item(1, "MacBook Charger", "Cables"),
                        Item(2, "HDMI Cable", "Cables"),
                        Item(3, "USB-C Hub", "Accessories"),
                        Item(4, "Power Bank", "Electronics")
                    )
                )
            )
        }
    }
}
