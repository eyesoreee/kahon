package com.example.kahon.core.di

import android.content.Context
import androidx.room.Room
import com.example.kahon.core.db.KahonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        KahonDatabase::class.java,
        "kahon_db"
    ).build()

    @Singleton
    @Provides
    fun provideLocationDao(db: KahonDatabase) = db.locationDao
}