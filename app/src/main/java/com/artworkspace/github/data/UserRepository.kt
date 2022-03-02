package com.artworkspace.github.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.artworkspace.github.BuildConfig
import com.artworkspace.github.data.local.entity.UserEntity
import com.artworkspace.github.data.local.room.UserDao
import com.artworkspace.github.data.remote.response.SimpleUser
import com.artworkspace.github.data.remote.response.User
import com.artworkspace.github.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val preferences: AppPreferences
) {

    fun searchUserByUsername(q: String): LiveData<Result<ArrayList<SimpleUser>>> = liveData {
        emit(Result.Loading)
        try {
            val users = apiService.searchUsername(token = API_TOKEN, q).items
            emit(Result.Success(users))
        } catch (e: Exception) {
            Log.d(TAG, "searchUserByUsername: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserFollowing(id: String): LiveData<Result<ArrayList<SimpleUser>>> = liveData {
        emit(Result.Loading)
        try {
            val users = apiService.getUserFollowing(token = API_TOKEN, id)
            emit(Result.Success(users))
        } catch (e: Exception) {
            Log.d(TAG, "getUserFollowing: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserFollowers(id: String): LiveData<Result<ArrayList<SimpleUser>>> = liveData {
        emit(Result.Loading)
        try {
            val users = apiService.getUserFollowers(token = API_TOKEN, id)
            emit(Result.Success(users))
        } catch (e: Exception) {
            Log.d(TAG, "getUserFollowers: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserDetail(id: String): LiveData<Result<User>> = liveData {
        emit(Result.Loading)
        try {
            val user = apiService.getUserDetail(token = API_TOKEN, id)
            emit(Result.Success(user))
        } catch (e: Exception) {
            Log.d(TAG, "getUserDetail: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    /**
     * Determine this user is favorite or not
     *
     * @param id User id
     * @return LiveData<Boolean>
     */
    fun isFavoriteUser(id: String): LiveData<Boolean> = liveData {
        emit(userDao.isFavoriteUser(id))
    }

    /**
     * Get all favorite users from database
     *
     * @return LiveData<List<UserEntity>>
     */
    fun getAllFavoriteUsers(): LiveData<List<UserEntity>> = userDao.getAllUsers()

    /**
     * Delete a favorite user from database
     *
     * @param user User to delete
     */
    suspend fun deleteFromFavorite(user: UserEntity) {
        userDao.delete(user)
    }

    /**
     * Save user as favorite to database
     *
     * @param user User to save
     */
    suspend fun saveUserAsFavorite(user: UserEntity) {
        userDao.insert(user)
    }

    fun getLastSearchQuery(): LiveData<String> {
        return preferences.getLastSearchQuery().asLiveData()
    }

    suspend fun saveLastSearchQuery(query: String) {
        preferences.saveLastSearchQuery(query)
    }

    /**
     * Get theme setting for dark mode state from DataStore
     *
     * @return LiveData<Boolean>
     */
    fun getThemeSetting(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    /**
     * Save theme setting for dark mode state to DataStore
     *
     * @param isDarkModeActive Dark mode state to save
     */
    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        preferences.saveThemeSetting(isDarkModeActive)
    }

    companion object {
        private const val API_TOKEN = "Bearer ${BuildConfig.API_KEY}"
        private val TAG = UserRepository::class.java.simpleName

        private var INSTANCE: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            preferences: AppPreferences
        ): UserRepository {
            return INSTANCE ?: synchronized(this) {
                UserRepository(apiService, userDao, preferences).also {
                    INSTANCE = it
                }
            }
        }
    }
}