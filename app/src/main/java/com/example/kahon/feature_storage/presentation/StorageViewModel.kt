package com.example.kahon.feature_storage.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.kahon.core.navigation.StorageRoute
import com.example.kahon.feature_storage.data.local.Storage
import com.example.kahon.feature_storage.domain.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _uiState = MutableStateFlow<StorageUiState>(StorageUiState.Loading)
    val uiState: StateFlow<StorageUiState> = _uiState.asStateFlow()

    init {
        loadStorages()
    }

    fun onAction(action: StorageAction) {
        when (action) {
            is StorageAction.OnConfirmAddStorage -> onConfirmAddStorage(action.name)
            is StorageAction.OnDeleteStorage -> onDeleteStorage(action.id)
            is StorageAction.OnConfirmEditStorage -> onConfirmEditStorage(action.id, action.newName)
        }
    }

    private fun loadStorages() {
        _uiState.value = StorageUiState.Loading
        viewModelScope.launch {
            try {
                val storages = storageRepository.getStorages(roomId)
                _uiState.value = StorageUiState.Ready(
                    storageState = StorageState(
                        storages = storages
                    )
                )
            } catch (e: Exception) {
                _uiState.value = StorageUiState.Error(e.message)
            }
        }
    }

    private fun onConfirmAddStorage(name: String) {
        if (name.isBlank()) return

        viewModelScope.launch {
            storageRepository.insertStorage(
                Storage(
                    name = name,
                    roomId = roomId
                )
            )
            loadStorages()
        }
    }

    private fun onDeleteStorage(id: Long) {
        viewModelScope.launch {
            storageRepository.deleteStorage(id)
            loadStorages()
        }
    }

    private fun onConfirmEditStorage(id: Long, newName: String) {
        if (newName.isBlank()) return

        viewModelScope.launch {
            storageRepository.updateStorage(
                Storage(
                    id = id,
                    name = newName,
                    roomId = roomId
                )
            )
            loadStorages()
        }
    }
}
