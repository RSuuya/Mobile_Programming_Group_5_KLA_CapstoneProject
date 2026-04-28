package com.courseworktracker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val IS_COORDINATOR = booleanPreferencesKey("is_coordinator")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .map { preferences ->
            UserPreferences(
                userName = preferences[PreferencesKeys.USER_NAME] ?: "Student",
                isCoordinator = preferences[PreferencesKeys.IS_COORDINATOR] ?: false,
                isLoggedIn = preferences[PreferencesKeys.IS_LOGGED_IN] ?: false,
                isDarkMode = prefs[DARK_MODE_KEY] ?: false
            )
        }

    suspend fun updateLoginState(userName: String, isCoordinator: Boolean, isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = userName
            preferences[PreferencesKeys.IS_COORDINATOR] = isCoordinator
            preferences[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn
        }
    }
    suspend fun toggleDarkMode(isDarkMode: Boolean) {
        // if using DataStore:
        dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = isDarkMode
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}

data class UserPreferences(
    val userName: String,
    val isCoordinator: Boolean,
    val isLoggedIn: Boolean,
    val isDarkMode: Boolean = false
)