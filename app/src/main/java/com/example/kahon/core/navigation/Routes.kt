package com.example.kahon.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object LocationsRoute

@Serializable
data class ContainersRoute(
    val locationId: String,
    val locationName: String
)

@Serializable
data class ItemsRoute(
    val containerId: String,
    val containerName: String
)