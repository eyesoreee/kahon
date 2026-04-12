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
            is StorageAction.OnAddStorageClick -> onAddStorageClick()
            is StorageAction.OnDismissAddStorageDialog -> onDismissAddStorageDialog()
            is StorageAction.OnNewStorageNameChange -> onNewStorageNameChange(action.name)
            is StorageAction.OnConfirmAddStorage -> onConfirmAddStorage()
            is StorageAction.OnStorageLongClick -> onStorageLongClick(action.id)
            is StorageAction.OnDismissStorageOptions -> onDismissStorageOptions()
            is StorageAction.OnEditStorageClick -> onEditStorageClick(action.id)
            is StorageAction.OnDeleteStorageClick -> onDeleteStorageClick(action.id)
            is StorageAction.OnDismissEditStorageDialog -> onDismissEditStorageDialog()
            is StorageAction.OnEditStorageNameChange -> onEditStorageNameChange(action.name)
            is StorageAction.OnConfirmEditStorage -> onConfirmEditStorage()
            is StorageAction.OnStorageClick -> {}
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

    private fun onAddStorageClick() {
        _uiState.updateWhenReady { it.copy(isAddStorageDialogOpen = true) }
    }

    private fun onDismissAddStorageDialog() {
        _uiState.updateWhenReady { it.copy(isAddStorageDialogOpen = false, newStorageName = "") }
    }

    private fun onNewStorageNameChange(name: String) {
        _uiState.updateWhenReady { it.copy(newStorageName = name) }
    }

    private fun onConfirmAddStorage() {
        val currentState = (uiState.value as? StorageUiState.Ready)?.storageState ?: return
        if (currentState.newStorageName.isBlank()) return

        viewModelScope.launch {
            storageRepository.insertStorage(
                Storage(
                    name = currentState.newStorageName,
                    roomId = roomId
                )
            )
            onDismissAddStorageDialog()
            loadStorages()
        }
    }

    private fun onStorageLongClick(id: Long) {
        _uiState.updateWhenReady {
            it.copy(
                isStorageOptionsDialogOpen = true,
                selectedStorageId = id
            )
        }
    }

    private fun onDismissStorageOptions() {
        _uiState.updateWhenReady {
            it.copy(
                isStorageOptionsDialogOpen = false,
                selectedStorageId = null
            )
        }
    }

    private fun onEditStorageClick(id: Long) {
        _uiState.updateWhenReady { storageState ->
            val storage = storageState.storages.find { it.id == id }
            storageState.copy(
                isStorageOptionsDialogOpen = false,
                isEditStorageDialogOpen = true,
                editStorageName = storage?.name ?: ""
            )
        }
    }

    private fun onDeleteStorageClick(id: Long) {
        val currentState = (uiState.value as? StorageUiState.Ready)?.storageState ?: return
        val storage = currentState.storages.find { it.id == id }

        if (storage != null && storage.itemCount > 0) {
            onDismissStorageOptions()
            return
        }

        viewModelScope.launch {
            storageRepository.deleteStorage(id)
            onDismissStorageOptions()
            loadStorages()
        }
    }

    private fun onDismissEditStorageDialog() {
        _uiState.updateWhenReady {
            it.copy(
                isEditStorageDialogOpen = false,
                editStorageName = "",
                selectedStorageId = null
            )
        }
    }

    private fun onEditStorageNameChange(name: String) {
        _uiState.updateWhenReady { it.copy(editStorageName = name) }
    }

    private fun onConfirmEditStorage() {
        val currentState = (uiState.value as? StorageUiState.Ready)?.storageState ?: return
        val storageId = currentState.selectedStorageId ?: return
        if (currentState.editStorageName.isBlank()) return

        viewModelScope.launch {
            storageRepository.updateStorage(
                Storage(
                    id = storageId,
                    name = currentState.editStorageName,
                    roomId = roomId
                )
            )
            onDismissEditStorageDialog()
            loadStorages()
        }
    }
}
