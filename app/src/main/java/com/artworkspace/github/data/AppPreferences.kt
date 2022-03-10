package com.artworkspace.github.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    /**
     * Get theme setting for dark mode state from DataStore
     *
     * @return Flow<Boolean>
     */
    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    /**
     * Save theme setting for dark mode state to DataStore
     *
     * @param darkModeState Dark mode state to save
     */
    suspend fun saveThemeSetting(darkModeState: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = darkModeState
        }
    }

    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_setting")

        @Volatile
        private var INSTANCE: AppPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): AppPreferences {
            return INSTANCE ?: synchronized(this) {
                AppPreferences(dataStore).also {
                    INSTANCE = it
                }
            }
        }
    }
}