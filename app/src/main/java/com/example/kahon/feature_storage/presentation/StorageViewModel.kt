package com.example.kahon.feature_storage.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StorageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StorageUiState.Loading)
    val uiState: StateFlow<StorageUiState> = _uiState.asStateFlow()

    fun onAction(action: StorageAction) {
//        when (action) {
//        }
    }
}