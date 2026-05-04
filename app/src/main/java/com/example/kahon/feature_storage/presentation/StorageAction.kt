package com.example.kahon.feature_storage.presentation

sealed interface StorageAction {
    data class OnConfirmAddStorage(val name: String, val color: Long) : StorageAction
    data class OnDeleteStorage(val id: Long) : StorageAction
    data class OnConfirmEditStorage(val id: Long, val newName: String, val newColor: Long) : StorageAction
}