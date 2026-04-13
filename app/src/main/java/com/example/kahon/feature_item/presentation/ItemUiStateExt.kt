package com.example.kahon.feature_item.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Atomically updates the [MutableStateFlow] when its current value is [ItemUiState.Ready].
 * If the current state is not `Ready`, the update is ignored.
 *
 * @param block Transformation to apply to the nested [ItemState].
 */
inline fun MutableStateFlow<ItemUiState>.updateWhenReady(
    crossinline block: (ItemState) -> ItemState
) {
    update { current ->
        when (current) {
            is ItemUiState.Ready -> current.copy(state = block(current.state))
            else -> current
        }
    }
}