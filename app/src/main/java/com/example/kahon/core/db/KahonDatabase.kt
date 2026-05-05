package com.example.kahon.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kahon.feature_item.data.local.Item
import com.example.kahon.feature_item.data.local.ItemDao
import com.example.kahon.feature_room.data.local.Room
import com.example.kahon.feature_room.data.local.RoomDao
import com.example.kahon.feature_storage.data.local.Storage
import com.example.kahon.feature_storage.data.local.StorageDao

@Database(
    entities = [
        Room::class,
        Storage::class,
        Item::class
    ],
    version = 8
)
abstract class KahonDatabase : RoomDatabase() {
    abstract val roomDao: RoomDao
    abstract val storageDao: StorageDao
    abstract val itemDao: ItemDao
}
