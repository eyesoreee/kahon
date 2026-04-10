package com.example.kahon.feature_home.presentation

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Ready(val spacesState: HomeState) : HomeUiState()
    data class Error(val errorMessage: String?) : HomeUiState()
}