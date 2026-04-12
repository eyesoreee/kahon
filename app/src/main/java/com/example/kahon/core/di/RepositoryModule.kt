package com.example.kahon.core.di

import com.example.kahon.feature_item.data.local.ItemDao
import com.example.kahon.feature_item.data.repository.ItemRepositoryImp
import com.example.kahon.feature_item.domain.repository.ItemRepository
import com.example.kahon.feature_room.data.local.RoomDao
import com.example.kahon.feature_room.data.repository.RoomRepositoryImp
import com.example.kahon.feature_room.domain.repository.RoomRepository
import com.example.kahon.feature_storage.data.local.StorageDao
import com.example.kahon.feature_storage.data.repository.StorageRepositoryImp
import com.example.kahon.feature_storage.domain.repository.StorageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRoomRepository(roomDao: RoomDao): RoomRepository {
        return RoomRepositoryImp(roomDao)
    }

    @Provides
    @Singleton
    fun provideStorageRepository(storageDao: StorageDao): StorageRepository {
        return StorageRepositoryImp(storageDao)
    }

    @Provides
    @Singleton
    fun provideItemRepository(itemDao: ItemDao): ItemRepository {
        return ItemRepositoryImp(itemDao)
    }
}