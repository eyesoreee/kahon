package com.example.kahon.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kahon.feature_room.data.local.Room
import com.example.kahon.feature_room.data.local.RoomDao

@Database(
    entities = [Room::class],
    version = 3
)
abstract class KahonDatabase : RoomDatabase() {
    abstract val roomDao: RoomDao
}
