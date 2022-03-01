package com.artworkspace.github.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.artworkspace.github.BuildConfig
import com.artworkspace.github.data.local.entity.UserEntity
import com.artworkspace.github.data.local.room.UserDao
import com.artworkspace.github.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val preferences: SettingPreferences
) {

    suspend fun saveUserAsFavorite(user: UserEntity) {
        userDao.insert(user)
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        preferences.saveThemeSetting(isDarkModeActive)
    }

    companion object {
        private const val API_TOKEN = "Bearer ${BuildConfig.API_KEY}"

        private var INSTANCE: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            preferences: SettingPreferences
        ): UserRepository {
            return INSTANCE ?: synchronized(this) {
                UserRepository(apiService, userDao, preferences).also {
                    INSTANCE = it
                }
            }
        }
    }
}