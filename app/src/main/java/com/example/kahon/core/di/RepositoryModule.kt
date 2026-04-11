package com.example.kahon.core.di

import com.example.kahon.feature_room.data.local.RoomDao
import com.example.kahon.feature_room.data.repository.RoomRepositoryImp
import com.example.kahon.feature_room.domain.repository.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRoomRepository(roomDao: RoomDao): RoomRepository {
        return RoomRepositoryImp(roomDao)
    }
}