package com.example.kahon.feature_item.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.kahon.core.navigation.ItemRoute
import com.example.kahon.feature_item.domain.model.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val routeData = savedStateHandle.toRoute<ItemRoute>()
    val roomName = routeData.roomName
    val storageName = routeData.storageName

    private val _uiState = MutableStateFlow<ItemUiState>(ItemUiState.Loading)
    val uiState: StateFlow<ItemUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    fun onAction(action: ItemAction) {
        when (action) {
            else -> {}
        }
    }

    private fun loadItems() {
        // For now, providing dummy data as requested for design only
        _uiState.value = ItemUiState.Ready(
            state = ItemState(
                containerName = storageName,
                roomName = roomName,
                items = listOf(
                    Item(1, "MacBook Charger", "Cables"),
                    Item(2, "HDMI Cable", "Cables"),
                    Item(3, "USB-C Hub", "Accessories"),
                    Item(4, "Power Bank", "Electronics")
                )
            )
        )
    }
}
