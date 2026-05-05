package com.example.kahon.feature_item.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.kahon.core.navigation.ItemRoute
import com.example.kahon.feature_item.data.local.Item
import com.example.kahon.feature_item.domain.repository.ItemRepository
import com.example.kahon.feature_room.domain.repository.RoomRepository
import com.example.kahon.feature_storage.domain.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val storageRepository: StorageRepository,
    private val roomRepository: RoomRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {
    private val routeData = savedStateHandle.toRoute<ItemRoute>()
    val roomName = routeData.roomName
    val storageName = routeData.storageName

    private var storageId: Long? = null

    private val _uiState = MutableStateFlow<ItemUiState>(ItemUiState.Loading)
    val uiState: StateFlow<ItemUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun onAction(action: ItemAction) {
        when (action) {
            ItemAction.ShowAddItemDialog -> {
                _uiState.update { state ->
                    if (state is ItemUiState.Ready) {
                        state.copy(state = state.state.copy(isAddingItem = true))
                    } else state
                }
            }

            ItemAction.DismissDialog -> {
                _uiState.update { state ->
                    if (state is ItemUiState.Ready) {
                        state.copy(
                            state = state.state.copy(
                                isAddingItem = false,
                                editingItem = null
                            )
                        )
                    } else state
                }
            }

            is ItemAction.AddItem -> {
                val sId = storageId ?: return
                viewModelScope.launch {
                    val newItem = Item(
                        name = action.name,
                        category = action.category,
                        storageId = sId,
                        quantity = 1
                    )
                    itemRepository.insertItem(newItem)
                    loadData()
                    onAction(ItemAction.DismissDialog)
                }
            }

            is ItemAction.EditItem -> {
                _uiState.update { state ->
                    if (state is ItemUiState.Ready) {
                        state.copy(state = state.state.copy(editingItem = action.item))
                    } else state
                }
            }

            is ItemAction.UpdateItem -> {
                viewModelScope.launch {
                    itemRepository.updateItem(action.item)
                    loadData()
                    onAction(ItemAction.DismissDialog)
                }
            }

            is ItemAction.DeleteItem -> {
                viewModelScope.launch {
                    itemRepository.deleteItem(action.item.id)
                    loadData()
                    onAction(ItemAction.DismissDialog)
                }
            }

            is ItemAction.DeleteCategory -> {
                viewModelScope.launch {
                    itemRepository.clearCategory(action.category)
                    loadData()
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val roomExists = roomRepository.doesRoomExist(roomName)
            if (!roomExists) {
                _uiState.value = ItemUiState.Error(
                    "The room '$roomName' was not found."
                )
                return@launch
            }

            val sId = storageRepository.getStorageId(roomName, storageName)
            if (sId == null) {
                _uiState.value = ItemUiState.Error(
                    "The storage '$storageName' was not found in '$roomName'."
                )
                return@launch
            }
            storageId = sId

            val items = itemRepository.getItems(sId)
            val categoriesFromDb = itemRepository.getAllCategories()

            val defaultCategories = listOf("Electronics", "Clothing", "Kitchen", "Tools", "Office")
            val allCategories = (defaultCategories + categoriesFromDb).distinct().sorted()

            _uiState.value = ItemUiState.Ready(
                state = ItemState(
                    storageName = storageName,
                    roomName = roomName,
                    items = items,
                    categories = allCategories
                )
            )
        }
    }
}
