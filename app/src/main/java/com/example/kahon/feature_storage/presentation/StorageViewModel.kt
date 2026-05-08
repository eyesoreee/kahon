package com.example.kahon.feature_storage.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.kahon.core.navigation.StorageRoute
import com.example.kahon.feature_storage.data.local.Storage
import com.example.kahon.feature_storage.domain.model.StorageWithCount
import com.example.kahon.feature_storage.domain.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val routeData = savedStateHandle.toRoute<StorageRoute>()
    private val roomId = routeData.roomId.toLong()
    val roomName = routeData.roomName

    val uiState: StateFlow<StorageUiState> = storageRepository.getStorages(roomId)
        .map<List<StorageWithCount>, StorageUiState> { storages ->
            StorageUiState.Ready(
                storageState = StorageState(
                    storages = storages
                )
            )
        }
        .catch { e ->
            emit(StorageUiState.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StorageUiState.Loading
        )

    fun onAction(action: StorageAction) {
        when (action) {
            is StorageAction.OnConfirmAddStorage -> onConfirmAddStorage(action.name, action.color)
            is StorageAction.OnDeleteStorage -> onDeleteStorage(action.id)
            is StorageAction.OnConfirmEditStorage -> onConfirmEditStorage(action.id, action.newName, action.newColor)
        }
    }

    private fun onConfirmAddStorage(name: String, color: Long) {
        if (name.isBlank()) return

        viewModelScope.launch {
            storageRepository.insertStorage(
                Storage(
                    name = name,
                    roomId = roomId,
                    color = color
                )
            )
        }
    }

    private fun onDeleteStorage(id: Long) {
        viewModelScope.launch {
            storageRepository.deleteStorage(id)
        }
    }

    private fun onConfirmEditStorage(id: Long, newName: String, newColor: Long) {
        if (newName.isBlank()) return

        viewModelScope.launch {
            storageRepository.updateStorage(
                Storage(
                    id = id,
                    name = newName,
                    roomId = roomId,
                    color = newColor
                )
            )
        }
    }
}
