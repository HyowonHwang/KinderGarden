package com.hwang.kindergarden.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val SHOULD_SHOW_WELCOME = booleanPreferencesKey(DataStoreKeys.Welcome.SHOULD_SHOW_WELCOME)
        val USER_ID = stringPreferencesKey(DataStoreKeys.User.USER_ID)
        val USER_NAME = stringPreferencesKey(DataStoreKeys.User.USER_NAME)
        val DARK_MODE = booleanPreferencesKey(DataStoreKeys.Settings.DARK_MODE)
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey(DataStoreKeys.Settings.NOTIFICATIONS_ENABLED)
    }

    val shouldShowWelcome: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            preferences[Keys.SHOULD_SHOW_WELCOME] ?: true
        }

    val userId: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[Keys.USER_ID]
        }

    val userName: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[Keys.USER_NAME]
        }

    val isDarkMode: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            preferences[Keys.DARK_MODE] ?: false
        }

    val isNotificationsEnabled: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            preferences[Keys.NOTIFICATIONS_ENABLED] ?: true
        }

    suspend fun setShouldShowWelcome(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.SHOULD_SHOW_WELCOME] = show
        }
    }

    suspend fun setUserId(userId: String?) {
        dataStore.edit { preferences ->
            if (userId != null) {
                preferences[Keys.USER_ID] = userId
            } else {
                preferences.remove(Keys.USER_ID)
            }
        }
    }

    suspend fun setUserName(userName: String?) {
        dataStore.edit { preferences ->
            if (userName != null) {
                preferences[Keys.USER_NAME] = userName
            } else {
                preferences.remove(Keys.USER_NAME)
            }
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.DARK_MODE] = enabled
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun clearPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
