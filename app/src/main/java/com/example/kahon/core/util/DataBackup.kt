package com.example.kahon.core.util

import com.example.kahon.feature_item.data.local.Item
import com.example.kahon.feature_room.data.local.Room
import com.example.kahon.feature_storage.data.local.Storage
import kotlinx.serialization.Serializable

@Serializable
data class DataBackup(
    val rooms: List<Room>,
    val storages: List<Storage>,
    val items: List<Item>
)
