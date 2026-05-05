package com.example.kahon.feature_item.presentation

import com.example.kahon.feature_item.data.local.Item

sealed interface ItemAction {
    data object ShowAddItemDialog : ItemAction
    data object DismissDialog : ItemAction
    data class AddItem(val name: String, val category: String) : ItemAction
    data class EditItem(val item: Item) : ItemAction
    data class UpdateItem(val item: Item) : ItemAction
    data class DeleteItem(val item: Item) : ItemAction
    data class DeleteCategory(val category: String) : ItemAction
}