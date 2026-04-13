package com.example.kahon.feature_storage.presentation

sealed interface StorageAction {
    data class OnStorageLongClick(val id: Long) : StorageAction
    data object OnAddStorageClick : StorageAction
    data object OnDismissAddStorageDialog : StorageAction
    data class OnNewStorageNameChange(val name: String) : StorageAction
    data object OnConfirmAddStorage : StorageAction

    data object OnDismissStorageOptions : StorageAction
    data class OnEditStorageClick(val id: Long) : StorageAction
    data class OnDeleteStorageClick(val id: Long) : StorageAction

    data object OnDismissEditStorageDialog : StorageAction
    data class OnEditStorageNameChange(val name: String) : StorageAction
    data object OnConfirmEditStorage : StorageAction
}