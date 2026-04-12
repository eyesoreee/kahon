package com.example.kahon.feature_item.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.kahon.feature_storage.data.local.Storage

@Entity(
    tableName = "item",
    foreignKeys = [
        ForeignKey(
            entity = Storage::class,
            parentColumns = ["id"],
            childColumns = ["storageId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["storageId"])]
)
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "storageId")
    val storageId: Long,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "imagePath")
    val imagePath: String? = null
)
