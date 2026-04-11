package com.example.kahon.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kahon.feature_room.data.local.Room
import com.example.kahon.feature_room.data.local.RoomDao
import com.example.kahon.feature_storage.data.local.Storage

@Database(
    entities = [Room::class, Storage::class],
    version = 4
)
abstract class KahonDatabase : RoomDatabase() {
    abstract val roomDao: RoomDao
}
