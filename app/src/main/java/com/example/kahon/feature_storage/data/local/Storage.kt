package com.example.kahon.feature_storage.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.kahon.feature_room.data.local.Room

@Entity(
    tableName = "storage",
    foreignKeys = [
        ForeignKey(
            entity = Room::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["roomId"])]
)
data class Storage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "roomId")
    val roomId: Long,

    @ColumnInfo(name = "color")
    val color: Long
)