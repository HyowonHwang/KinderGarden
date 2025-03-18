package com.hwang.kindergarden.di

import com.google.firebase.database.FirebaseDatabase
import com.hwang.kindergarden.data.repository.VideoContentRepositoryImpl
import com.hwang.kindergarden.domain.repository.VideoContentRepository
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
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideRoomRepository(
        database: FirebaseDatabase
    ): VideoContentRepository = VideoContentRepositoryImpl(database)
} 