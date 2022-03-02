package com.artworkspace.github.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.artworkspace.github.data.SettingPreferences
import com.artworkspace.github.data.UserRepository
import com.artworkspace.github.data.local.room.UserDatabase
import com.artworkspace.github.data.remote.retrofit.ApiConfig

object Injection {

    /**
     * Provide repository for view model via ViewModelFactory
     *
     * @param context Context
     * @param dataStore DataStore preferences
     * @return UserRepository
     */
    fun provideRepository(context: Context, dataStore: DataStore<Preferences>): UserRepository {
        val apiService = ApiConfig.getApiService()
        val preferences = SettingPreferences.getInstance(dataStore)
        val database = UserDatabase.getDatabase(context)
        val dao = database.userDao()

        return UserRepository.getInstance(apiService, dao, preferences)
    }
}
