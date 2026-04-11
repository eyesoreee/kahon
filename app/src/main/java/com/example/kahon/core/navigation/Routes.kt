package com.example.kahon.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object RoomsRoute

@Serializable
data class ContainersRoute(
    val roomId: String,
    val roomName: String
)

@Serializable
data class ItemsRoute(
    val containerId: String,
    val containerName: String
)