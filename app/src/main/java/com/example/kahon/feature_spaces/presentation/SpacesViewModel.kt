package com.example.kahon.feature_spaces.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SpacesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<SpacesUiState>(SpacesUiState.Loading)
    val uiState: StateFlow<SpacesUiState> = _uiState.asStateFlow()

    fun onAction(action: SpacesAction) {
//        when (action) {
//        }
    }
}