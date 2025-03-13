// data/preferences/PreferencesKeys.kt
package com.hwang.kindergarden.data.datastore

object DataStoreKeys {
    const val PREF_NAME = "app_preferences"
    
    object Welcome {
        const val SHOULD_SHOW_WELCOME = "should_show_welcome"
    }

    object User {
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"
        const val USER_EMAIL = "user_email"
    }
    
    object Settings {
        const val DARK_MODE = "dark_mode"
        const val NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val LANGUAGE = "language"
    }
}