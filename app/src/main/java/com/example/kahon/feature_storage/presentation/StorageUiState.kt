package com.example.kahon.feature_storage.presentation

sealed class StorageUiState {
    data object Loading : StorageUiState()
    data class Ready(val storageState: StorageState) : StorageUiState()
    data class Error(val errorMessage: String?) : StorageUiState()
}