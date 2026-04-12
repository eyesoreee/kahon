package com.example.kahon.feature_storage.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Atomically updates the [MutableStateFlow] when its current value is [StorageUiState.Ready].
 * If the current state is not `Ready`, the update is ignored.
 *
 * @param block Transformation to apply to the nested [StorageState].
 */
inline fun MutableStateFlow<StorageUiState>.updateWhenReady(
    crossinline block: (StorageState) -> StorageState
) {
    update { current ->
        when (current) {
            is StorageUiState.Ready -> current.copy(storageState = block(current.storageState))
            else -> current
        }
    }
}
