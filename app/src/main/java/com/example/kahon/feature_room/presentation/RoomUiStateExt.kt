package com.example.kahon.feature_room.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Atomically updates the [MutableStateFlow] when its current value is [RoomUiState.Ready].
 * If the current state is not `Ready`, the update is ignored.
 *
 * @param block Transformation to apply to the nested [RoomState].
 */
inline fun MutableStateFlow<RoomUiState>.updateWhenReady(
    crossinline block: (RoomState) -> RoomState
) {
    update { current ->
        when (current) {
            is RoomUiState.Ready -> current.copy(roomState = block(current.roomState))
            else -> current
        }
    }
}