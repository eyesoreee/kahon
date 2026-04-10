package com.example.kahon.feature_location.presentation

sealed class LocationUiState {
    data object Loading : LocationUiState()
    data class Ready(val locationState: LocationState) : LocationUiState()
    data class Error(val errorMessage: String?) : LocationUiState()
}