package com.example.kahon.feature_item.presentation

sealed class ItemUiState {
    data object Loading : ItemUiState()
    data class Ready(val state: ItemState) : ItemUiState()
    data class Error(val message: String?) : ItemUiState()
}