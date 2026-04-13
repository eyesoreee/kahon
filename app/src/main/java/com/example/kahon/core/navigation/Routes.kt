package com.example.kahon.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object RoomRoute

@Serializable
data class StorageRoute(
    val roomId: String,
    val roomName: String
)

@Serializable
data class ItemRoute(
    val containerId: String,
    val containerName: String
)