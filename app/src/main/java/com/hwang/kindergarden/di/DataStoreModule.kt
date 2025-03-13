package com.hwang.kindergarden.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.hwang.kindergarden.data.datastore.DataStoreKeys
import com.hwang.kindergarden.data.datastore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DataStoreKeys.PREF_NAME
)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore
    
    @Provides
    @Singleton
    fun provideDataStoreManager(
        dataStore: DataStore<Preferences>
    ): DataStoreManager = DataStoreManager(dataStore)
} 