package com.example.kahon.feature_item.presentation

import androidx.compose.runtime.Immutable

@Immutable
sealed class ItemUiState {
    data object Loading : ItemUiState()
    data class Ready(val state: ItemState) : ItemUiState()
    data class Error(val message: String?) : ItemUiState()
}