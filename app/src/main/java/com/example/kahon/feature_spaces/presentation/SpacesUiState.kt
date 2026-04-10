package com.example.kahon.feature_spaces.presentation

sealed class SpacesUiState {
    data object Loading : SpacesUiState()
    data class Ready(val spacesState: SpacesState) : SpacesUiState()
    data class Error(val errorMessage: String?) : SpacesUiState()
}